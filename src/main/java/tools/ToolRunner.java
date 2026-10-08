package tools;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

/**
 * 群星規範工具組（單一檔案版）
 *
 * 依序執行：
 *   0. author     為中文論文與英文翻譯的 YAML 頭部補上缺少的 author、contact（只補不改）
 *      label      為中文論文寫入句碼標籤（只加、換、刪標籤，工具規格 2.3）
 *      stamp      更新 Git 暫存區中檔案的時間戳（只改時間戳那一行）
 *   1. validate   格式與連結檢查（稽核總表中檢查者以「工具」開頭的條目；
 *                 含與最後一次 commit 的版本比對）
 *   2. bundle     在論文資料夾中產出「形式化輸入包.md」與依稽核總表 0.3 分工表生成的各稽核輸入包
 *   3. tree       產出 PaperTree.html（引用關係圖）
 *   4. starmap    產出 starmap.html（概念關係星圖）
 *   5. concepts   產出 exports/concepts.jsonl（依工具規格第四節抽取並分類關係）
 *   6. papers     產出 exports/papers.jsonl
 *   7. checkPapers  檢查 papers.jsonl 結構
 *   8. checkHf    遠端檢查 HF dataset 狀態（需要網路；未設定 HF_DATASET 時略過）
 *
 * validate 有「不通過」時仍重建形式化輸入包，但不產生稽核輸入包，也不執行其餘後續步驟；「警告」不阻擋。
 * Issue 代碼即稽核總表的約束編號（論、譯、概、存、形、跨）。
 * 規格見《群星規範——工具規格》。
 */
public class ToolRunner {

    // ════════════════════════════════════════════════════════════
    // 設定
    // ════════════════════════════════════════════════════════════

    static final String SRC_DIR    = "src/main/java";
    static final String EXPORTS    = "exports";
    /** HF 資料集名稱（例如「帳號/資料集」）。尚未發布時留空，第 8 步會略過。 */
    static final String HF_DATASET = "";
    /** 規範文件所在目錄（相對於專案根目錄） */
    static final String DOCS_DIR   = "src/main/resources";
    /** 形式化手冊（附在形式化輸入包中）；找不到時不產生形式化輸入包 */
    static final String FORM_MANUAL = "群星規範-形式化手冊.md";
    /** 稽核總表（附在各稽核輸入包中，報告讀取「檢查者」欄）；找不到時不產生稽核輸入包 */
    static final String AUDIT_TABLE = "群星規範-稽核總表.md";
    /** 論文資料夾中的輸入包（生成物，每次執行先刪除再重建；不視為論文檔） */
    static final String FORM_BUNDLE          = "形式化輸入包.md";
    static final String AUDIT_BUNDLE_LEGACY  = "稽核輸入包.md";
    static final String AUDIT_BUNDLE_PREFIX  = "稽核輸入包-";
    /** 形式化文件頭的行數：論文、UUID */
    static final int F_HEADER_LINES = 2;
    /** 作者與聯絡方式：寫入論文的 YAML 頭部（author、contact），NewPaper 與 author 步驟共用 */
    static final String AUTHOR  = "黃正宇 / Cheng Yu Huang";
    static final String CONTACT = "mthree.tw@gmail.com";

    static boolean isGeneratedBundleName(String name) {
        return name.equals(FORM_BUNDLE) || name.equals(AUDIT_BUNDLE_LEGACY)
            || (name.startsWith(AUDIT_BUNDLE_PREFIX) && name.endsWith(".md"));
    }

    static boolean isGeneratedBundlePath(String path) {
        String name = Paths.get(path).getFileName().toString();
        return isGeneratedBundleName(name);
    }

    static String auditBundleName(String pack) {
        return AUDIT_BUNDLE_PREFIX + pack + ".md";
    }

    // ════════════════════════════════════════════════════════════
    // 資料模型
    // ════════════════════════════════════════════════════════════

    /** 概念表中的一條 */
    record Concept(String id, String mod, String zh, String en, String def,
                   boolean deprecated, int line, String raw) {}

    /** 正文中的一個概念連結 */
    record Link(String display, String targetDir, String id, int line, String file) {}

    /** 一個論元：原文與其中出現的概念（UUID.ID） */
    record Arg(String text, List<String> concepts) {}

    /** 一筆抽取出的關係（工具規格 4.1 節） */
    record Rel(String type, List<Arg> args, List<String> derivedFrom,
               String context, String confidence, String section, Set<String> localIds) {}

    /** 一篇論文或一個外部存根 */
    static class Paper {
        Path dir;
        String folder;          // paper_xxx / ext_xxx
        String uuid;
        String title;
        String titleEn;
        boolean external;
        Path zhPaper, enPaper, conceptsFile, formalizationFile;
        List<Concept> concepts = new ArrayList<>();
        List<Link> links = new ArrayList<>();
        List<Link> linksEn = new ArrayList<>();
        Set<String> refs = new LinkedHashSet<>();
        Map<String, String> conceptsYaml = new LinkedHashMap<>();
        Map<String, String> paperYaml = new LinkedHashMap<>();
        String conceptsText = "", zhText = "", enText = "", formText = "";
        /** 刪除注釋後的論文全文（整段刪除）：輸入包與資料集使用。zhText、enText 則以空行取代注釋，保留行號 */
        String zhClean = "", enClean = "";
        /** 注釋格式問題（論-17、譯-06） */
        List<String> zhNoteProblems = new ArrayList<>(), enNoteProblems = new ArrayList<>();
        List<Rel> rels = new ArrayList<>();
        /** 中文論文原檔（含注釋與句碼標籤）、可見句碼版（注釋已刪除，標籤改印為【句……】）、正文單位 */
        String zhRaw = "", zhVisible = "";
        List<Unit> units = new ArrayList<>();

        Concept byId(String id) {
            for (Concept c : concepts) if (c.id.equals(id)) return c;
            return null;
        }
        Concept byZh(String zh) {
            for (Concept c : concepts) if (zh.equals(c.zh)) return c;
            return null;
        }
        /** 英文翻譯已開始：檔案存在，且不是原封未動的模板（檔名與內容都還是佔位符） */
        boolean enStarted() {
            if (enPaper == null) return false;
            if (!baseName(enPaper).equals(EN_TITLE_PLACEHOLDER)) return true;
            String body = enText.replace("\r", "").replaceFirst("(?s)^---\n.*?\n---\n", "").trim();
            return !body.isEmpty() && !body.equals("# " + EN_TITLE_PLACEHOLDER);
        }
        /** 形式化已開始：「記號約定」「待決項」之外至少有一個段落 */
        boolean formStarted() {
            if (formalizationFile == null) return false;
            Set<String> keys = new HashSet<>(sections(afterHeader(formText.split("\r?\n", -1))).keySet());
            keys.remove("記號約定"); keys.remove("待決項");
            return !keys.isEmpty();
        }
    }

    /** 單條檢查結果 */
    record Issue(String code, String where, String detail) {}

    /** 中文論文的一個正文單位（工具規格 2.3）：句碼、種類（標題／形式段／文句）、顯示文字、行號、所在標題 */
    record Unit(String code, String kind, String text, int line, String heading) {}

    /** label 的結果：寫入標籤後的全文、各單位、資訊（內容相同、句碼相撞） */
    record Labeled(String text, List<Unit> units, List<String> info) {}

    // ════════════════════════════════════════════════════════════
    // 共用正規表達式
    // ════════════════════════════════════════════════════════════

    static final Pattern UUID_RE = Pattern.compile(
        "^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$");

    static final Pattern TS_RE = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$");

    static final Pattern ANCHOR_RE = Pattern.compile("^<a\\s+id=\"([^\"]+)\"\\s*></a>\\s*$");

    /** 概念表標題：## 修飾子 中文名 / English Name */
    static final Pattern CT_TITLE_RE =
        Pattern.compile("^##\\s+(\\S+)\\s+(\\S+)\\s+/\\s+(.+?)\\s*$");

    static final Pattern ID_RE = Pattern.compile("^c\\d{2,}$");

    static final Pattern MD_LINK_RE = Pattern.compile("\\[([^\\]]*)\\]\\(([^)]*)\\)");

    static final Pattern LOCAL_TARGET_RE = Pattern.compile("^concepts\\.md#(.+)$");
    static final Pattern UP_TARGET_RE =
        Pattern.compile("^\\.\\./(paper_[0-9a-f_]+)/concepts\\.md#(.+)$");
    static final Pattern EXT_TARGET_RE =
        Pattern.compile("^\\.\\./ext/(ext_[0-9a-f_]+)/concepts\\.md#(.+)$");

    static final Pattern F_UUID_RE  = Pattern.compile("^UUID[：:]\\s*([0-9a-f-]{36})\\s*$");
    static final Pattern F_TITLE_RE = Pattern.compile("^論文[：:]\\s*《(.+)》\\s*$");

    /** 形式化文件中的上游概念：UUID.概念名 或 ext:UUID.概念名 */
    static final Pattern PREFIXED_REF = Pattern.compile(
        "(ext:)?([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})\\."
        + "([^\\s，,、。；;（(）)：:「」【】]+)");

    /** 關係述詞（形式化手冊 5.1 節）→ relation_type（工具規格 4.2 節） */
    static final Map<String, String> PREDICATES = new LinkedHashMap<>();
    static {
        PREDICATES.put("互斥分類", "mutually_exclusive_with");
        PREDICATES.put("蘊含", "implies");
        PREDICATES.put("導致", "leads_to");
        PREDICATES.put("組成", "composed_of");
        PREDICATES.put("衝突", "conflicts_with");
        PREDICATES.put("收窄", "narrows");
        PREDICATES.put("屬於", "member_of");
    }
    static final Pattern PRED_RE = Pattern.compile(
        "(" + String.join("|", PREDICATES.keySet()) + ")\\(");

    /** 推導來源（形式化手冊 5.2 節、形-16） */
    static final Pattern DERIVE_RE = Pattern.compile("（從\\s*(.+?)\\s*推出");

    /** 程式碼區塊外禁止的關係符號（形-09） */
    static final String REL_SYMBOLS = "→⇒↝≡⊕⊗⊂⊆⊥∈∉∨∧¬=≠≤≥×";

    /** 切詞邊界（形-08） */
    static final String SPLITTER = "[\\s，。、；：:（）()「」《》〈〉【】\\[\\]\\-_+,/]+";

    static final Pattern FOREIGN = Pattern.compile("[A-Za-z\\u0370-\\u03FF]+");
    static final String NUM = "一二三四五六七八九十";


    /** 句碼標籤（工具規格 2.3、3.10；論-20） */
    static final Pattern SENT_TAG = Pattern.compile("<!--句(\\d{10})-->");
    /** 單行的 HTML 註解；內容含「句」者視為句碼標籤（含格式錯誤的變體） */
    static final Pattern HTML_COMMENT = Pattern.compile("<!--.*?-->");
    /** 形式化文件行尾的正文引用（形-38、跨-30） */
    static final Pattern CITE_RE  = Pattern.compile("【正文：([^】]*)】\\s*$");
    static final Pattern CITE_ANY = Pattern.compile("【正文：[^】]*】");
    static final Pattern CITE_CODE = Pattern.compile("句(\\d{10})");
    /** 推導標籤（形-36）：步驟、結論、循環加中文數字，以「之」連接 */
    static final String LABEL_NUM = "〇一二三四五六七八九十";
    static final Pattern LABEL_RE = Pattern.compile(
        "^((?:步驟|結論|循環)[" + LABEL_NUM + "]+(?:之[" + LABEL_NUM + "]+)*)：");
    static final Pattern LABEL_TOKEN = Pattern.compile(
        "^(?:步驟|結論|循環)[" + LABEL_NUM + "]+(?:之[" + LABEL_NUM + "]+)*$");

    // ════════════════════════════════════════════════════════════
    // 主流程
    // ════════════════════════════════════════════════════════════

    public static void main(String[] args) throws IOException {
        Path root = resolveRoot();
        Path src  = root.resolve(SRC_DIR);
        System.out.println("專案根目錄：" + root);

        if (!Files.isDirectory(src)) {
            System.out.println("找不到 " + src + "，結束。");
            return;
        }

        section("0. author、label、stamp");
        try {
            fillAuthor(root, src);
        } catch (IllegalStateException e) {
            System.out.println("✗ " + e.getMessage());
            return;
        } catch (Exception e) {
            System.out.println("（作者欄位略過：" + e.getClass().getSimpleName() + " " + e.getMessage() + "）");
        }
        try {
            labelPapers(root, src);
        } catch (IllegalStateException e) {
            System.out.println("✗ " + e.getMessage());
            return;
        } catch (Exception e) {
            System.out.println("（句碼標籤略過：" + e.getClass().getSimpleName() + " " + e.getMessage() + "）");
        }
        try {
            stamp(root);
        } catch (Exception e) {
            System.out.println("（時間戳略過：" + e.getClass().getSimpleName() + " " + e.getMessage() + "）");
        }

        List<Paper> papers = scanRepo(src);
        System.out.println("掃描到 " + papers.stream().filter(p -> !p.external).count()
            + " 篇論文、" + papers.stream().filter(p -> p.external).count() + " 個外部存根\n");

        if (papers.isEmpty()) {
            System.out.println("倉庫是空的，結束。");
            return;
        }
        clearBundles(papers);

        section("1. validate");
        List<Issue> warnings = new ArrayList<>();
        List<Issue> issues = validate(root, papers, warnings);
        report(root, issues, warnings, papers);
        if (!issues.isEmpty()) {
            // 形式化輸入包是工作材料，不是稽核通過後的產物。
            // 即使 validate 有不通過，也以本次掃描到的現行材料重建，避免舊包被清除後消失或留下過期內容。
            // 稽核輸入包與其餘後續步驟仍要求 validate 全部通過。
            section("2. bundle（只產生形式化輸入包）");
            bundleForm(root, papers);
            System.out.println("\nvalidate 未通過：已重建形式化輸入包；稽核輸入包與其餘後續步驟中止。");
            return;
        }

        Map<String, Paper> byFolder = byFolder(papers);
        for (Paper p : papers) if (!p.external) p.rels = extractRelations(p, byFolder);

        section("2. bundle");
        bundle(root, papers, warnings);

        section("3. tree");
        tree(root, papers);

        section("4. starmap");
        starmap(root, papers);

        section("5. concepts.jsonl");
        exportConcepts(root, papers);

        section("6. papers.jsonl");
        exportPapers(root, papers);

        section("7. checkPapers");
        checkPapers(root);

        section("8. checkHf");
        if (HF_DATASET.isBlank()) System.out.println("尚未設定 HF 資料集，略過");
        else try {
            checkHf(HF_DATASET);
        } catch (Exception e) {
            System.out.println("（遠端檢查略過：" + e.getClass().getSimpleName() + " " + e.getMessage() + "）");
        }

        System.out.println("\n✓ 完成");
    }

    // ════════════════════════════════════════════════════════════
    // 0. author
    // ════════════════════════════════════════════════════════════

    /**
     * 為倉庫中每篇論文的中文論文與英文翻譯補上 YAML 頭部缺少的 author、contact。
     * 只補缺少的欄位，已有的值（即使與 AUTHOR、CONTACT 不同）一律不動；沒有 YAML 頭部的檔案不處理，由論-01 回報。
     * 寫入前後只能多出這兩行，否則中止。暫存規則同 label：已暫存者寫入後重新暫存，同時有已暫存與未暫存修改者跳過。
     */
    static void fillAuthor(Path root, Path src) throws IOException, InterruptedException {
        GitStatus gs = gitStatus(root);
        int changed = 0, skipped = 0;
        for (Path f : paperFiles(src, true)) {
            String rel = gs.rel(f);
            String xy = rel == null ? null : gs.status().get(rel);
            boolean staged = xy != null && xy.charAt(0) != ' ' && xy.charAt(0) != '?';
            String raw = Files.readString(f, StandardCharsets.UTF_8);
            String updated = withAuthor(raw);
            if (updated == null || updated.equals(raw)) continue;
            if (staged && xy.charAt(1) != ' ') {
                System.out.println("  [跳過] " + rel + "：缺少作者欄位，但同時有已暫存與未暫存的修改，請整理暫存後再執行");
                skipped++;
                continue;
            }
            List<String> a = new ArrayList<>(Arrays.asList(raw.split("\n", -1)));
            List<String> b = new ArrayList<>(Arrays.asList(updated.split("\n", -1)));
            b.removeIf(l -> l.replace("\r", "").startsWith("author: ") || l.replace("\r", "").startsWith("contact: "));
            a.removeIf(l -> l.replace("\r", "").startsWith("author: ") || l.replace("\r", "").startsWith("contact: "));
            if (!a.equals(b))
                throw new IllegalStateException("author 改動了作者欄位以外的內容，已中止，未寫檔：" + f);
            Files.writeString(f, updated, StandardCharsets.UTF_8);
            changed++;
            System.out.println("  [作者] " + (rel != null ? rel : f.toString()));
            if (staged) git(root, "add", "--", rel);
        }
        if (changed == 0 && skipped == 0) System.out.println("作者欄位無需補上");
    }

    /** 在 YAML 頭部補上缺少的 author、contact（插在 last-modified 之後；沒有時插在結尾的 --- 之前）。沒有 YAML 頭部時回傳 null */
    static String withAuthor(String text) {
        String[] lines = text.split("\n", -1);
        if (lines.length == 0 || !lines[0].replace("\r", "").trim().equals("---")) return null;
        int end = -1, lm = -1;
        boolean hasAuthor = false, hasContact = false;
        for (int i = 1; i < lines.length; i++) {
            String l = lines[i].replace("\r", "");
            if (l.trim().equals("---")) { end = i; break; }
            if (l.startsWith("last-modified:")) lm = i;
            if (l.startsWith("author:")) hasAuthor = true;
            if (l.startsWith("contact:")) hasContact = true;
        }
        if (end < 0) return null;
        if (hasAuthor && hasContact) return text;
        String eol = lines[0].endsWith("\r") ? "\r" : "";
        List<String> add = new ArrayList<>();
        if (!hasAuthor) add.add("author: " + AUTHOR + eol);
        if (!hasContact) add.add("contact: " + CONTACT + eol);
        List<String> out = new ArrayList<>(Arrays.asList(lines));
        out.addAll(lm >= 0 ? lm + 1 : end, add);
        return String.join("\n", out);
    }

    /** git status 的結果：倉庫根目錄（找不到時為 null）、相對路徑 → 狀態碼 XY */
    record GitStatus(Path repo, Map<String, String> status) {
        String rel(Path f) throws IOException {
            return repo == null ? null : repo.relativize(f.toRealPath()).toString().replace('\\', '/');
        }
    }

    static GitStatus gitStatus(Path root) throws IOException, InterruptedException {
        Map<String, String> status = new HashMap<>();
        String top = git(root, "rev-parse", "--show-toplevel");
        if (top == null) return new GitStatus(null, status);
        Path repo = Paths.get(top.trim()).toRealPath();
        String out = git(root, "-c", "core.quotepath=false", "status", "--porcelain=v1", "-z");
        if (out != null) {
            String[] ent = out.split("\0");
            for (int i = 0; i < ent.length; i++) {
                String e = ent[i];
                if (e.length() < 4) continue;
                status.put(e.substring(3), e.substring(0, 2));
                if (e.charAt(0) == 'R' || e.charAt(0) == 'C') i++;   // 下一段是更名前的路徑
            }
        }
        return new GitStatus(repo, status);
    }

    // ════════════════════════════════════════════════════════════
    // 0. label（工具規格 2.3）
    // ════════════════════════════════════════════════════════════

    static final String OPENERS = "「『（〔[", CLOSERS = "」』）〕]";
    static final String ENDERS = "。！？";
    /** 句末標點之後歸前一個單位的字元：右引號、右括號、強調標記的結尾 */
    static final String TRAILERS = "」』）〕”’\"'*_";
    static final Pattern HR_RE = Pattern.compile("^\\s*([-*_])(\\s*\\1){2,}\\s*$");
    static final Pattern HTML_ONLY_RE = Pattern.compile("^\\s*(<[^>]+>\\s*)+$");
    static final Pattern HEADING_RE = Pattern.compile("^#{2,6}\\s");

    /**
     * 為倉庫中每篇論文的中文論文寫入句碼標籤。只動標籤：寫入前後去掉標籤的內容必須逐字相同，否則中止。
     * 執行前已在暫存區者寫入後重新暫存；同時有已暫存與未暫存修改者跳過。
     */
    static void labelPapers(Path root, Path src) throws IOException, InterruptedException {
        GitStatus gs = gitStatus(root);
        int changed = 0;
        for (Path f : paperFiles(src, false)) {
            String rel = gs.rel(f);
            String xy = rel == null ? null : gs.status().get(rel);
            boolean staged = xy != null && xy.charAt(0) != ' ' && xy.charAt(0) != '?';
            if (staged && xy.charAt(1) != ' ') {
                System.out.println("  [跳過] " + rel + "：同時有已暫存與未暫存的修改，請整理暫存後再執行");
                continue;
            }
            String raw = Files.readString(f, StandardCharsets.UTF_8);
            Labeled lb = label(raw);
            if (lb.text().equals(raw)) continue;
            for (String s : lb.info()) System.out.println("  [資訊] " + f.getParent().getFileName() + "：" + s);
            if (!removeTags(lb.text()).equals(removeTags(raw)))
                throw new IllegalStateException("label 改動了句碼標籤以外的內容，已中止，未寫檔：" + f);
            Files.writeString(f, lb.text(), StandardCharsets.UTF_8);
            changed++;
            System.out.println("  [句碼] " + (rel != null ? rel : f.toString()) + "（" + lb.units().size() + " 個單位）");
            if (staged) git(root, "add", "--", rel);
        }
        if (changed == 0) System.out.println("句碼標籤無需更新");
    }

    /** 所有論文資料夾中的中文論文檔；withEn 為 true 時一併列出英文翻譯 */
    static List<Path> paperFiles(Path src, boolean withEn) throws IOException {
        List<Path> out = new ArrayList<>();
        try (var s = Files.list(src)) {
            for (Path d : s.filter(Files::isDirectory).sorted().toList()) {
                if (!d.getFileName().toString().startsWith("paper_")) continue;
                try (var m = Files.list(d)) {
                    for (Path f : m.sorted().toList()) {
                        String n = f.getFileName().toString();
                        if (!n.endsWith(".md") || n.equals("concepts.md") || n.equals("formalization.md")
                            || isGeneratedBundleName(n) || n.equals("README.md")) continue;
                        if (withEn || !isMostlyAscii(n.substring(0, n.length() - 3))) out.add(f);
                    }
                }
            }
        }
        return out;
    }

    static boolean isSentTagComment(String c) { return c.contains("句"); }

    /** 去掉一行中的句碼標籤（含格式錯誤的變體）；其他 HTML 註解保留 */
    static String removeInlineTags(String s) {
        var m = HTML_COMMENT.matcher(s);
        StringBuilder sb = new StringBuilder();
        while (m.find())
            m.appendReplacement(sb, isSentTagComment(m.group()) ? "" : Matcher.quoteReplacement(m.group()));
        m.appendTail(sb);
        return sb.toString();
    }

    /** 整行只由句碼標籤構成 */
    static boolean onlySentTags(String line) {
        String t = line.replace("\r", "").trim();
        if (t.isEmpty() || !HTML_COMMENT.matcher(t).find()) return false;
        String r = removeInlineTags(t);
        return r.isBlank() && !r.equals(t);
    }

    /** 去掉全部句碼標籤：行內者刪除，整行只有標籤者整行刪除（label 用來比對寫入前後） */
    static String removeTags(String text) {
        List<String> out = new ArrayList<>();
        for (String l : text.split("\n", -1)) if (!onlySentTags(l)) out.add(removeInlineTags(l));
        return String.join("\n", out);
    }

    /** 去掉全部句碼標籤，整行只有標籤者以空行取代，保留行號（validate 用） */
    static String blankTags(String text) {
        String[] lines = text.split("\n", -1);
        for (int i = 0; i < lines.length; i++)
            lines[i] = onlySentTags(lines[i]) ? (lines[i].endsWith("\r") ? "\r" : "") : removeInlineTags(lines[i]);
        return String.join("\n", lines);
    }

    /** 句碼標籤改印為可見的【句……】（輸入包用） */
    static String visibleTags(String text) {
        return SENT_TAG.matcher(text).replaceAll("【句$1】");
    }

    /** 連結只留顯示文字 */
    static String display(String s) {
        return MD_LINK_RE.matcher(s).replaceAll(mr -> Matcher.quoteReplacement(mr.group(1)));
    }

    /** 正規化：連結只留顯示文字，去掉全部空白 */
    static String norm(String s) {
        return display(s).replaceAll("[\\s\\u3000]+", "");
    }

    static boolean hasContent(String s) {
        return display(s).codePoints().anyMatch(Character::isLetterOrDigit);
    }

    /** 十位數字的句碼：SHA-256 前八個位元組，視為大端序無號整數，除以 10 的 10 次方取餘數 */
    static String hash10(String s) {
        try {
            byte[] d = java.security.MessageDigest.getInstance("SHA-256").digest(s.getBytes(StandardCharsets.UTF_8));
            long v = new java.math.BigInteger(1, Arrays.copyOf(d, 8))
                .mod(java.math.BigInteger.valueOf(10_000_000_000L)).longValue();
            return String.format("%010d", v);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    /** 取得句碼：內容相同者句碼相同；內容不同而相撞者依序接「＃2」「＃3」重算 */
    static String codeFor(String n, Map<String, String> seen, List<String> info, String where) {
        String k = hash10(n);
        String prev = seen.get(k);
        if (prev == null) { seen.put(k, n); return k; }
        if (prev.equals(n)) { info.add(where + "：內容與前文的單位相同，句碼相同 " + k); return k; }
        for (int i = 2; ; i++) {
            String n2 = n + "＃" + i, k2 = hash10(n2), p2 = seen.get(k2);
            if (p2 == null) { seen.put(k2, n2); info.add(where + "：句碼相撞，改用 " + k2); return k2; }
            if (p2.equals(n2)) return k2;
        }
    }

    /** 一行中的文句單位：{起點, 終點, 標籤插入位置} */
    static List<int[]> sentenceSpans(String c) {
        List<int[]> out = new ArrayList<>();
        int depth = 0, start = 0, n = c.length();
        for (int i = 0; i < n; i++) {
            char ch = c.charAt(i);
            if (OPENERS.indexOf(ch) >= 0) depth++;
            else if (CLOSERS.indexOf(ch) >= 0) depth = Math.max(0, depth - 1);
            else if (ENDERS.indexOf(ch) >= 0 && depth == 0) {
                int e = i + 1;
                while (e < n && TRAILERS.indexOf(c.charAt(e)) >= 0) e++;
                if (hasContent(c.substring(start, e))) { out.add(new int[]{start, e, e}); start = e; }
                i = e - 1;
            }
        }
        if (start < n && hasContent(c.substring(start))) {
            String t = c.stripTrailing();
            int at = t.length();
            if (t.trim().startsWith("|") && t.endsWith("|")) at = Math.max(start, t.lastIndexOf('|'));   // 表格列：放在最後一個 | 之前
            out.add(new int[]{start, n, at});
        }
        return out;
    }

    /** 依工具規格 2.3 切分單位、計算句碼、寫入標籤（先去掉全部舊標籤） */
    static Labeled label(String raw) {
        String[] lines = removeTags(raw).split("\n", -1);
        List<String> out = new ArrayList<>();
        List<Unit> units = new ArrayList<>();
        List<String> info = new ArrayList<>();
        Map<String, String> seen = new HashMap<>();
        boolean yaml = lines.length > 0 && lines[0].trim().equals("---");
        boolean inCode = false, inNote = false;
        StringBuilder code = new StringBuilder();
        int codeStart = -1;
        String heading = "篇首";
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            boolean cr = line.endsWith("\r");
            String c = cr ? line.substring(0, line.length() - 1) : line;
            String eol = cr ? "\r" : "";
            if (yaml) { out.add(line); if (i > 0 && c.trim().equals("---")) yaml = false; continue; }
            String st = c.stripTrailing();
            if (!inCode && st.equals(NOTE_OPEN)) { inNote = true; out.add(line); continue; }
            if (!inCode && inNote && st.equals(NOTE_CLOSE)) { inNote = false; out.add(line); continue; }
            if (inNote) { out.add(line); continue; }
            if (c.trim().startsWith("```")) {
                out.add(line);
                if (!inCode) { inCode = true; code.setLength(0); codeStart = i + 1; }
                else {
                    inCode = false;
                    String txt = code.toString();
                    String k = codeFor(norm(txt), seen, info, "第 " + codeStart + " 行的形式段");
                    units.add(new Unit(k, "形式段", txt.strip(), codeStart, heading));
                    out.add("<!--句" + k + "-->" + eol);
                }
                continue;
            }
            if (inCode) { code.append(c).append('\n'); out.add(line); continue; }
            if (c.isBlank() || c.startsWith("# ") || HR_RE.matcher(c).matches() || HTML_ONLY_RE.matcher(c).matches()) {
                out.add(line); continue;
            }
            if (HEADING_RE.matcher(c).find()) {
                String disp = display(c.replaceFirst("^#+\\s*", "")).strip();
                String k = codeFor(norm(disp), seen, info, "第 " + (i + 1) + " 行的標題");
                units.add(new Unit(k, "標題", disp, i + 1, disp));
                heading = disp;
                out.add(c + "<!--句" + k + "-->" + eol);
                continue;
            }
            StringBuilder nl = new StringBuilder();
            int prev = 0;
            for (int[] sp : sentenceSpans(c)) {
                String disp = display(c.substring(sp[0], sp[1])).strip();
                String k = codeFor(norm(disp), seen, info, "第 " + (i + 1) + " 行");
                units.add(new Unit(k, "文句", disp, i + 1, heading));
                nl.append(c, prev, sp[2]).append("<!--句").append(k).append("-->");
                prev = sp[2];
            }
            nl.append(c.substring(prev));
            out.add(nl + eol);
        }
        return new Labeled(String.join("\n", out), units, info);
    }

    /** 兩段文字第一個不同的行（報告用） */
    static String firstDiff(String a, String b) {
        String[] x = a.split("\n", -1), y = b.split("\n", -1);
        for (int i = 0; i < Math.min(x.length, y.length); i++)
            if (!x[i].equals(y[i])) return "（第 " + (i + 1) + " 行起）";
        return "（行數不同）";
    }

    // ════════════════════════════════════════════════════════════
    // 0. stamp
    // ════════════════════════════════════════════════════════════

    /** 手冊與說明文件開頭的「某某最後修改：時間戳」 */
    static final Pattern DOC_TS_RE = Pattern.compile("^(\\S{2,12})最後修改：(\\S+)$");

    /**
     * 只處理 Git 暫存區中的檔案：論文與概念表更新 YAML 頭部的 last-modified，
     * 手冊與說明文件更新開頭的「最後修改」。只改時間戳那一行，改完重新暫存。
     * 同時有已暫存與未暫存修改的檔案一律跳過，以免把刻意不提交的修改一併暫存。
     */
    static void stamp(Path root) throws IOException, InterruptedException {
        String top = git(root, "rev-parse", "--show-toplevel");
        if (top == null) { System.out.println("（略過：找不到 git，或專案不是 Git 倉庫）"); return; }
        Path repo = Paths.get(top.trim());
        String out = git(root, "-c", "core.quotepath=false", "status", "--porcelain=v1", "-z");
        if (out == null) return;

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        List<String> done = new ArrayList<>(), partial = new ArrayList<>();
        String[] ent = out.split("\0");
        for (int i = 0; i < ent.length; i++) {
            String e = ent[i];
            if (e.length() < 4) continue;
            char x = e.charAt(0), y = e.charAt(1);
            String path = e.substring(3);
            if (x == 'R' || x == 'C') i++;                    // 下一段是更名前的路徑
            if ("MARC".indexOf(x) < 0 || !path.endsWith(".md")) continue;
            if (isGeneratedBundlePath(path)) continue;   // 生成物不蓋時間戳
            if (y != ' ') { partial.add(path); continue; }
            Path f = repo.resolve(path);
            if (!Files.exists(f)) continue;
            String text = Files.readString(f, StandardCharsets.UTF_8);
            String updated = restamp(text, now);
            if (updated == null || updated.equals(text)) continue;
            Files.writeString(f, updated, StandardCharsets.UTF_8);
            git(root, "add", "--", path);
            done.add(path);
        }
        if (done.isEmpty() && partial.isEmpty()) System.out.println("暫存區中沒有需要更新時間戳的檔案");
        for (String d : done) System.out.println("  [時間戳] " + d + " → " + now);
        for (String d : partial)
            System.out.println("  [跳過] " + d + "：同時有已暫存與未暫存的修改，請整理暫存後再執行");
    }

    /** 只改開頭的時間戳那一行；沒有可改的時間戳時回傳 null */
    static String restamp(String text, String now) {
        String[] lines = text.split("\n", -1);
        if (lines.length > 0 && lines[0].trim().equals("---")) {
            for (int i = 1; i < lines.length; i++) {
                if (lines[i].trim().equals("---")) break;
                if (lines[i].startsWith("last-modified:")) {
                    lines[i] = "last-modified: " + now + (lines[i].endsWith("\r") ? "\r" : "");
                    return String.join("\n", lines);
                }
            }
            return null;
        }
        for (int i = 0; i < Math.min(lines.length, 10); i++) {
            String l = lines[i].replace("\r", "");
            var m = DOC_TS_RE.matcher(l);
            if (m.matches()) {
                lines[i] = m.group(1) + "最後修改：" + now + (lines[i].endsWith("\r") ? "\r" : "");
                return String.join("\n", lines);
            }
        }
        return null;
    }

    /** 執行 git 指令並回傳輸出；失敗時回傳 null */
    static String git(Path dir, String... args) throws InterruptedException {
        List<String> cmd = new ArrayList<>();
        cmd.add("git");
        cmd.addAll(Arrays.asList(args));
        try {
            Process p = new ProcessBuilder(cmd).directory(dir.toFile()).redirectErrorStream(false).start();
            String out = new String(p.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            p.getErrorStream().readAllBytes();
            return p.waitFor() == 0 ? out : null;
        } catch (IOException e) {
            return null;
        }
    }

    static void section(String name) {
        System.out.println("\n── " + name + " ───────────────────────────────");
    }

    static Path resolveRoot() {
        Path p = Paths.get("").toAbsolutePath();
        while (p != null) {
            if (Files.isDirectory(p.resolve(SRC_DIR))) return p;
            p = p.getParent();
        }
        return Paths.get("").toAbsolutePath();
    }

    static Map<String, Paper> byFolder(List<Paper> papers) {
        Map<String, Paper> m = new LinkedHashMap<>();
        for (Paper p : papers) m.put(p.folder, p);
        return m;
    }

    // ════════════════════════════════════════════════════════════
    // 掃描與解析
    // ════════════════════════════════════════════════════════════

    static List<Paper> scanRepo(Path src) throws IOException {
        List<Paper> result = new ArrayList<>();
        List<Path> dirs = new ArrayList<>();
        try (var s = Files.list(src)) {
            for (Path d : s.filter(Files::isDirectory).sorted().toList()) {
                String n = d.getFileName().toString();
                if (n.startsWith("paper_")) dirs.add(d);
                else if (n.equals("ext")) {
                    try (var e = Files.list(d)) {
                        for (Path ed : e.filter(Files::isDirectory).sorted().toList())
                            if (ed.getFileName().toString().startsWith("ext_")) dirs.add(ed);
                    }
                }
            }
        }
        for (Path d : dirs) result.add(readPaper(d));
        return result;
    }

    static Paper readPaper(Path dir) throws IOException {
        Paper p = new Paper();
        p.dir = dir;
        p.folder = dir.getFileName().toString();
        p.external = p.folder.startsWith("ext_");

        Path ct = dir.resolve("concepts.md");
        if (Files.exists(ct)) {
            p.conceptsFile = ct;
            p.conceptsText = Files.readString(ct, StandardCharsets.UTF_8);
            p.conceptsYaml = parseYaml(p.conceptsText);
            p.concepts = parseConceptTable(p.conceptsText);
            p.uuid = p.conceptsYaml.get("uuid");
        }

        Path fm = dir.resolve("formalization.md");
        if (Files.exists(fm)) {
            p.formalizationFile = fm;
            p.formText = Files.readString(fm, StandardCharsets.UTF_8);
            for (String line : p.formText.split("\r?\n")) {
                var m = F_TITLE_RE.matcher(line.trim());
                if (m.matches()) p.title = m.group(1);
            }
        }

        if (!p.external) {
            List<Path> mds;
            try (var s = Files.list(dir)) {
                mds = s.filter(f -> f.getFileName().toString().endsWith(".md"))
                       .filter(f -> {
                           String n = f.getFileName().toString();
                           return !n.equals("concepts.md") && !n.equals("formalization.md")
                               && !isGeneratedBundleName(n)
                               && !n.equals("README.md");
                       })
                       .sorted().collect(Collectors.toList());
            }
            for (Path md : mds) {
                // 注釋在一切檢查與輸出之前刪除（工具規格 3.8）；中文論文另外先去掉句碼標籤（3.10）
                String fileText = Files.readString(md, StandardCharsets.UTF_8);
                String name = md.getFileName().toString();
                String base = name.substring(0, name.length() - 3);
                boolean english = isMostlyAscii(base);
                Notes notes = stripNotes(english ? fileText : blankTags(fileText));
                String text = notes.blanked;
                if (english) {
                    p.enPaper = md; p.enText = text; p.titleEn = base;
                    p.enClean = notes.removed; p.enNoteProblems = notes.problems;
                    p.linksEn = scanLinks(text, name);
                } else {
                    p.zhPaper = md; p.zhText = text;
                    p.zhNoteProblems = notes.problems;
                    p.zhRaw = fileText;
                    p.zhClean = stripNotes(fileText).removed;      // 保留句碼標籤：資料集使用（工具規格第六節）
                    p.zhVisible = visibleTags(p.zhClean);           // 標籤改印為【句……】：輸入包使用
                    p.units = label(fileText).units();
                    if (p.title == null) p.title = base;
                    p.paperYaml = parseYaml(text);
                    if (p.uuid == null) p.uuid = p.paperYaml.get("uuid");
                    p.links = scanLinks(text, name);
                }
            }
            for (Link l : p.links)
                if (l.targetDir != null && !l.targetDir.startsWith("?")) p.refs.add(l.targetDir);
        }
        return p;
    }

    /** 新論文的標題佔位符（NewPaper 建立時使用） */
    static final String ZH_TITLE_PLACEHOLDER = "中文標題";
    static final String EN_TITLE_PLACEHOLDER = "English Title";

    /** YAML 頭部之後、程式碼區塊之外的第一個一級標題 */
    static String firstH1(String text) {
        String[] lines = stripCodeLines(text);
        boolean yaml = lines.length > 0 && lines[0].trim().equals("---");
        for (int i = 0; i < lines.length; i++) {
            if (yaml) { if (i > 0 && lines[i].trim().equals("---")) yaml = false; continue; }
            String l = lines[i].trim();
            if (l.startsWith("# ")) return l.substring(2).trim();
        }
        return null;
    }

    static String baseName(Path f) {
        String n = f.getFileName().toString();
        return n.endsWith(".md") ? n.substring(0, n.length() - 3) : n;
    }

    static boolean isMostlyAscii(String s) {
        int ascii = 0;
        for (char c : s.toCharArray()) if (c < 128) ascii++;
        return s.isEmpty() || ascii * 2 > s.length();
    }

    static Map<String, String> parseYaml(String text) {
        Map<String, String> map = new LinkedHashMap<>();
        String[] lines = text.split("\r?\n");
        if (lines.length == 0 || !lines[0].trim().equals("---")) return map;
        for (int i = 1; i < lines.length; i++) {
            String l = lines[i].trim();
            if (l.equals("---")) break;
            int c = l.indexOf(':');
            if (c > 0) map.put(l.substring(0, c).trim(), l.substring(c + 1).trim());
        }
        return map;
    }

    /** 解析概念表：錨點 + 標題（修飾子 中文名 / 英文名）+ 條列欄位 */
    static List<Concept> parseConceptTable(String text) {
        List<Concept> out = new ArrayList<>();
        String[] lines = text.split("\r?\n");
        for (int i = 0; i < lines.length; i++) {
            var a = ANCHOR_RE.matcher(lines[i].trim());
            if (!a.matches()) continue;
            String id = a.group(1);
            String mod = null, zh = null, en = null, def = null;
            boolean dep = false;
            StringBuilder raw = new StringBuilder(lines[i]).append('\n');

            int j = i + 1;
            if (j < lines.length) {
                var t = CT_TITLE_RE.matcher(lines[j].trim());
                if (t.matches()) { mod = t.group(1); zh = t.group(2); en = t.group(3).trim(); }
                raw.append(lines[j]).append('\n');
                j++;
            }
            for (; j < lines.length; j++) {
                String l = lines[j].trim();
                if (ANCHOR_RE.matcher(l).matches()) break;
                if (l.startsWith("## ")) break;
                raw.append(lines[j]).append('\n');
                if (l.startsWith("- 定義：") && def == null) def = l.substring("- 定義：".length()).trim();
                if (l.equals("- 狀態：已廢棄")) dep = true;
            }
            out.add(new Concept(id, mod, zh, en, def, dep, i + 1, raw.toString()));
            i = j - 1;
        }
        return out;
    }

    // ── 注釋（工具規格 3.8；論-17、譯-06） ──

    static final String NOTE_OPEN = "<note>", NOTE_CLOSE = "</note>";
    /** 刪除注釋後仍殘留的標籤，含大小寫、全形、自我封閉等寫錯的變體 */
    static final Pattern NOTE_RESIDUE = Pattern.compile("(?i)[<＜]\\s*/?\\s*note\\s*/?\\s*[>＞]");

    /** blanked：注釋以空行取代，保留行號；removed：注釋整段刪除，連同結尾後的一個空行；problems：格式問題 */
    record Notes(String blanked, String removed, List<String> problems) {}

    static Notes stripNotes(String text) {
        String[] orig = text.split("\r?\n", -1);
        String[] blanked = orig.clone();
        boolean[] drop = new boolean[orig.length];
        List<String> problems = new ArrayList<>();
        boolean inCode = false, inNote = false;
        int open = -1;
        for (int i = 0; i < orig.length; i++) {
            String t = orig[i].stripTrailing();
            boolean isOpen = t.equals(NOTE_OPEN), isClose = t.equals(NOTE_CLOSE);
            if (!inNote && t.trim().startsWith("```")) { inCode = !inCode; continue; }
            if (inCode) {
                if (isOpen || isClose)
                    problems.add("第 " + (i + 1) + " 行：注釋標籤不得在程式碼區塊內");
                else if (NOTE_RESIDUE.matcher(t).find())
                    problems.add("第 " + (i + 1) + " 行：疑似寫錯的注釋標籤：" + t.trim());
                continue;
            }
            if (isOpen || isClose) {
                if (isOpen && inNote) problems.add("第 " + (i + 1) + " 行：注釋不得巢狀（前一個開頭在第 " + (open + 1) + " 行）");
                if (isClose && !inNote) problems.add("第 " + (i + 1) + " 行：結尾標籤沒有對應的開頭");
                if (i > 0 && !orig[i - 1].isBlank())
                    problems.add("第 " + (i + 1) + " 行：" + t + " 的前一行須為空行");
                if (i + 1 < orig.length && !orig[i + 1].isBlank())
                    problems.add("第 " + (i + 1) + " 行：" + t + " 的後一行須為空行");
                if (isOpen && !inNote) { inNote = true; open = i; }
                else if (isClose && inNote) {
                    inNote = false;
                    for (int k = open; k <= i; k++) { blanked[k] = ""; drop[k] = true; }
                    if (i + 1 < orig.length && orig[i + 1].isBlank()) drop[i + 1] = true;
                }
                continue;
            }
            if (!inNote && NOTE_RESIDUE.matcher(t).find())
                problems.add("第 " + (i + 1) + " 行：疑似寫錯的注釋標籤：" + t.trim());
        }
        if (inNote) problems.add("第 " + (open + 1) + " 行：注釋沒有結尾標籤");
        StringBuilder removed = new StringBuilder();
        for (int i = 0; i < orig.length; i++) {
            if (drop[i]) continue;
            if (removed.length() > 0) removed.append('\n');
            removed.append(orig[i]);
        }
        return new Notes(String.join("\n", blanked), removed.toString(), problems);
    }

    /** 程式碼區塊內的行以空行取代，保留行號 */
    static String[] stripCodeLines(String text) {
        String[] lines = text.split("\r?\n", -1);
        boolean in = false;
        for (int i = 0; i < lines.length; i++) {
            boolean fence = lines[i].trim().startsWith("```");
            if (fence) { in = !in; lines[i] = ""; continue; }
            if (in) lines[i] = "";
        }
        return lines;
    }

    /** 掃描正文中的概念連結（YAML 頭部與程式碼區塊除外；標題行另行標記） */
    static List<Link> scanLinks(String text, String filename) {
        List<Link> out = new ArrayList<>();
        String[] lines = stripCodeLines(text);
        boolean inYaml = lines.length > 0 && lines[0].trim().equals("---");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (inYaml) {
                if (i > 0 && line.trim().equals("---")) inYaml = false;
                continue;
            }
            boolean heading = line.trim().startsWith("#");
            var m = MD_LINK_RE.matcher(line);
            while (m.find()) {
                String display = m.group(1), target = m.group(2).trim();
                String dir = null, id;
                var loc = LOCAL_TARGET_RE.matcher(target);
                var ext = EXT_TARGET_RE.matcher(target);
                var up  = UP_TARGET_RE.matcher(target);
                if (loc.matches())      { id = loc.group(1); }
                else if (ext.matches()) { dir = ext.group(1); id = ext.group(2); }
                else if (up.matches())  { dir = up.group(1);  id = up.group(2); }
                else { out.add(new Link(display, "?BAD?", target, i + 1, filename)); continue; }
                if (heading) { out.add(new Link(display, "?HEADING?", id, i + 1, filename)); continue; }
                out.add(new Link(display, dir, id, i + 1, filename));
            }
        }
        return out;
    }

    /** 去掉〔…〕後的拉丁與希臘字母 */
    static Set<String> foreignOutside(String text) {
        String t = text.replaceAll("〔[^〔〕]*〕", "");
        Set<String> out = new TreeSet<>();
        var m = FOREIGN.matcher(t);
        while (m.find()) out.add(m.group());
        return out;
    }

    /** 六角括號：非空、不巢狀、單一字母須緊貼中文字 */
    static List<String> bracketProblems(String text) {
        List<String> out = new ArrayList<>();
        int depth = 0;
        for (char c : text.toCharArray()) {
            if (c == '〔' && ++depth > 1) out.add("巢狀括號");
            if (c == '〕') depth--;
        }
        if (depth != 0) out.add("括號不成對");
        var m = Pattern.compile("〔([^〔〕]*)〕").matcher(text);
        while (m.find()) {
            String in = m.group(1);
            if (in.isBlank()) out.add("空括號");
            else if (in.matches("[A-Za-z\\u0370-\\u03FF]")) {
                char b = m.start() > 0 ? text.charAt(m.start() - 1) : ' ';
                char a = m.end() < text.length() ? text.charAt(m.end()) : ' ';
                if (!isCjk(b) && !isCjk(a)) out.add("單一字母 " + m.group() + " 未緊貼中文字");
            }
        }
        return out;
    }

    static boolean isCjk(char c) {
        return Character.UnicodeScript.of(c) == Character.UnicodeScript.HAN;
    }

    /** 形式化文件的二級段落：標題 → 內容 */
    static Map<String, String> sections(String text) {
        Map<String, String> m = new LinkedHashMap<>();
        String cur = null;
        StringBuilder sb = new StringBuilder();
        for (String l : text.split("\r?\n", -1)) {
            if (l.startsWith("## ")) {
                if (cur != null) m.put(cur, sb.toString());
                cur = l.substring(3).trim(); sb.setLength(0);
            } else if (cur != null) sb.append(l).append('\n');
        }
        if (cur != null) m.put(cur, sb.toString());
        return m;
    }

    static String stripCode(String text) {
        return String.join("\n", stripCodeLines(text));
    }

    // ════════════════════════════════════════════════════════════
    // 1. validate
    // ════════════════════════════════════════════════════════════

    static List<Issue> validate(Path root, List<Paper> papers, List<Issue> warn) throws IOException {
        List<Issue> is = new ArrayList<>();
        Map<String, Paper> byFolder = byFolder(papers);

        for (Paper p : papers) {
            String w = p.folder;

            // ── 概念表：檔案與頭部（概-01～概-04） ──
            if (p.conceptsFile == null) {
                is.add(new Issue("概-01", w, "缺少 concepts.md"));
                continue;
            }
            if (!p.conceptsYaml.containsKey("uuid") || !p.conceptsYaml.containsKey("last-modified"))
                is.add(new Issue("概-02", w, "concepts.md 缺少 YAML 頭部或必填欄位"));
            String cu = p.conceptsYaml.get("uuid");
            if (cu != null) {
                if (!UUID_RE.matcher(cu).matches())
                    is.add(new Issue("概-03", w, "uuid 非合法 UUID v4：" + cu));
                else if (!p.folder.equals((p.external ? "ext_" : "paper_") + cu.replace('-', '_')))
                    is.add(new Issue("概-03", w, "uuid 與資料夾名稱不符：" + cu));
            }
            String clm = p.conceptsYaml.get("last-modified");
            if (clm != null && !TS_RE.matcher(clm).matches())
                is.add(new Issue("概-04", w, "concepts.md last-modified 格式錯誤：" + clm));

            // ── 概念表：條目（概-05～概-26、存-03） ──
            Set<String> ids = new HashSet<>(), zhNames = new HashSet<>(), enNames = new HashSet<>();
            for (Concept c : p.concepts) {
                String at = w + " #" + c.id;
                if (!ID_RE.matcher(c.id).matches())
                    is.add(new Issue("概-17", at, "ID 格式應為 c 加兩位以上數字"));
                if (!ids.add(c.id))
                    is.add(new Issue("概-18", at, "ID 重複"));
                if (c.zh == null) {
                    String[] rl = c.raw.split("\n");
                    if (rl.length > 1 && rl[1].trim().startsWith("## "))
                        is.add(new Issue("概-06", at, "標題應為「修飾子 中文名 / English Name」"));
                    else
                        is.add(new Issue("概-05", at, "錨點之後應緊接一個二級標題"));
                    continue;
                }
                if (!"public".equals(c.mod) && !"private".equals(c.mod))
                    is.add(new Issue("概-11", at, "修飾子只允許 public 或 private：" + c.mod));
                if (p.external && !"public".equals(c.mod))
                    is.add(new Issue("存-03", at, "外部存根的條目一律為 public"));
                if (!zhNames.add(c.zh)) is.add(new Issue("概-25", at, "中文名重複：" + c.zh));
                if (!enNames.add(c.en)) is.add(new Issue("概-26", at, "英文名重複：" + c.en));
                Set<String> f = foreignOutside(c.zh);
                List<String> bp = bracketProblems(c.zh);
                if (!f.isEmpty() || !bp.isEmpty())
                    is.add(new Issue("概-22", at, "中文名外文問題：" + c.zh + " " + f + bp));
                if (c.def == null || c.def.isBlank())
                    is.add(new Issue("概-07", at, "缺少「- 定義：」或內容為空"));
                if (!Pattern.compile("(?m)^- Definition: \\S").matcher(c.raw).find())
                    is.add(new Issue("概-07", at, "缺少「- Definition: 」或內容為空"));
                for (String bad : CONTEXT_WORDS)
                    if (c.def != null && c.def.contains(bad))
                        is.add(new Issue("概-14", at, "定義含依賴論文語境的指涉：" + bad));
                for (String l : c.raw.split("\n"))
                    if (l.startsWith("- 狀態：") && !l.equals("- 狀態：已廢棄"))
                        is.add(new Issue("概-09", at, "狀態值只允許「已廢棄」：" + l));
                int iYuan = c.raw.indexOf("- 原文："), iSta = c.raw.indexOf("- 狀態："),
                    iCai = c.raw.indexOf("- 裁決：");
                if ((iYuan >= 0 && iSta >= 0 && iYuan > iSta) || (iSta >= 0 && iCai >= 0 && iSta > iCai)
                    || (iYuan >= 0 && iCai >= 0 && iYuan > iCai))
                    is.add(new Issue("概-10", at, "可選欄位順序應為 原文 → 狀態 → 裁決"));
            }
            // 概-15（警告）：定義中字面上等於其他本篇概念名、卻未加連結的片段
            List<String> names = p.concepts.stream().map(Concept::zh).filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(String::length).reversed()).toList();
            for (Concept c : p.concepts) {
                if (c.def == null || c.zh == null) continue;
                String t = MD_LINK_RE.matcher(c.def).replaceAll("").replace(c.zh, "■");
                for (String n : names) {
                    if (n.equals(c.zh) || !t.contains(n)) continue;
                    warn.add(new Issue("概-15", w + " #" + c.id, "定義中未連結、字面上等於概念名：" + n));
                    t = t.replace(n, "■");
                }
            }
            // 概-12（工具部分）、概-34：定義中的連結
            checkDefinitionLinks(is, p, byFolder);
            // 概-08：條目之間不得有其他二級標題
            checkTailHeadings(is, p);

            // ── 概念表：待決項與不登記（概-29、概-30、概-33） ──
            if (!p.external) {
                if (!p.conceptsText.contains("\n## 待決項"))
                    is.add(new Issue("概-29", w, "concepts.md 缺少「待決項」區塊"));
                else if (!pendingEmpty(p.conceptsText))
                    is.add(new Issue("概-30", w, "概念層待決項未清零"));
                int iNo = p.conceptsText.indexOf("\n## 不登記"), iPen = p.conceptsText.indexOf("\n## 待決項");
                if (iNo < 0 || (iPen >= 0 && iNo > iPen))
                    is.add(new Issue("概-33", w, "「不登記」區塊不存在，或不在「待決項」之前"));
                else {
                    String blk = p.conceptsText.substring(iNo + "\n## 不登記".length(), iPen < 0 ? p.conceptsText.length() : iPen).trim();
                    for (String l : blk.split("\n"))
                        if (!l.isBlank() && !l.equals("無") && !l.matches("- \\S+：\\d{4}-\\d{2}-\\d{2} .+"))
                            is.add(new Issue("概-33", w, "不登記項目格式應為「- 名稱：日期 理由」：" + l));
                }
            }

            // ── 外部存根（存-01、存-02） ──
            if (p.external) {
                for (String f : List.of("來源：", "詮釋者：", "詮釋日期："))
                    if (!p.conceptsText.contains(f))
                        is.add(new Issue("存-02", w, "外部存根缺少「" + f + "」"));
                try (var s = Files.list(p.dir)) {
                    List<String> others = s.map(x -> x.getFileName().toString())
                        .filter(n -> !n.equals("concepts.md")).toList();
                    if (!others.isEmpty()) is.add(new Issue("存-01", w, "外部存根只能有 concepts.md：" + others));
                }
                continue;
            }

            // ── 中文論文：頭部與標題（論-01～論-05、跨-27） ──
            if (p.zhPaper == null) {
                is.add(new Issue("論-01", w, "找不到中文論文 .md"));
                continue;
            }
            if (!p.paperYaml.containsKey("uuid") || !p.paperYaml.containsKey("last-modified"))
                is.add(new Issue("論-01", w, "論文缺少 YAML 頭部或必填欄位"));
            for (String k : List.of("author", "contact"))
                if (!p.paperYaml.isEmpty() && isBlank(p.paperYaml.get(k)))
                    is.add(new Issue("論-01", w, "論文 YAML 頭部缺少 " + k + " 或內容為空"
                        + "（重新執行 ToolRunner 即由 author 步驟自動補上；若因暫存狀態跳過，請先整理暫存）"));
            String pu = p.paperYaml.get("uuid");
            if (pu != null) {
                if (!UUID_RE.matcher(pu).matches())
                    is.add(new Issue("論-02", w, "論文 uuid 非合法 UUID v4：" + pu));
                else if (!p.folder.equals("paper_" + pu.replace('-', '_')))
                    is.add(new Issue("論-02", w, "論文 uuid 與資料夾名稱不符"));
                if (cu != null && !cu.equals(pu))
                    is.add(new Issue("跨-27", w, "論文 uuid 與 concepts.md uuid 不一致"));
            }
            String plm = p.paperYaml.get("last-modified");
            if (plm != null && !TS_RE.matcher(plm).matches())
                is.add(new Issue("論-03", w, "論文 last-modified 格式錯誤：" + plm));

            String zhBase = baseName(p.zhPaper);
            if (zhBase.equals(ZH_TITLE_PLACEHOLDER))
                is.add(new Issue("論-04", w, "中文論文的檔名仍是佔位符「" + ZH_TITLE_PLACEHOLDER + "」"));
            String h1 = firstH1(p.zhText);
            if (!zhBase.equals(h1))
                is.add(new Issue("論-05", w, "中文論文的一級標題「" + (h1 == null ? "（沒有）" : h1)
                    + "」與中文檔名「" + zhBase + "」不一致"));
            // 論-17：注釋格式
            for (String np : p.zhNoteProblems) is.add(new Issue("論-17", w, np));
            // 論-20：句碼標籤與依現行內容算出的結果一致（工具規格 3.10）
            String relabeled = label(p.zhRaw).text();
            if (!relabeled.equals(p.zhRaw))
                is.add(new Issue("論-20", w, "句碼標籤與現行內容不一致" + firstDiff(p.zhRaw, relabeled)
                    + "；重新執行 ToolRunner 即由 label 自動改寫；若 label 因檔案同時有已暫存與未暫存的修改而跳過，請先整理暫存"));

            // ── 英文翻譯：檔名與標題（譯-01、譯-02）。尚未開始翻譯時，譯組與跨-19 暫不檢查 ──
            boolean en = p.enStarted();
            if (en) {
                String enBase = baseName(p.enPaper), eh1 = firstH1(p.enText);
                if (enBase.equals(EN_TITLE_PLACEHOLDER))
                    is.add(new Issue("譯-01", w, "英文翻譯的檔名仍是佔位符「" + EN_TITLE_PLACEHOLDER + "」"));
                if (!enBase.equals(eh1))
                    is.add(new Issue("譯-02", w, "英文翻譯的一級標題「" + (eh1 == null ? "（沒有）" : eh1)
                        + "」與英文檔名「" + enBase + "」不一致"));
                // 譯-06：注釋格式
                for (String np : p.enNoteProblems) is.add(new Issue("譯-06", w, np));
            }

            // ── 正文連結（論-07～論-13、譯-03～譯-05、跨-22） ──
            checkLinks(is, p, p.links, byFolder, ZH_CODES);
            if (en) checkLinks(is, p, p.linksEn, byFolder, EN_CODES);

            // 跨-19：中英文版使用的概念 ID 集合一致
            if (en) {
                Set<String> a = p.links.stream().filter(ToolRunner::valid).filter(l -> ID_RE.matcher(l.id).matches())
                    .map(ToolRunner::key).collect(Collectors.toSet());
                Set<String> b = p.linksEn.stream().filter(ToolRunner::valid).filter(l -> ID_RE.matcher(l.id).matches())
                    .map(ToolRunner::key).collect(Collectors.toSet());
                if (!a.equals(b)) {
                    Set<String> only = new TreeSet<>(a); only.removeAll(b);
                    Set<String> only2 = new TreeSet<>(b); only2.removeAll(a);
                    is.add(new Issue("跨-19", w, "中英文版連結目標不一致"
                        + (only.isEmpty() ? "" : "，僅中文有：" + only)
                        + (only2.isEmpty() ? "" : "，僅英文有：" + only2)));
                }
            }

            // 跨-18：中英文逐段對照，對應段落的連結目標一致；跨-17（工具部分，警告）：英文顯示文字
            if (en) {
                Alignment al = align(p);
                checkAlignment(is, p, w, al);
                checkEnDisplay(warn, p, byFolder, w, al);
            }

            // 跨-02：每個未廢棄條目在程式碼區塊外至少被連結一次
            Set<String> used = p.links.stream()
                .filter(l -> l.targetDir == null).map(l -> l.id).collect(Collectors.toSet());
            for (Concept c : p.concepts)
                if (!c.deprecated && !used.contains(c.id))
                    is.add(new Issue("跨-02", w + " #" + c.id, "概念未在正文的程式碼區塊外被連結：" + c.zh));
            // 跨-01：正文連結的本篇 ID 必須在概念表中
            for (String id : used)
                if (!id.equals("new") && ID_RE.matcher(id).matches() && p.byId(id) == null)
                    is.add(new Issue("跨-01", w + " #" + id, "正文連結到概念表中不存在的 ID"));

            // 論-06（警告）：正文中字面上等於概念名、卻未加連結的片段
            StringBuilder plain = new StringBuilder();
            String[] zl = stripCodeLines(p.zhText);
            boolean yaml = zl.length > 0 && zl[0].trim().equals("---");
            for (int i = 0; i < zl.length; i++) {
                if (yaml) { if (i > 0 && zl[i].trim().equals("---")) yaml = false; continue; }
                if (zl[i].trim().startsWith("#")) continue;
                plain.append(MD_LINK_RE.matcher(zl[i]).replaceAll("")).append('\n');
            }
            String t = plain.toString();
            for (String n : names)
                if (t.contains(n)) {
                    warn.add(new Issue("論-06", w, "正文中未連結、字面上等於概念名：" + n + "（請確認是否在指涉該概念）"));
                    t = t.replace(n, "■");
                }

            // 論-14、論-15：外文限制（中文論文）
            String body = p.zhText.replaceFirst("(?s)^---\n.*?\n---\n", "")
                .replaceAll("\\]\\([^)]*\\)", "]").replaceAll("(?m)^```[^\\n]*$", "```");
            Set<String> fo = foreignOutside(body);
            if (!fo.isEmpty()) is.add(new Issue("論-14", w, "去除例外後出現外文：" + fo));
            for (String b : bracketProblems(body)) is.add(new Issue("論-15", w, b));

            // ── 形式化文件（形-01～形-38、跨-23～跨-25、跨-28～跨-30） ──
            if (p.formalizationFile == null)
                is.add(new Issue("形-01", w, "缺少 formalization.md"));
            else
                checkFormalization(is, warn, p, byFolder, zhBase);
        }

        // ── 循環引用（跨-20） ──
        for (Paper p : papers) {
            if (p.external) continue;
            List<String> cycle = findCycle(p.folder, byFolder);
            if (cycle != null)
                is.add(new Issue("跨-20", p.folder, "循環引用：" + String.join(" → ", cycle)));
        }

        // ── 版本比對（概-13、概-19、概-20、概-21、跨-21；工具規格 3.9） ──
        checkHistory(root, papers, byFolder, is);
        return is;
    }

    // ── 版本比對（工具規格 3.9） ──

    /** 版本比對略過的原因；null 表示已比對 */
    static String historySkipped = null;

    /**
     * commit 即定稿：最後一次 commit（HEAD）中的概念表就是定稿版。
     * 拿工作中的概念表與它比對：舊 ID 消失（概-19，涵蓋概-21）、public 改為 private（概-13）、
     * 已廢棄的條目恢復使用（概-20 的工具部分）。被引用的上游必須已在 HEAD 中（跨-21）。
     * 新論文不在 HEAD 中，自動略過。只讀不寫。
     */
    // ── 跨-18、跨-17：中英文逐段對照（工具規格 3.2） ─────────────────

    /** 一個段落：起訖行號（1 起算）與標題層級（正文為 0） */
    record Block(int start, int end, int level) {}

    /** 中英文分段的結果；aligned 為 false 時，problem 說明第一個對不上的位置 */
    record Alignment(List<Block> zh, List<Block> en, boolean aligned, String problem) {}

    static final Pattern LIST_ITEM_RE = Pattern.compile("^\\s*(?:[-*+]|\\d+\\.)\\s");

    /**
     * 正文分段：去掉 YAML 頭部；注釋、句碼標籤與程式碼區塊已以空行取代。
     * 空行與分隔線 --- 結束一段；每個標題、每個清單項目各自成一段。
     */
    static List<Block> textBlocks(String text) {
        String[] lines = stripCodeLines(text);
        List<Block> out = new ArrayList<>();
        int i = 0;
        if (lines.length > 0 && lines[0].trim().equals("---")) {
            i = 1;
            while (i < lines.length && !lines[i].trim().equals("---")) i++;
            i++;
        }
        int start = -1;
        for (; i <= lines.length; i++) {
            String t = i < lines.length ? lines[i].trim() : "";
            boolean blank = t.isEmpty() || t.equals("---");
            boolean heading = t.startsWith("#");
            boolean item = !blank && LIST_ITEM_RE.matcher(lines[i]).find();
            if (start >= 0 && (blank || heading || item)) { out.add(new Block(start + 1, i, 0)); start = -1; }
            if (blank) continue;
            if (heading) {
                int lv = 0;
                while (lv < t.length() && t.charAt(lv) == '#') lv++;
                out.add(new Block(i + 1, i + 1, lv));
                continue;
            }
            if (start < 0) start = i;
        }
        return out;
    }

    static String blockKind(Block b) { return b.level() == 0 ? "正文" : " " + b.level() + " 級標題"; }

    static Alignment align(Paper p) {
        List<Block> zh = textBlocks(p.zhText), en = textBlocks(p.enText);
        int n = Math.min(zh.size(), en.size());
        for (int i = 0; i < n; i++)
            if (zh.get(i).level() != en.get(i).level())
                return new Alignment(zh, en, false, "第 " + (i + 1) + " 段起對不上：中文 L" + zh.get(i).start()
                    + " 是" + blockKind(zh.get(i)) + "，英文 L" + en.get(i).start() + " 是" + blockKind(en.get(i)));
        if (zh.size() != en.size()) {
            String where = zh.size() > en.size() ? "中文 L" + zh.get(n).start() : "英文 L" + en.get(n).start();
            return new Alignment(zh, en, false, "前 " + n + " 段對得上，之後只有一方還有段落（從 " + where + " 起）");
        }
        return new Alignment(zh, en, true, null);
    }

    /** 行號所在的段落序號；不在任何段落中時為 -1 */
    static int blockOf(List<Block> blocks, int line) {
        int lo = 0, hi = blocks.size() - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            Block b = blocks.get(mid);
            if (line < b.start()) hi = mid - 1;
            else if (line > b.end()) lo = mid + 1;
            else return mid;
        }
        return -1;
    }

    static boolean idLink(Link l) { return valid(l) && ID_RE.matcher(l.id).matches(); }

    /** 每段的連結目標集合（目錄 + ID） */
    static Map<Integer, Set<String>> linkSets(List<Link> links, List<Block> blocks) {
        Map<Integer, Set<String>> m = new HashMap<>();
        for (Link l : links) {
            if (!idLink(l)) continue;
            int b = blockOf(blocks, l.line);
            if (b >= 0) m.computeIfAbsent(b, k -> new TreeSet<>()).add(key(l));
        }
        return m;
    }

    /** 跨-18：段落結構一致，且每段的連結目標集合相同 */
    static void checkAlignment(List<Issue> is, Paper p, String w, Alignment al) {
        String en = p.enPaper.getFileName().toString();
        Map<Integer, Set<String>> zs = linkSets(p.links, al.zh()), es = linkSets(p.linksEn, al.en());
        if (!al.aligned()) {
            // 提示：從頭比對，第一個連結目標不同的段落，通常就是多出或缺少段落的地方
            String hint = "";
            for (int i = 0; i < Math.min(al.zh().size(), al.en().size()); i++)
                if (!zs.getOrDefault(i, Set.of()).equals(es.getOrDefault(i, Set.of()))) {
                    hint = "；第一個連結目標不同的段落在中文 L" + al.zh().get(i).start() + "、英文 L" + al.en().get(i).start()
                        + "，多出或缺少的段落可能在此附近";
                    break;
                }
            is.add(new Issue("跨-18", w, "中英文段落結構不一致（中文 " + al.zh().size() + " 段、英文 "
                + al.en().size() + " 段），無法逐段對照；" + al.problem() + hint));
            return;
        }
        for (int i = 0; i < al.zh().size(); i++) {
            Set<String> a = zs.getOrDefault(i, Set.of()), b = es.getOrDefault(i, Set.of());
            if (a.equals(b)) continue;
            Set<String> only = new TreeSet<>(a); only.removeAll(b);
            Set<String> only2 = new TreeSet<>(b); only2.removeAll(a);
            is.add(new Issue("跨-18", w + " L" + al.zh().get(i).start() + " ↔ " + en + " L" + al.en().get(i).start(),
                "對應段落的連結目標不一致"
                + (only.isEmpty() ? "" : "，僅中文有：" + only)
                + (only2.isEmpty() ? "" : "，僅英文有：" + only2)));
        }
    }

    static List<String> enWords(String s) {
        List<String> out = new ArrayList<>();
        for (String x : s.toLowerCase(Locale.ROOT).split("[\\s\\-]+")) if (!x.isEmpty()) out.add(x);
        return out;
    }

    static String plainForm(String w) {
        if (w.endsWith("ves") && w.length() > 4) return w.substring(0, w.length() - 3) + "f";
        for (String suf : new String[]{"ies", "es", "s"})
            if (w.endsWith(suf) && w.length() - suf.length() >= 2) return w.substring(0, w.length() - suf.length());
        return w;
    }

    /**
     * 跨-17 容許的詞形變化：相同；去掉複數詞尾後相同（selves 對 self）；
     * 或共同字首至少 5 個字母，或至少 4 個且達較短者的六成（distinguished 對 distinction、arrived 對 arrival）
     */
    static boolean sameWord(String a, String b) {
        if (a.equals(b)) return true;
        String pa = plainForm(a), pb = plainForm(b);
        if (pa.equals(pb) || pa.equals(b) || a.equals(pb)) return true;
        int k = 0, n = Math.min(a.length(), b.length());
        while (k < n && a.charAt(k) == b.charAt(k)) k++;
        return k >= 5 || (k >= 4 && k >= 0.6 * n);
    }

    /** 顯示文字中依序包含英文名的每個詞（前後可有限定詞） */
    static boolean containsName(List<String> d, List<String> n) {
        for (int i = 0; i + n.size() <= d.size(); i++) {
            boolean ok = true;
            for (int j = 0; j < n.size() && ok; j++) ok = sameWord(d.get(i + j), n.get(j));
            if (ok) return true;
        }
        return false;
    }

    /** 顯示文字恰為英文名中連續的幾個詞（不是全部）：簡寫 */
    static boolean isAbbreviation(List<String> d, List<String> n) {
        if (d.isEmpty() || d.size() >= n.size()) return false;
        for (int i = 0; i + d.size() <= n.size(); i++) {
            boolean ok = true;
            for (int j = 0; j < d.size() && ok; j++) ok = sameWord(d.get(j), n.get(i + j));
            if (ok) return true;
        }
        return false;
    }

    static Concept conceptOf(Link l, Paper p, Map<String, Paper> byFolder) {
        if (l.targetDir == null) return p.byId(l.id);
        Paper up = byFolder.get(l.targetDir);
        return up == null ? null : up.byId(l.id);
    }

    /**
     * 跨-17（工具部分，警告）：英文顯示文字須為概念表英文名，容許大小寫、連字號、詞形變化與限定詞。
     * 中文對應段落以簡寫顯示同一概念時（顯示文字不是概念表中文名），英文可用英文名中連續的幾個詞作為簡寫。
     * 其餘不符者依「顯示文字 → 英文名」合併列出，由稽核判斷。
     */
    static void checkEnDisplay(List<Issue> warn, Paper p, Map<String, Paper> byFolder, String w, Alignment al) {
        Map<Integer, Set<String>> zhShort = new HashMap<>();
        if (al.aligned())
            for (Link l : p.links) {
                if (!idLink(l)) continue;
                Concept c = conceptOf(l, p, byFolder);
                int b = blockOf(al.zh(), l.line);
                if (c != null && b >= 0 && !l.display.equals(c.zh()))
                    zhShort.computeIfAbsent(b, k -> new HashSet<>()).add(key(l));
            }
        Map<String, int[]> bad = new LinkedHashMap<>();   // 顯示文字 → 英文名：{次數, 第一處行號}
        for (Link l : p.linksEn) {
            if (!idLink(l)) continue;
            Concept c = conceptOf(l, p, byFolder);
            if (c == null || c.en() == null) continue;      // 目標不存在由譯-03、跨-22 回報
            List<String> d = enWords(l.display), n = enWords(c.en());
            if (containsName(d, n)) continue;
            int b = al.aligned() ? blockOf(al.en(), l.line) : -1;
            if (b >= 0 && isAbbreviation(d, n) && zhShort.getOrDefault(b, Set.of()).contains(key(l))) continue;
            int[] v = bad.computeIfAbsent("「" + l.display + "」→「" + c.en() + "」", k -> new int[]{0, l.line});
            v[0]++;
        }
        String en = p.enPaper.getFileName().toString();
        for (var e : bad.entrySet())
            warn.add(new Issue("跨-17", w + "/" + en + " L" + e.getValue()[1],
                "顯示文字與概念表英文名不在容許的變化內：" + e.getKey() + "（共 " + e.getValue()[0] + " 處）"));
    }

    static void checkHistory(Path root, List<Paper> papers, Map<String, Paper> byFolder, List<Issue> is) {
        historySkipped = null;
        Path repo;
        try {
            String top = git(root, "rev-parse", "--show-toplevel");
            if (top == null) { historySkipped = "找不到 git，或專案不是 Git 倉庫"; return; }
            if (git(root, "rev-parse", "--verify", "-q", "HEAD") == null) {
                historySkipped = "倉庫還沒有任何 commit"; return;
            }
            repo = Paths.get(top.trim()).toRealPath();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            historySkipped = "git 執行被中斷"; return;
        } catch (IOException e) {
            historySkipped = "無法解析倉庫路徑：" + e.getMessage(); return;
        }

        // 取出 HEAD 中的概念表；不在 HEAD 中者（新論文、新存根）不放進 committed
        Map<String, List<Concept>> committed = new HashMap<>();
        for (Paper p : papers) {
            if (p.conceptsFile == null) continue;
            try {
                String rel = repo.relativize(p.conceptsFile.toRealPath()).toString().replace('\\', '/');
                String old = git(root, "show", "HEAD:" + rel);
                if (old != null) committed.put(p.folder, parseConceptTable(old));
            } catch (IOException e) {
                // 路徑無法解析時略過此篇
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                historySkipped = "git 執行被中斷"; return;
            }
        }

        for (Paper p : papers) {
            List<Concept> old = committed.get(p.folder);
            if (old == null) continue;
            for (Concept o : old) {
                String at = p.folder + " #" + o.id;
                Concept c = p.byId(o.id);
                if (c == null) {
                    is.add(new Issue("概-19", at, "最後一次 commit 中的 ID 已消失；定稿後 ID 不得修改、條目不得刪除"
                        + (o.zh == null ? "" : "（原為「" + o.zh + "」）")));
                    continue;
                }
                if ("public".equals(o.mod) && "private".equals(c.mod))
                    is.add(new Issue("概-13", at, "定稿的 public 條目改為 private：" + c.zh));
                if (o.deprecated && !c.deprecated)
                    is.add(new Issue("概-20", at, "已廢棄的條目恢復使用：" + c.zh));
            }
        }

        // 跨-21：被引用的論文或存根必須已 commit（已定稿）
        for (Paper p : papers) {
            if (p.external) continue;
            for (String r : p.refs) {
                if (byFolder.get(r) == null) continue;      // 找不到資料夾者由跨-22 回報
                if (!committed.containsKey(r))
                    is.add(new Issue("跨-21", p.folder, "引用了尚未 commit（未定稿）的論文或存根：" + r));
            }
        }
    }

    /** 概念表定義中不得出現的固定字眼（概-14 的工具部分） */
    static final List<String> CONTEXT_WORDS = List.of("如上", "前述", "上述", "本文第");

    /** 連結檢查的代碼：中文論文與英文翻譯分屬不同條目 */
    record LinkCodes(String which, String format, String uuid, String anchor, String display,
                     String heading, String placeholderUp, String placeholderLeft) {}

    static final LinkCodes ZH_CODES = new LinkCodes("中文", "論-07", "論-08", "論-09", "論-10", "論-11", "論-12", "論-13");
    static final LinkCodes EN_CODES = new LinkCodes("英文", "譯-03", "譯-03", "譯-03", "譯-05", "譯-04", "譯-03", "譯-03");

    /** 概-12（工具部分）、概-34：定義中的連結有效；public 定義只連 public */
    static void checkDefinitionLinks(List<Issue> is, Paper p, Map<String, Paper> byFolder) {
        for (Concept c : p.concepts) {
            if (c.deprecated || c.zh == null) continue;
            String at = p.folder + " #" + c.id;
            Set<String> seen = new HashSet<>();          // 同一條目連到同一目標只報一次
            for (String line : c.raw.split("\n")) {
                String t = line.trim();
                if (!t.startsWith("- 定義：") && !t.startsWith("- Definition:")) continue;
                var m = MD_LINK_RE.matcher(t);
                while (m.find()) {
                    String target = m.group(2).trim();
                    if (!seen.add(target)) continue;
                    var loc = LOCAL_TARGET_RE.matcher(target);
                    var ext = EXT_TARGET_RE.matcher(target);
                    var up  = UP_TARGET_RE.matcher(target);
                    String dir = null, id;
                    if (loc.matches())      id = loc.group(1);
                    else if (ext.matches()) { dir = ext.group(1); id = ext.group(2); }
                    else if (up.matches())  { dir = up.group(1);  id = up.group(2); }
                    else { is.add(new Issue("概-34", at, "定義中的連結格式不合法：" + target)); continue; }
                    Paper tp = dir == null ? p : byFolder.get(dir);
                    Concept tc = tp == null ? null : tp.byId(id);
                    if (tc == null)
                        is.add(new Issue("概-34", at, "定義連到不存在的條目：" + target));
                    else if (tc.deprecated)
                        is.add(new Issue("概-34", at, "定義連到已廢棄的條目：" + target));
                    else if (dir != null && !"public".equals(tc.mod))
                        is.add(new Issue("概-34", at, "定義連到他篇的非 public 條目：" + target));
                    else if (dir == null && "public".equals(c.mod) && !"public".equals(tc.mod))
                        is.add(new Issue("概-12", at, "public 條目的定義連到 private 條目：" + tc.zh + "（" + id + "）"));
                }
            }
        }
    }

    static void checkTailHeadings(List<Issue> is, Paper p) {
        String[] lines = p.conceptsText.split("\r?\n");
        boolean tail = false;
        for (int i = 0; i < lines.length; i++) {
            String l = lines[i].trim();
            if (l.startsWith("## ") && !(i > 0 && ANCHOR_RE.matcher(lines[i - 1].trim()).matches())) tail = true;
            else if (tail && ANCHOR_RE.matcher(l).matches())
                is.add(new Issue("概-08", p.folder + " L" + (i + 1), "條目出現在非條目的二級標題之後"));
        }
    }

    static boolean valid(Link l) {
        return l.targetDir == null || !l.targetDir.startsWith("?");
    }

    static String key(Link l) {
        return (l.targetDir == null ? "" : l.targetDir + "/") + l.id;
    }

    static boolean pendingEmpty(String text) {
        int i = text.indexOf("## 待決項");
        if (i < 0) return true;
        String tail = text.substring(i + "## 待決項".length()).trim();
        int nx = tail.indexOf("\n## ");
        if (nx >= 0) tail = tail.substring(0, nx).trim();
        return tail.equals("無") || tail.isEmpty();
    }

    static void checkLinks(List<Issue> is, Paper p, List<Link> links,
                           Map<String, Paper> byFolder, LinkCodes k) {
        Map<String, String> displayToId = new HashMap<>();
        for (Link l : links) {
            String at = p.folder + " " + k.which() + " L" + l.line;
            if ("?BAD?".equals(l.targetDir)) {
                is.add(new Issue(k.format(), at, "連結格式不合法：" + l.id));
                continue;
            }
            if ("?HEADING?".equals(l.targetDir)) {
                is.add(new Issue(k.heading(), at, "標題內不得標注連結"));
                continue;
            }
            if (l.id.equals("new")) {
                if (l.targetDir != null) is.add(new Issue(k.placeholderUp(), at, "佔位符只能用於本篇概念"));
                else is.add(new Issue(k.placeholderLeft(), at, "正文中殘留佔位符 #new（" + l.display + "）"));
                continue;
            }
            if (!ID_RE.matcher(l.id).matches()) {
                is.add(new Issue(k.anchor(), at, "錨點必須是 ID，不得使用概念名：" + l.id));
                continue;
            }
            if (l.targetDir != null) {
                String u = l.targetDir.substring(l.targetDir.indexOf('_') + 1).replace('_', '-');
                if (!UUID_RE.matcher(u).matches())
                    is.add(new Issue(k.uuid(), at, "連結中的 UUID 不合法：" + l.targetDir));
            }
            Paper target = l.targetDir == null ? p : byFolder.get(l.targetDir);
            if (target == null) {
                is.add(new Issue("跨-22", at, "找不到上游資料夾：" + l.targetDir));
                continue;
            }
            Concept c = target.byId(l.id);
            if (l.targetDir != null) {
                if (c == null)
                    is.add(new Issue("跨-22", at, "上游概念不存在：" + l.targetDir + " #" + l.id));
                else if (c.deprecated)
                    is.add(new Issue("跨-22", at, "引用了已廢棄的概念：" + l.targetDir + " #" + l.id));
                else if (!"public".equals(c.mod))
                    is.add(new Issue("跨-22", at, "引用了非 public 的概念：" + l.targetDir + " #" + l.id));
            }
            // 本篇 ID 不存在於概念表者，由跨-01 回報

            String key = key(l);
            String prev = displayToId.putIfAbsent(l.display, key);
            if (prev != null && !prev.equals(key))
                is.add(new Issue(k.display(), at, "顯示文字「" + l.display + "」同時指向 " + prev + " 與 " + key));
        }
    }

    static List<String> findCycle(String start, Map<String, Paper> byFolder) {
        return dfs(start, start, byFolder, new ArrayDeque<>(), new HashSet<>(), true);
    }

    static List<String> dfs(String cur, String target, Map<String, Paper> byFolder,
                            Deque<String> path, Set<String> seen, boolean first) {
        if (!first && cur.equals(target)) {
            List<String> r = new ArrayList<>(path);
            r.add(target);
            return r;
        }
        if (!seen.add(cur)) return null;
        Paper p = byFolder.get(cur);
        if (p == null || p.external) return null;
        path.addLast(cur);
        for (String r : p.refs) {
            List<String> found = dfs(r, target, byFolder, path, seen, false);
            if (found != null) return found;
        }
        path.removeLast();
        return null;
    }

    /** 形式化文件頭之後的內容 */
    static String afterHeader(String[] lines) {
        return String.join("\n", Arrays.copyOfRange(lines, Math.min(F_HEADER_LINES, lines.length), lines.length));
    }

    static void checkFormalization(List<Issue> is, List<Issue> warn, Paper p, Map<String, Paper> byFolder, String zhBase) {
        String w = p.folder + "/formalization.md";
        String[] lines = p.formText.split("\r?\n", -1);
        String[] prose = stripCodeLines(p.formText);

        // 形-02：文件頭固定為前兩行；跨-28、跨-29：與論文一致
        String title = null, uuid = null;
        if (lines.length >= F_HEADER_LINES) {
            var m1 = F_TITLE_RE.matcher(lines[0].trim()); if (m1.matches()) title = m1.group(1);
            var m2 = F_UUID_RE.matcher(lines[1].trim());  if (m2.matches()) uuid = m2.group(1);
        }
        if (title == null || uuid == null)
            is.add(new Issue("形-02", w, "文件頭應為前兩行：論文：《……》 / UUID：……"));
        if (uuid != null && p.uuid != null && !uuid.equals(p.uuid))
            is.add(new Issue("跨-28", w, "文件頭的 UUID 與論文不一致"));
        if (title != null && !title.equals(zhBase))
            is.add(new Issue("跨-29", w, "文件頭的標題《" + title + "》與中文檔名「" + zhBase + "」不一致"));

        // 形-03：記號約定；形-04：每個段落有來源標註
        Map<String, String> secs = sections(afterHeader(lines));
        for (var e : secs.entrySet())
            if (!e.getKey().equals("待決項") && !e.getValue().stripLeading().startsWith("來源："))
                is.add(new Issue("形-04", w, "段落缺少來源標註：" + e.getKey()));
        if (!secs.containsKey("記號約定"))
            is.add(new Issue("形-03", w, "缺少「記號約定」段落"));
        else if (!secs.keySet().iterator().next().equals("記號約定"))
            is.add(new Issue("形-03", w, "「記號約定」段落應位於文件頭之後、其他段落之前"));
        if (secs.containsKey("記號約定")) {
            // 跨-09：「記號約定」的來源固定寫「論文全文」（其餘段落的來源由形-38、跨-30 檢查）
            String first = secs.get("記號約定").stripLeading().split("\n", 2)[0].trim();
            if (first.startsWith("來源：") && !first.equals("來源：論文全文"))
                is.add(new Issue("跨-09", w, "「記號約定」的來源應為「來源：論文全文」：" + first));
        }

        String proseText = afterHeader(prose);
        Set<String> zhNames = p.concepts.stream().map(Concept::zh).filter(Objects::nonNull)
            .collect(Collectors.toSet());

        // 形-06、跨-23、跨-24、跨-25：上游概念
        var m = PREFIXED_REF.matcher(proseText);
        while (m.find()) {
            String u = m.group(2), name = m.group(3);
            if (u.equals(p.uuid)) {
                is.add(new Issue("形-06", w, "本篇概念禁止加 UUID 前綴：" + name));
                continue;
            }
            String folder = (m.group(1) != null ? "ext_" : "paper_") + u.replace('-', '_');
            Paper up = byFolder.get(folder);
            Concept uc = up == null ? null : up.byZh(name);
            if (uc == null) {
                is.add(new Issue("跨-23", w, "上游概念不存在：" + u + "." + name));
                continue;
            }
            if (!"public".equals(uc.mod))
                is.add(new Issue("跨-24", w, "上游概念不是 public：" + u + "." + name));
            boolean cited = p.links.stream().anyMatch(l -> folder.equals(l.targetDir) && uc.id.equals(l.id));
            if (!cited)
                is.add(new Issue("跨-25", w, "形式化引用了論文正文未引用的上游概念：" + u + "." + name));
        }

        // 形-08：切詞邊界
        String decl = secs.getOrDefault("記號約定", "");
        Set<String> declared = new HashSet<>(Arrays.asList(stripCode(decl).split(SPLITTER)));
        List<String> ordered = zhNames.stream()
            .sorted(Comparator.comparingInt(String::length).reversed()).toList();
        Set<String> badTok = new TreeSet<>();
        for (String tok : PREFIXED_REF.matcher(CITE_ANY.matcher(proseText).replaceAll(" ")).replaceAll(" ").split(SPLITTER)) {
            if (tok.isEmpty() || zhNames.contains(tok) || declared.contains(tok)) continue;
            for (String n : ordered)
                if (tok.contains(n)) { badTok.add("「" + tok + "」含「" + n + "」"); break; }
        }
        for (String b : badTok) is.add(new Issue("形-08", w, "概念名未以空格等分隔，或非概念用語未宣告：" + b));

        // 形-09：程式碼區塊外不得有關係符號
        for (int i = F_HEADER_LINES; i < prose.length; i++)
            for (char c : REL_SYMBOLS.toCharArray())
                if (prose[i].indexOf(c) >= 0)
                    is.add(new Issue("形-09", w + " L" + (i + 1), "程式碼區塊外出現關係符號 " + c));

        // 形-13、形-14：外文限制
        String ft = afterHeader(lines)
            .replaceAll("(?m)^```[^\\n]*$", "```")
            .replaceAll("(ext:)?[0-9a-f]{8}-[0-9a-f-]{27}", "");
        Set<String> fo = foreignOutside(ft);
        if (!fo.isEmpty()) is.add(new Issue("形-13", w, "去除例外後出現外文：" + fo));
        for (String b : bracketProblems(ft)) is.add(new Issue("形-14", w, b));

        // 形-16（工具部分）：蘊含必須標註推導來源
        for (int i = F_HEADER_LINES; i < prose.length; i++)
            if (prose[i].contains("蘊含(") && !DERIVE_RE.matcher(prose[i]).find())
                is.add(new Issue("形-16", w + " L" + (i + 1), "蘊含未標註推導來源（從 甲 + 乙 推出）"));

        // 形-22：前提除限定條件外，皆須在推導中被引用
        String pre = stripCode(secs.getOrDefault("前置條件", ""));
        List<String> prem = new ArrayList<>();
        Set<String> limited = new HashSet<>();
        Pattern pl = Pattern.compile("前提([" + NUM + "]+)");
        for (String l : pre.split("\n")) {
            String head = l.contains("：") ? l.substring(0, l.indexOf('：')) : "";
            var pm = pl.matcher(head);
            while (pm.find()) {
                if (!prem.contains(pm.group(1))) prem.add(pm.group(1));
                if (head.contains("[限定條件]")) limited.add(pm.group(1));
            }
        }
        Set<String> cited = new HashSet<>();
        var dm = DERIVE_RE.matcher(proseText);
        List<String> sources = new ArrayList<>();
        while (dm.find()) {
            sources.add(dm.group(1));
            var pm = pl.matcher(dm.group(1));
            while (pm.find()) cited.add(pm.group(1));
        }
        for (String x : prem)
            if (!limited.contains(x) && !cited.contains(x))
                is.add(new Issue("形-22", w, "前提" + x + " 未在任何推導中被引用，也未標記為限定條件"));

        // 形-39：前提與限定條件中不得出現「公理」（鐵律的地位寫在它的定義中，不列為前提）
        for (String l : pre.split("\n")) {
            String b = CITE_ANY.matcher(bodyOf(l)).replaceAll("").trim();
            if (b.startsWith("前提") && b.contains("公理"))
                is.add(new Issue("形-39", w, "前提中出現「公理」：鐵律不列為前提，推導直接引用該律；限定另寫一條：" + abbrev(b)));
        }

        // 形-29、形-30、形-33（工具部分）：待決項
        List<String> keys = new ArrayList<>(secs.keySet());
        if (keys.isEmpty() || !keys.get(keys.size() - 1).equals("待決項"))
            is.add(new Issue("形-29", w, "文件末尾缺少「待決項」區塊"));
        else if (!pendingEmpty(p.formText))
            is.add(new Issue("形-30", w, "論證層待決項未清零，不得進入稽核"));
        for (String s : sources)
            if (s.contains("待決項")) is.add(new Issue("形-33", w, "待決項不得作為推導來源：" + s));

        // 形-36、形-37、形-38、跨-30：推導標籤、來源、正文引用（工具規格 3.5）
        Set<String> unitCodes = new HashSet<>(), headCodes = new HashSet<>();
        for (Unit u : p.units) { unitCodes.add(u.code()); if (u.kind().equals("標題")) headCodes.add(u.code()); }
        Set<String> labels = new HashSet<>();
        Map<String, String> blocks = statementBlocks(lines);   // 形-35 警告：前提與推導的原文（含其後的程式碼區塊）
        String curSec = null;
        for (int i = F_HEADER_LINES; i < prose.length; i++) {
            String raw = prose[i].trim();
            if (raw.startsWith("## ")) { curSec = raw.substring(3).trim(); continue; }
            if (raw.isEmpty() || curSec == null || curSec.equals("記號約定") || curSec.equals("待決項")) continue;
            String at = w + " L" + (i + 1);
            String body = bodyOf(raw);
            List<String> cites = new ArrayList<>();
            var cm = CITE_RE.matcher(body);
            boolean hasCite = cm.find();
            if (hasCite) {
                for (String c : cm.group(1).split("、")) {
                    c = c.trim();
                    if (!c.matches("句\\d{10}")) { is.add(new Issue("形-38", at, "正文引用格式錯誤：" + c)); continue; }
                    cites.add(c.substring(1));
                }
                for (String c : cites)
                    if (!unitCodes.contains(c))
                        is.add(new Issue("跨-30", at, "引用的句碼不在中文論文現行版本中：句" + c + "（正文可能已修改，請重新對照）"));
                body = body.substring(0, cm.start()).trim();
            } else if (CITE_ANY.matcher(body).find())
                is.add(new Issue("形-38", at, "正文引用須放在行尾"));
            if (body.startsWith("來源：")) {
                if (!hasCite) is.add(new Issue("形-38", at, "「來源：」行缺少正文引用"));
                else if (cites.stream().noneMatch(headCodes::contains))
                    is.add(new Issue("形-38", at, "「來源：」行引用的句碼中沒有標題單位"));
                continue;
            }
            boolean premise = curSec.equals("前置條件") && body.startsWith("前提");
            boolean example = body.startsWith("例");
            var dmm = DERIVE_RE.matcher(body);
            boolean hasSrc = dmm.find();
            boolean deriv = hasSrc && !body.startsWith("附註：") && !body.startsWith("[作者裁決]") && !example;
            if ((premise || example || deriv) && !hasCite)
                is.add(new Issue("形-38", at, "缺少正文引用【正文：句……】：" + abbrev(body)));
            String label = null;
            if (deriv) {
                var lm = LABEL_RE.matcher(body);
                if (!lm.find()) is.add(new Issue("形-36", at, "推導缺少標籤（步驟、結論、循環加中文數字）：" + abbrev(body)));
                else {
                    label = lm.group(1);
                    if (!labels.add(label)) is.add(new Issue("形-36", at, "標籤重複：" + label));
                }
            }
            if (!hasSrc || !(deriv || example)) continue;
            for (String tok : dmm.group(1).split("\\s*\\+\\s*")) {
                String why = sourceProblem(tok.trim(), prem, labels, label, example, p, byFolder);
                // 引用了未列於「前置條件」的前提，即形-23 的內容，以形-23 回報；其餘來源問題為形-37
                if (why != null) is.add(new Issue(why.equals(PREMISE_UNLISTED) ? "形-23" : "形-37", at, why + "：" + tok.trim()));
            }
            // 形-35（工具部分，警告）：陳述中出現、但所列來源的原文中都沒有出現的概念
            if (deriv && label != null) {
                String stmt = body.substring(label.length() + 1, dmm.start()).trim();
                String hay = sourceText(dmm.group(1), blocks, p, byFolder);
                List<String> missing = uncoveredConcepts(stmt, hay, ordered, p);
                List<String> quant = new ArrayList<>();
                for (String q : QUANTIFIERS) if (stmt.contains(q) && !hay.contains(q)) quant.add(q);
                if (!missing.isEmpty() || !quant.isEmpty()) {
                    List<String> parts = new ArrayList<>();
                    if (!missing.isEmpty())
                        parts.add("陳述用到「" + String.join("」「", missing) + "」，所列來源的原文中都沒有出現");
                    if (!quant.isEmpty())
                        parts.add("陳述有量詞「" + String.join("」「", quant) + "」，所列來源的原文中沒有");
                    parts.add(ruledBelow(prose, i) ? "已有作者裁決（形-35 明文例外）" : "無作者裁決，須逐條判斷");
                    warn.add(new Issue("形-35", at, label + "：" + String.join("；", parts)));
                }
            }
        }
    }

    /** 形-35 警告用：前提編號與推導標籤 → 該行原文（去掉正文引用）加上緊接其後的程式碼區塊內容（工具規格 3.5） */
    static Map<String, String> statementBlocks(String[] lines) {
        Map<String, String> out = new HashMap<>();
        Pattern pp = Pattern.compile("^(前提[" + NUM + "]+)");
        String cur = null, sec = null;
        StringBuilder sb = new StringBuilder();
        boolean inCode = false;
        for (int i = F_HEADER_LINES; i < lines.length; i++) {
            String l = lines[i];
            String t = l.trim();
            if (t.startsWith("```")) { inCode = !inCode; continue; }
            if (inCode) { if (cur != null) sb.append('\n').append(l); continue; }
            if (t.startsWith("## ") || t.startsWith("- ")) {
                if (cur != null) out.putIfAbsent(cur, sb.toString());
                cur = null; sb.setLength(0);
                if (t.startsWith("## ")) { sec = t.substring(3).trim(); continue; }
                String body = CITE_ANY.matcher(bodyOf(t)).replaceAll("").trim();
                var pm = pp.matcher(body);
                var lm = LABEL_RE.matcher(body);
                if ("前置條件".equals(sec) && pm.find()) cur = pm.group(1);
                else if (lm.find()) cur = lm.group(1);
                if (cur != null) sb.append(body);
            }
        }
        if (cur != null) out.putIfAbsent(cur, sb.toString());
        return out;
    }

    /**
     * 形-35 警告用：陳述（以切詞邊界切分，片段恰為概念名者）中的本篇概念與上游概念識別，
     * 若在所有來源展開後的原文中都沒有出現，即列出。來源展開：前提與標籤取原文（含其後的程式碼區塊）；
     * 「概念名 的定義」與概念本身取概念名與定義全文。本篇概念以字面比對：來源原文中出現包含它的較長概念名（例如以「信空間」涵蓋「信」），也算出現
     */
    static List<String> uncoveredConcepts(String stmt, String hay, List<String> ordered, Paper p) {
        Set<String> present = new HashSet<>();
        var um = PREFIXED_REF.matcher(hay);
        while (um.find()) present.add(um.group());
        String rest = um.replaceAll("■");
        for (String n : ordered)
            if (rest.contains(n)) present.add(n);
        List<String> missing = new ArrayList<>();
        var sm = PREFIXED_REF.matcher(stmt);
        while (sm.find()) if (!present.contains(sm.group()) && !missing.contains(sm.group())) missing.add(sm.group());
        for (String tok : sm.replaceAll(" ").split(SPLITTER))
            if (!tok.isEmpty() && p.byZh(tok) != null && !present.contains(tok) && !missing.contains(tok)) missing.add(tok);
        return missing;
    }

    /** 形-35 警告用：量詞。陳述含其中之一、所列來源展開後的原文中卻沒有時，列入警告。清單為程式常數 */
    static final List<String> QUANTIFIERS = List.of("每個", "每一", "所有", "任何", "任一", "全部", "恰", "一一", "唯一");

    /** 形-35 警告用：本行之後（略過空行，程式碼區塊已以空行取代）的第一個非空行以「[作者裁決]」開頭 */
    static boolean ruledBelow(String[] prose, int i) {
        for (int j = i + 1; j < prose.length; j++) {
            String b = bodyOf(prose[j]);
            if (b.isEmpty()) continue;
            return b.startsWith("[作者裁決]");
        }
        return false;
    }

    /** 形-35 警告用：所列來源逐項展開後的原文 */
    static String sourceText(String src, Map<String, String> blocks, Paper p, Map<String, Paper> byFolder) {
        StringBuilder text = new StringBuilder();
        for (String tok : src.split("\\s*\\+\\s*")) {
            String t = tok.trim();
            if (blocks.containsKey(t)) { text.append(blocks.get(t)).append('\n'); continue; }
            String name = t.endsWith(" 的定義") ? t.substring(0, t.length() - " 的定義".length()).trim() : t;
            Concept c = conceptByName(name, p, byFolder);
            if (c != null) text.append(name).append('\n').append(c.def() == null ? "" : display(c.def())).append('\n');
        }
        return text.toString();
    }

    static String bodyOf(String line) {
        String t = line.trim();
        return t.startsWith("- ") ? t.substring(2).trim() : t;
    }

    static String abbrev(String s) {
        return s.length() <= 40 ? s : s.substring(0, 40) + "……";
    }

    /** 形-37：來源的一項是否屬於五種之一且找得到對象；合格時回傳 null */
    /** 形-23：推導引用的前提不在「前置條件」中 */
    static final String PREMISE_UNLISTED = "前提不在「前置條件」中";

    static String sourceProblem(String t, List<String> prem, Set<String> labels, String current,
                                boolean example, Paper p, Map<String, Paper> byFolder) {
        if (t.isEmpty()) return "來源為空";
        var pm = Pattern.compile("^前提([" + NUM + "]+)$").matcher(t);
        if (pm.matches()) return prem.contains(pm.group(1)) ? null : PREMISE_UNLISTED;
        if (LABEL_TOKEN.matcher(t).matches())
            return labels.contains(t) && !t.equals(current) ? null : "標籤不在前文中";
        if (t.equals("本例效準")) return example ? null : "「本例效準」只能用於例";
        boolean def = t.endsWith(" 的定義");
        String name = def ? t.substring(0, t.length() - " 的定義".length()).trim() : t;
        if (conceptByName(name, p, byFolder) != null) return null;
        return def ? "概念不存在" : "不屬於五種來源之一";
    }

    /** 以本篇概念名或上游概念識別找概念 */
    static Concept conceptByName(String name, Paper p, Map<String, Paper> byFolder) {
        Concept c = p.byZh(name);
        if (c != null) return c;
        var m = PREFIXED_REF.matcher(name);
        if (!m.matches()) return null;
        Paper up = byFolder.get((m.group(1) != null ? "ext_" : "paper_") + m.group(2).replace('-', '_'));
        return up == null ? null : up.byZh(m.group(3));
    }

    // ════════════════════════════════════════════════════════════
    // 報告（依稽核總表的「檢查者」欄）
    // ════════════════════════════════════════════════════════════

    /** 稽核總表的一列：| 編號 | 約束 | 檢查者 | 舊編號 | */
    static final Pattern AUDIT_ROW_RE =
        Pattern.compile("^\\|\\s*((?:論|譯|概|存|形|跨)-\\d{2})\\s*\\|[^|]*\\|([^|]*)\\|");
    static final List<String> PREFIXES = List.of("論", "譯", "概", "存", "形", "跨");

    /** 讀取稽核總表：編號 → 檢查者。找不到檔案時回傳空表 */
    static Map<String, String> loadAuditTable(Path root) throws IOException {
        Map<String, String> m = new LinkedHashMap<>();
        Path f = doc(root, AUDIT_TABLE);
        if (!Files.exists(f)) return m;
        for (String l : Files.readAllLines(f, StandardCharsets.UTF_8)) {
            var r = AUDIT_ROW_RE.matcher(l.trim());
            if (r.find()) m.put(r.group(1), r.group(2).trim());
        }
        return m;
    }

    static final Pattern AUDIT_CODE_RE = Pattern.compile("(?:論|譯|概|存|形|跨)-\\d{2}");

    /** 從稽核總表 0.3 的「| 稽核包 | AI 條目 |」表讀取全部分工；包名同時用於輸出檔名。 */
    static Map<String, LinkedHashSet<String>> loadAuditPackages(Path root) throws IOException {
        Map<String, LinkedHashSet<String>> out = new LinkedHashMap<>();
        Path f = doc(root, AUDIT_TABLE);
        if (!Files.exists(f)) return out;
        boolean inTable = false;
        for (String raw : Files.readAllLines(f, StandardCharsets.UTF_8)) {
            String line = raw.trim();
            if (line.equals("| 稽核包 | AI 條目 |")) { inTable = true; continue; }
            if (!inTable) continue;
            if (line.matches("^\\|[- :]+\\|[- :]+\\|$")) continue;
            if (!line.startsWith("|")) break;
            String[] col = line.split("\\|", -1);
            if (col.length < 4) continue;
            String pack = col[1].trim();
            if (pack.isBlank()) continue;
            LinkedHashSet<String> codes = out.computeIfAbsent(pack, k -> new LinkedHashSet<>());
            var m = AUDIT_CODE_RE.matcher(col[2]);
            while (m.find()) codes.add(m.group());
        }
        return out;
    }

    static Set<String> aiAuditCodes(Map<String, String> table) {
        Set<String> out = new LinkedHashSet<>();
        for (var e : table.entrySet())
            if (e.getValue().startsWith("AI") || e.getValue().startsWith("工具＋AI")) out.add(e.getKey());
        return out;
    }

    /** 分包表必須把全部 AI／工具＋AI 條目恰好分完；各包非空、不得重複或混入非 AI 條目。 */
    static List<String> auditPackageProblems(Map<String, String> table,
                                             Map<String, LinkedHashSet<String>> packs) {
        List<String> out = new ArrayList<>();
        Set<String> expected = aiAuditCodes(table);
        if (packs.isEmpty()) {
            out.add("分包表為空");
            return out;
        }

        Map<String, List<String>> owners = new LinkedHashMap<>();
        Set<String> union = new LinkedHashSet<>();
        for (var e : packs.entrySet()) {
            String pack = e.getKey();
            Set<String> codes = e.getValue();
            if (pack.contains("/") || pack.contains("\\") || pack.contains(".."))
                out.add("分包名不可作為檔名：" + pack);
            if (codes.isEmpty()) out.add(pack + "包為空");
            for (String code : codes) {
                union.add(code);
                owners.computeIfAbsent(code, k -> new ArrayList<>()).add(pack);
            }
        }

        List<String> duplicated = new ArrayList<>();
        for (var e : owners.entrySet())
            if (e.getValue().size() > 1)
                duplicated.add(e.getKey() + "（" + String.join("、", e.getValue()) + "）");
        if (!duplicated.isEmpty()) out.add("重複分包：" + String.join("、", duplicated));

        Set<String> missing = new LinkedHashSet<>(expected); missing.removeAll(union);
        if (!missing.isEmpty()) out.add("未分包：" + String.join("、", missing));
        Set<String> extra = new LinkedHashSet<>(union); extra.removeAll(expected);
        if (!extra.isEmpty()) out.add("分包表含非 AI 條目：" + String.join("、", extra));
        return out;
    }

    /** 只有中英逐處對應類條目需要把英文全文帶入該包。 */
    static boolean auditPackageNeedsEnglish(Set<String> codes) {
        return codes.contains("跨-16") || codes.contains("跨-17")
            || codes.stream().anyMatch(c -> c.startsWith("譯-"));
    }

    /** 未被引用正文只供形式化完整性／粒度檢查；形-35 本身不需要。 */
    static boolean auditPackageNeedsUncited(Set<String> codes) {
        return codes.contains("跨-10") || codes.contains("跨-11") || codes.contains("跨-13");
    }

    static String auditPackageScope(Map<String, String> table, Set<String> codes, String pack) {
        StringBuilder sb = new StringBuilder("本包為「" + pack + "」包，只稽核下列 AI 條目；不要輸出其他條目：\n");
        for (String pf : PREFIXES) {
            List<String> ai = new ArrayList<>();
            for (var e : table.entrySet()) {
                if (!codes.contains(e.getKey()) || !e.getKey().startsWith(pf + "-")) continue;
                ai.add(e.getKey() + (e.getValue().startsWith("工具＋AI") ? "*" : ""));
            }
            if (!ai.isEmpty()) sb.append("  ").append(pf).append("　").append(String.join("、", ai)).append('\n');
        }
        sb.append("標 * 者為「工具＋AI」，只檢查工具未涵蓋的語意部分。\n");
        return sb.toString();
    }

    static void report(Path root, List<Issue> issues, List<Issue> warnings, List<Paper> papers) throws IOException {
        if (issues.isEmpty()) System.out.println("✓ 全部通過（" + papers.size() + " 個資料夾）");
        else {
            System.out.println("✗ " + issues.size() + " 項不通過\n");
            print(issues);
        }
        if (!warnings.isEmpty()) {
            System.out.println("\n△ " + warnings.size() + " 項警告（不阻擋後續步驟，請人工確認）\n");
            print(warnings);
        }

        List<String> enPending = papers.stream()
            .filter(p -> !p.external && p.zhPaper != null && !p.enStarted()).map(p -> p.folder).toList();
        if (!enPending.isEmpty())
            System.out.println("\n（英文翻譯尚未開始，譯組與跨-19 暫不檢查：" + String.join("、", enPending) + "）");
        if (historySkipped != null)
            System.out.println("\n△ 版本比對略過（" + historySkipped + "）：概-13、概-19、概-20、概-21、跨-21 本次未檢查");

        Map<String, String> table = loadAuditTable(root);
        if (table.isEmpty()) {
            System.out.println("\n（找不到 " + DOCS_DIR + "/" + AUDIT_TABLE + "，無法列出 AI 稽核範圍）");
            return;
        }
        // 同步檢查：工具回報的代碼必須在總表中，且檢查者含「工具」
        Set<String> bad = new TreeSet<>();
        for (Issue i : concat(issues, warnings)) {
            String c = table.get(i.code);
            if (c == null) bad.add(i.code + "（總表中沒有此編號）");
            else if (!c.startsWith("工具")) bad.add(i.code + "（總表的檢查者為「" + c + "」）");
        }
        if (!bad.isEmpty())
            System.out.println("\n△ 工具與稽核總表不一致，請修正其中一方：" + String.join("、", bad));
        System.out.print("\n" + auditScope(table));
    }

    /** 依稽核總表列出 AI 稽核範圍與作者把關的條目 */
    static String auditScope(Map<String, String> table) {
        StringBuilder sb = new StringBuilder(
            "本輪 AI 稽核範圍（依稽核總表；標 * 者為「工具＋AI」，只含工具未涵蓋的部分）：\n");
        for (String pf : PREFIXES) {
            List<String> ai = new ArrayList<>();
            for (var e : table.entrySet())
                if (e.getKey().startsWith(pf + "-")) {
                    if (e.getValue().startsWith("AI")) ai.add(e.getKey());
                    else if (e.getValue().startsWith("工具＋AI")) ai.add(e.getKey() + "*");
                }
            if (!ai.isEmpty()) sb.append("  ").append(pf).append("　").append(String.join("、", ai)).append('\n');
        }
        List<String> author = new ArrayList<>();
        for (var e : table.entrySet()) {
            if (e.getValue().startsWith("作者")) author.add(e.getKey());
            else if (e.getValue().startsWith("工具＋作者")) author.add(e.getKey() + "*");
        }
        if (!author.isEmpty())
            sb.append("作者把關範圍（標 * 者為「工具＋作者」，工具已查機械的部分）：")
              .append(String.join("、", author)).append('\n');
        return sb.toString();
    }

    static List<Issue> concat(List<Issue> a, List<Issue> b) {
        List<Issue> out = new ArrayList<>(a);
        out.addAll(b);
        return out;
    }

    static void print(List<Issue> list) {
        list.stream().collect(Collectors.groupingBy(Issue::code, TreeMap::new, Collectors.toList()))
            .forEach((code, l) -> {
                System.out.println("[" + code + "] " + l.size() + " 項");
                for (Issue i : l) System.out.println("    " + i.where + "：" + i.detail);
            });
    }

    // ════════════════════════════════════════════════════════════
    // 關係抽取與分類（工具規格第四節）
    // ════════════════════════════════════════════════════════════

    static List<Rel> extractRelations(Paper p, Map<String, Paper> byFolder) {
        List<Rel> out = new ArrayList<>();
        if (p.formText.isBlank()) return out;
        String[] prose = stripCodeLines(p.formText);
        String sec = null;
        for (int i = F_HEADER_LINES; i < prose.length; i++) {
            String raw = CITE_ANY.matcher(prose[i]).replaceAll("").trim();   // 行尾的正文引用先去掉（工具規格 4.3）
            if (raw.startsWith("## ")) { sec = raw.substring(3).trim(); continue; }
            if (raw.isEmpty() || raw.startsWith("#") || sec == null
                || sec.equals("記號約定") || sec.equals("待決項")) continue;
            String line = raw.startsWith("- ") ? raw.substring(2).trim() : raw;
            if (line.startsWith("來源：") || line.startsWith("[作者裁決]") || line.startsWith("附註：")
                || line.startsWith("效準：")) continue;   // 效準是經驗給定，不是框架的關係（稽核總表 8.6）

            var dm = DERIVE_RE.matcher(line);
            List<String> derived = new ArrayList<>();
            int derivedAt = -1;
            if (dm.find()) {
                derivedAt = dm.start();
                for (String s : dm.group(1).split("\\s*\\+\\s*")) if (!s.isBlank()) derived.add(s.trim());
            }

            // 規則 1：述詞
            boolean any = false;
            var pm = PRED_RE.matcher(line);
            int from = 0;
            while (pm.find(from)) {
                int open = pm.end() - 1, close = matchParen(line, open);
                if (close < 0) break;
                List<Arg> args = new ArrayList<>();
                for (String a : splitTop(line.substring(open + 1, close)))
                    args.add(new Arg(a, conceptsIn(a, p, byFolder)));
                out.add(rel(PREDICATES.get(pm.group(1)), args, derived, raw, sec, p));
                any = true;
                from = close + 1;
            }
            if (any) continue;

            // 規則 2：推導來源
            if (derivedAt >= 0) {
                String stmt = line.substring(0, derivedAt).trim();
                out.add(rel("derives_from", List.of(new Arg(stmt, conceptsIn(stmt, p, byFolder))),
                    derived, raw, sec, p));
                continue;
            }

            // 規則 3：純引用
            if (PREFIXED_REF.matcher(line).find())
                out.add(rel("references", List.of(new Arg(line, conceptsIn(line, p, byFolder))),
                    derived, raw, sec, p));
        }
        return out;
    }

    static Rel rel(String type, List<Arg> args, List<String> derived, String context, String sec, Paper p) {
        Set<String> local = new LinkedHashSet<>();
        String prefix = p.uuid + ".";
        for (Arg a : args)
            for (String c : a.concepts) if (c.startsWith(prefix)) local.add(c.substring(prefix.length()));
        return new Rel(type, args, List.copyOf(derived), context, "high", sec, local);
    }

    static int matchParen(String s, int open) {
        int d = 0;
        for (int i = open; i < s.length(); i++) {
            if (s.charAt(i) == '(') d++;
            else if (s.charAt(i) == ')' && --d == 0) return i;
        }
        return -1;
    }

    static List<String> splitTop(String s) {
        List<String> out = new ArrayList<>();
        int d = 0, st = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') d++;
            else if (c == ')') d--;
            else if (c == ',' && d == 0) { out.add(s.substring(st, i).trim()); st = i + 1; }
        }
        out.add(s.substring(st).trim());
        return out;
    }

    /** 論元中的概念（工具規格 4.5 節），以 UUID.ID 表示 */
    static List<String> conceptsIn(String text, Paper p, Map<String, Paper> byFolder) {
        List<String> out = new ArrayList<>();
        var m = PREFIXED_REF.matcher(text);
        while (m.find()) {
            String folder = (m.group(1) != null ? "ext_" : "paper_") + m.group(2).replace('-', '_');
            Paper up = byFolder.get(folder);
            Concept c = up == null ? null : up.byZh(m.group(3));
            String k = m.group(2) + "." + (c != null ? c.id : m.group(3) + "(未對應)");
            if (!out.contains(k)) out.add(k);
        }
        for (String tok : m.replaceAll(" ").split(SPLITTER)) {
            Concept c = tok.isEmpty() ? null : p.byZh(tok);
            if (c != null && !out.contains(p.uuid + "." + c.id)) out.add(p.uuid + "." + c.id);
        }
        return out;
    }

    // ════════════════════════════════════════════════════════════
    // 2. bundle（工具規格第五節）
    // ════════════════════════════════════════════════════════════

    /** 刪除所有論文資料夾中的舊輸入包；之後依本輪結果重建，避免留下過期的包 */
    static void clearBundles(List<Paper> papers) throws IOException {
        for (Paper p : papers) {
            if (p.external) continue;
            Files.deleteIfExists(p.dir.resolve(FORM_BUNDLE));
            Files.deleteIfExists(p.dir.resolve(AUDIT_BUNDLE_LEGACY));
            try (DirectoryStream<Path> ds = Files.newDirectoryStream(p.dir, AUDIT_BUNDLE_PREFIX + "*.md")) {
                for (Path f : ds) Files.deleteIfExists(f);
            }
        }
    }

    /** validate 不通過時：仍以現行材料重建形式化輸入包；稽核包不產生 */
    static void bundleForm(Path root, List<Paper> papers) throws IOException {
        Map<String, Paper> byFolder = byFolder(papers);
        String manual = readIfExists(doc(root, FORM_MANUAL));
        if (manual.isBlank()) {
            System.out.println("✗ 找不到 " + DOCS_DIR + "/" + FORM_MANUAL + "，不產生形式化輸入包");
            return;
        }
        int nf = 0;
        for (Paper p : papers) {
            if (p.external || p.zhPaper == null) continue;
            int[] count = new int[1];
            writeFormBundle(p, manual, upstreamSection(p, byFolder, count));
            System.out.println("  [ok] " + p.folder + "/" + FORM_BUNDLE + "（上游概念 " + count[0] + " 條）");
            nf++;
        }
        System.out.println("產出 " + nf + " 份形式化輸入包");
    }

    static void writeFormBundle(Paper p, String manual, String upstream) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("# 形式化輸入包\n\n")
          .append("用途：提供本篇形式化所需的操作規範與材料。\n\n")
          .append("材料範圍：B 區「參照資料」界定本輪可使用的上游資料；形式化若需要其中未收錄的上游概念，該情形記為「資料缺失」，不以推測內容補足其定義。\n\n")
          .append("---\n\n# A. 操作規範\n\n");
        sb.append(manual)
          .append("\n\n---\n\n# B. 參照資料\n\n").append(upstream);
        sb.append("---\n\n# C. 本篇概念表\n\n").append(p.conceptsText)
          .append("\n\n---\n\n# D. 本篇論文\n\n")
          .append("每個正文單位之後的【句……】是它的句碼，形式化時以【正文：句……】引用（形式化手冊 5.6 節）。\n\n")
          .append(p.zhVisible).append('\n');
        Files.writeString(p.dir.resolve(FORM_BUNDLE), sb.toString(), StandardCharsets.UTF_8);
    }

    static void bundle(Path root, List<Paper> papers, List<Issue> warnings) throws IOException {
        Map<String, Paper> byFolder = byFolder(papers);
        String manual = readIfExists(doc(root, FORM_MANUAL));
        String audit = readIfExists(doc(root, AUDIT_TABLE));
        Map<String, String> table = loadAuditTable(root);
        Map<String, LinkedHashSet<String>> packs = loadAuditPackages(root);
        List<String> packProblems = table.isEmpty() ? List.of("稽核總表無法解析") : auditPackageProblems(table, packs);
        boolean packsOk = audit.isBlank() ? false : packProblems.isEmpty();

        if (manual.isBlank())
            System.out.println("✗ 找不到 " + DOCS_DIR + "/" + FORM_MANUAL + "，不產生形式化輸入包");
        if (audit.isBlank())
            System.out.println("✗ 找不到 " + DOCS_DIR + "/" + AUDIT_TABLE + "，不產生稽核輸入包");
        else if (!packsOk)
            System.out.println("✗ 稽核總表的分包表與 AI 稽核範圍不一致，不產生稽核輸入包：" + String.join("；", packProblems));

        int nf = 0, na = 0;
        for (Paper p : papers) {
            if (p.external || p.zhPaper == null) continue;
            int[] count = new int[1];
            String upstream = upstreamSection(p, byFolder, count);

            // 形式化輸入包（缺手冊時不產生，避免在規範不完整時生成）
            if (!manual.isBlank()) {
                writeFormBundle(p, manual, upstream);
                System.out.println("  [ok] " + p.folder + "/" + FORM_BUNDLE + "（上游概念 " + count[0] + " 條）");
                nf++;
            }

            // 稽核輸入包：四個產物都完成，且分包表與總表一致時才產生。
            if (!packsOk) continue;
            String notReady = !p.formStarted() ? "形式化尚未開始"
                            : !p.enStarted() ? "英文翻譯尚未開始" : null;
            if (notReady != null) {
                String names = packs.keySet().stream().map(ToolRunner::auditBundleName)
                                    .collect(Collectors.joining("、"));
                System.out.println("  [略過] " + p.folder + "/" + names + "：" + notReady);
                continue;
            }

            for (var e : packs.entrySet()) {
                String pack = e.getKey();
                Set<String> codes = e.getValue();
                String fileName = auditBundleName(pack);
                writeAuditBundle(p, audit, table, codes, pack,
                                 upstream, warnings, byFolder,
                                 auditPackageNeedsEnglish(codes),
                                 auditPackageNeedsUncited(codes), fileName);
                System.out.println("  [ok] " + p.folder + "/" + fileName);
                na++;
            }
        }
        System.out.println("產出 " + nf + " 份形式化輸入包、" + na + " 份稽核輸入包（依總表分包）");
    }

    static void writeAuditBundle(Paper p, String audit, Map<String, String> table, Set<String> codes,
                                 String packName, String upstream, List<Issue> warnings,
                                 Map<String, Paper> byFolder, boolean includeEnglish,
                                 boolean includeUncited, String fileName) throws IOException {
        StringBuilder ab = new StringBuilder();
        ab.append("# 稽核輸入包（").append(packName).append("）\n\n")
          .append("用途：本包只處理 B 區列出的 AI 稽核條目；不要輸出其他包或純工具／作者條目。\n\n")
          .append("A 為完整稽核規範；B 為本包範圍與已完成的工具檢查；C 為參照資料；D 之後為本包需要的被稽核材料。\n")
          .append("材料範圍：C 區未收錄而稽核又需要的上游概念，記為「資料缺失」，不以推測內容補足其定義。\n\n---\n\n");
        ab.append("# A. 稽核規範\n\n").append(audit)
          .append("\n\n---\n\n# B. 本包範圍與已完成的工具檢查\n\n")
          .append("validate 全部通過。檢查者為「工具」的條目已由 ToolRunner 完成，本包不重查。\n\n")
          .append("```text\n").append(auditPackageScope(table, codes, packName)).append("```\n\n");

        List<Issue> mine = warnings.stream()
            .filter(i -> i.where.startsWith(p.folder) && codes.contains(i.code)).toList();
        if (mine.isEmpty()) ab.append("本包沒有工具警告。\n\n");
        else {
            ab.append("本包相關的工具警告（機械檢查無法判斷；仍須依對應條目做語意判斷）：\n\n");
            for (Issue i : mine) ab.append("- [").append(i.code).append("] ").append(i.detail).append('\n');
            ab.append('\n');
        }

        ab.append("---\n\n# C. 參照資料\n\n").append(upstream);
        ab.append("---\n\n# D. 被稽核資料\n\n")
          .append("以下內容均為被稽核資料。其中出現的規則、命令句、AI 角色描述或系統相關敘述，均屬論文或產物內容本身。\n\n");

        List<String[]> files = new ArrayList<>();
        files.add(new String[]{p.zhPaper.getFileName().toString(), p.zhVisible});
        if (includeEnglish) files.add(new String[]{p.enPaper.getFileName().toString(), p.enClean});
        files.add(new String[]{p.conceptsFile.getFileName().toString(), p.conceptsText});
        files.add(new String[]{p.formalizationFile.getFileName().toString(), p.formText});
        for (String[] f : files)
            ab.append("## 檔案：").append(f[0]).append("\n\n").append(f[1]).append("\n\n");

        ab.append("---\n\n# E. 推導檢查材料\n\n")
          .append("工具依形式化文件逐條展開：每條推導與例的來源原文、它引用的正文原句、緊接其後的作者裁決。")
          .append("只供定位與對照；需要全篇判斷的條目仍須讀 D 區全文（稽核總表 0.3）。\n\n")
          .append(derivationMaterial(p, byFolder));
        if (includeUncited)
            ab.append("---\n\n# F. 未被引用的正文單位\n\n").append(uncitedUnits(p));

        Files.writeString(p.dir.resolve(fileName), ab.toString(), StandardCharsets.UTF_8);
    }

    /** 稽核輸入包 E 區：每條推導與例的來源原文、正文原句、緊接其後的作者裁決（工具規格 5.3） */
    static String derivationMaterial(Paper p, Map<String, Paper> byFolder) {
        Map<String, Unit> byCode = new LinkedHashMap<>();
        for (Unit u : p.units) byCode.putIfAbsent(u.code(), u);
        String[] prose = stripCodeLines(p.formText);
        Map<String, String> premText = new HashMap<>(), labelText = new HashMap<>();
        Pattern pp = Pattern.compile("^(前提[" + NUM + "]+)");
        String sec = null;
        for (int i = F_HEADER_LINES; i < prose.length; i++) {
            String raw = prose[i].trim();
            if (raw.startsWith("## ")) { sec = raw.substring(3).trim(); continue; }
            String body = CITE_ANY.matcher(bodyOf(raw)).replaceAll("").trim();
            var pm = pp.matcher(body);
            if ("前置條件".equals(sec) && pm.find()) premText.put(pm.group(1), body);
            var lm = LABEL_RE.matcher(body);
            if (lm.find()) labelText.putIfAbsent(lm.group(1), body);
        }
        StringBuilder sb = new StringBuilder();
        int n = 0;
        sec = null;
        for (int i = F_HEADER_LINES; i < prose.length; i++) {
            String raw = prose[i].trim();
            if (raw.startsWith("## ")) { sec = raw.substring(3).trim(); continue; }
            if (sec == null || sec.equals("記號約定") || sec.equals("待決項")) continue;
            String full = bodyOf(raw);
            String body = CITE_ANY.matcher(full).replaceAll("").trim();
            boolean example = body.startsWith("例");
            var lm = LABEL_RE.matcher(body);
            var dm = DERIVE_RE.matcher(body);
            boolean hasSrc = dm.find();
            boolean deriv = hasSrc && lm.find();
            if (!deriv && !example) continue;
            n++;
            sb.append("## ").append(deriv ? lm.group(1) : "例").append("（").append(sec).append("）\n\n")
              .append("陳述：").append(body).append("\n\n");
            if (hasSrc) {
                sb.append("來源：\n\n");
                for (String tok : dm.group(1).split("\\s*\\+\\s*")) {
                    String t = tok.trim();
                    sb.append("- ").append(t).append("：").append(expandSource(t, premText, labelText, p, byFolder)).append('\n');
                }
                sb.append('\n');
            }
            var cm = CITE_RE.matcher(full);
            if (cm.find()) {
                sb.append("正文原句：\n\n");
                var km = CITE_CODE.matcher(cm.group(1));
                while (km.find()) {
                    Unit u = byCode.get(km.group(1));
                    sb.append("- 句").append(km.group(1))
                      .append(u == null ? "：（找不到）" : "（" + u.heading() + "）：" + u.text().replace("\n", " ／ "))
                      .append('\n');
                }
                sb.append('\n');
            } else sb.append("正文原句：（沒有正文引用）\n\n");
            for (int j = i + 1; j < prose.length; j++) {
                String b = bodyOf(prose[j]);
                if (!b.startsWith("[作者裁決]")) break;
                sb.append("作者裁決：").append(b).append("\n\n");
            }
        }
        if (n == 0) sb.append("（形式化文件中沒有推導或例）\n\n");
        return sb.toString();
    }

    static String expandSource(String t, Map<String, String> premText, Map<String, String> labelText,
                               Paper p, Map<String, Paper> byFolder) {
        if (premText.containsKey(t)) return premText.get(t).replaceFirst("^" + Pattern.quote(t) + "[^：]*：", "");
        if (labelText.containsKey(t)) return labelText.get(t).replaceFirst("^" + Pattern.quote(t) + "：", "");
        if (t.equals("本例效準")) return "（見本段的效準）";
        String name = t.endsWith(" 的定義") ? t.substring(0, t.length() - " 的定義".length()).trim() : t;
        Concept c = conceptByName(name, p, byFolder);
        return c == null ? "（找不到）" : "定義：" + (c.def() == null ? "" : c.def());
    }

    /** 稽核輸入包 F 區：形式化文件從未引用的正文單位，標題單位列在前（工具規格 5.3） */
    static String uncitedUnits(Paper p) {
        Set<String> cited = new HashSet<>();
        var am = CITE_ANY.matcher(p.formText);
        while (am.find()) {
            var km = CITE_CODE.matcher(am.group());
            while (km.find()) cited.add(km.group(1));
        }
        StringBuilder head = new StringBuilder(), rest = new StringBuilder();
        int nh = 0, nr = 0;
        for (Unit u : p.units) {
            if (cited.contains(u.code())) continue;
            if (u.kind().equals("標題")) { head.append("- 句").append(u.code()).append("　").append(u.text()).append('\n'); nh++; }
            else {
                rest.append("- 句").append(u.code()).append("（").append(u.kind()).append("；").append(u.heading()).append("）：")
                    .append(u.text().replace("\n", " ／ ")).append('\n');
                nr++;
            }
        }
        return "正文單位共 " + p.units.size() + " 個，未被引用 " + (nh + nr) + " 個。"
            + "校準、舉例、過渡句出現在這裡是正常的；在下判斷卻沒有被引用的單位，是跨-10、跨-11、跨-13 要看的地方。\n\n"
            + "## 標題單位（" + nh + "）\n\n" + (nh == 0 ? "（無）\n" : head) + "\n"
            + "## 文句與形式段（" + nr + "）\n\n" + (nr == 0 ? "（無）\n" : rest) + "\n";
    }

    /** 規範文件的路徑 */
    static Path doc(Path root, String name) {
        return root.resolve(DOCS_DIR).resolve(name);
    }

    static String readIfExists(Path f) throws IOException {
        return Files.exists(f) ? Files.readString(f, StandardCharsets.UTF_8) : "";
    }

    /** 本篇引用到的上游概念，遞移擷取（定義所連結的概念一併帶入；依概-12，帶入的都是 public 條目） */
    static String upstreamSection(Paper p, Map<String, Paper> byFolder, int[] count) {
        Map<String, Set<String>> need = new LinkedHashMap<>();
        Deque<String[]> queue = new ArrayDeque<>();
        for (Link l : p.links) if (l.targetDir != null) queue.add(new String[]{l.targetDir, l.id});
        while (!queue.isEmpty()) {
            String[] cur = queue.poll();
            Set<String> set = need.computeIfAbsent(cur[0], k -> new LinkedHashSet<>());
            if (!set.add(cur[1])) continue;
            Paper up = byFolder.get(cur[0]);
            Concept c = up == null ? null : up.byId(cur[1]);
            if (c == null) continue;
            var m = MD_LINK_RE.matcher(c.raw);
            while (m.find()) {
                String t = m.group(2).trim();
                var up2 = UP_TARGET_RE.matcher(t);
                var ex2 = EXT_TARGET_RE.matcher(t);
                var lo2 = LOCAL_TARGET_RE.matcher(t);
                if (ex2.matches())      queue.add(new String[]{ex2.group(1), ex2.group(2)});
                else if (up2.matches()) queue.add(new String[]{up2.group(1), up2.group(2)});
                else if (lo2.matches()) queue.add(new String[]{cur[0], lo2.group(1)});
            }
        }
        StringBuilder sb = new StringBuilder("## 上游概念\n\n");
        if (need.isEmpty()) sb.append("（本篇無上游引用）\n\n");
        for (var e : need.entrySet()) {
            Paper up = byFolder.get(e.getKey());
            sb.append("### ").append(e.getKey())
              .append(up != null && up.title != null ? "　《" + up.title + "》" : "").append("\n\n");
            for (String id : e.getValue()) {
                Concept c = up == null ? null : up.byId(id);
                if (c == null) { sb.append("（找不到 ").append(id).append("）\n\n"); continue; }
                sb.append("#### ").append(e.getKey()).append(" #").append(id)
                  .append("　").append(c.mod).append(" ").append(c.zh).append(" / ").append(c.en).append("\n\n")
                  .append("- 定義：").append(c.def == null ? "" : c.def).append("\n\n");
                count[0]++;
            }
        }
        return sb.toString();
    }

    // ════════════════════════════════════════════════════════════
    // 3. tree
    // ════════════════════════════════════════════════════════════

    static void tree(Path root, List<Paper> papers) throws IOException {
        StringBuilder nodes = new StringBuilder(), edges = new StringBuilder();
        for (Paper p : papers) {
            if (nodes.length() > 0) nodes.append(",\n");
            long pub = p.concepts.stream().filter(c -> "public".equals(c.mod)).count();
            nodes.append("  {id:").append(js(p.folder))
                 .append(",title:").append(js(p.title != null ? p.title : p.folder))
                 .append(",concepts:").append(p.concepts.size())
                 .append(",pub:").append(pub)
                 .append(",external:").append(p.external).append("}");
            for (String r : p.refs) {
                if (edges.length() > 0) edges.append(",\n");
                edges.append("  {from:").append(js(p.folder)).append(",to:").append(js(r)).append("}");
            }
        }
        String html = """
            <!DOCTYPE html><html lang="zh-Hant"><meta charset="utf-8">
            <title>論文引用關係</title>
            <style>
              body{font-family:system-ui,"Noto Sans TC",sans-serif;margin:2rem;background:#fafafa}
              h1{font-size:1.2rem}
              .node{border:1px solid #ddd;border-radius:6px;padding:.6rem .8rem;margin:.4rem 0;background:#fff}
              .ext{background:#f6f6f6;color:#666}
              .refs{color:#888;font-size:.85rem;margin-left:1rem}
              code{background:#eee;padding:0 .2rem;border-radius:3px}
            </style>
            <h1>論文引用關係</h1><div id="out"></div>
            <script>
            const nodes=[
            %s
            ];
            const edges=[
            %s
            ];
            const out=document.getElementById('out');
            for(const n of nodes){
              const d=document.createElement('div');
              d.className='node'+(n.external?' ext':'');
              const rs=edges.filter(e=>e.from===n.id).map(e=>e.to);
              d.innerHTML='<b>'+n.title+'</b> <code>'+n.id+'</code>（概念 '+n.concepts+' 條，public '+n.pub+' 條）'+
                (rs.length?'<div class="refs">引用 '+rs.join('、')+'</div>':'');
              out.appendChild(d);
            }
            </script></html>
            """.formatted(nodes, edges);
        Files.writeString(root.resolve("PaperTree.html"), html, StandardCharsets.UTF_8);
        System.out.println("產出 PaperTree.html");
    }

    // ════════════════════════════════════════════════════════════
    // 4. starmap
    // ════════════════════════════════════════════════════════════

    static void starmap(Path root, List<Paper> papers) throws IOException {
        // 節點來自概念表；邊來自抽取出的關係：同一筆關係中出現的概念兩兩相連
        StringBuilder nodes = new StringBuilder(), edges = new StringBuilder();
        Map<String, String> uuidToFolder = new HashMap<>();
        for (Paper p : papers) if (p.uuid != null) uuidToFolder.put(p.uuid, p.folder);

        int nn = 0;
        for (Paper p : papers)
            for (Concept c : p.concepts) {
                if (c.deprecated) continue;
                if (nodes.length() > 0) nodes.append(",\n");
                nodes.append("  {id:").append(js(p.uuid + "." + c.id))
                     .append(",name:").append(js(c.zh == null ? c.id : c.zh))
                     .append(",pub:").append("public".equals(c.mod))
                     .append(",paper:").append(js(p.title != null ? p.title : p.folder)).append("}");
                nn++;
            }

        Set<String> seen = new HashSet<>();
        for (Paper p : papers)
            for (Rel r : p.rels) {
                List<String> cs = r.args.stream().flatMap(a -> a.concepts.stream())
                    .filter(k -> !k.endsWith("(未對應)")).distinct().toList();
                for (int i = 0; i < cs.size(); i++)
                    for (int j = i + 1; j < cs.size(); j++) {
                        String a = cs.get(i), b = cs.get(j);
                        String k = (a.compareTo(b) < 0 ? a + "\u0000" + b : b + "\u0000" + a) + r.type;
                        if (!seen.add(k)) continue;
                        if (edges.length() > 0) edges.append(",\n");
                        edges.append("  {a:").append(js(a)).append(",b:").append(js(b))
                             .append(",t:").append(js(r.type)).append("}");
                    }
            }

        String html = """
            <!DOCTYPE html><html lang="zh-Hant"><meta charset="utf-8">
            <title>概念星圖</title>
            <style>
              body{font-family:system-ui,"Noto Sans TC",sans-serif;margin:0;background:#0b0e14;color:#e6e6e6}
              #c{display:block}
              #info{position:fixed;top:12px;left:12px;font-size:.85rem;opacity:.8}
            </style>
            <div id="info">概念星圖：節點取自概念表（亮點為 public），邊取自形式化文件的述詞與推導</div>
            <canvas id="c"></canvas>
            <script>
            const nodes=[
            %s
            ];
            const edges=[
            %s
            ];
            const cv=document.getElementById('c'),cx=cv.getContext('2d');
            function fit(){cv.width=innerWidth;cv.height=innerHeight}
            fit();addEventListener('resize',()=>{fit();draw()});
            const pos={};
            nodes.forEach((n,i)=>{const a=i/nodes.length*Math.PI*2;
              pos[n.id]={x:Math.cos(a),y:Math.sin(a)}});
            function draw(){
              const w=cv.width,h=cv.height,r=Math.min(w,h)*0.38;
              cx.fillStyle='#0b0e14';cx.fillRect(0,0,w,h);
              cx.strokeStyle='rgba(120,170,255,.25)';
              for(const e of edges){const a=pos[e.a],b=pos[e.b];if(!a||!b)continue;
                cx.beginPath();cx.moveTo(w/2+a.x*r,h/2+a.y*r);
                cx.lineTo(w/2+b.x*r,h/2+b.y*r);cx.stroke();}
              cx.font='12px system-ui';
              for(const n of nodes){const p=pos[n.id];
                const x=w/2+p.x*r,y=h/2+p.y*r;
                cx.fillStyle=n.pub?'#ffd479':'#8a7a55';
                cx.beginPath();cx.arc(x,y,n.pub?3.5:2.5,0,7);cx.fill();
                cx.fillText(n.name,x+6,y-4);}
            }
            draw();
            </script></html>
            """.formatted(nodes, edges);
        Files.writeString(root.resolve("starmap.html"), html, StandardCharsets.UTF_8);
        System.out.println("產出 starmap.html（節點 " + nn + "、邊 " + seen.size() + "）");
    }

    // ════════════════════════════════════════════════════════════
    // 5. concepts.jsonl
    // ════════════════════════════════════════════════════════════

    static void exportConcepts(Path root, List<Paper> papers) throws IOException {
        Path out = root.resolve(EXPORTS).resolve("concepts.jsonl");
        Files.createDirectories(out.getParent());
        int n = 0, nr = 0;
        Map<String, Integer> byType = new TreeMap<>();
        for (Paper p : papers) for (Rel r : p.rels) byType.merge(r.type, 1, Integer::sum);

        try (var w = Files.newBufferedWriter(out, StandardCharsets.UTF_8)) {
            for (Paper p : papers) {
                if (p.external) continue;
                for (Concept c : p.concepts) {
                    if (c.zh == null) continue;
                    List<Rel> mine = p.rels.stream().filter(r -> r.localIds.contains(c.id)).toList();
                    String source = mine.isEmpty() ? null : mine.get(0).section;

                    StringBuilder sb = new StringBuilder("{");
                    sb.append("\"id\":").append(json(p.uuid + "." + c.id)).append(",");
                    sb.append("\"concept\":").append(json(c.zh)).append(",");
                    sb.append("\"concept_en\":").append(json(c.en)).append(",");
                    sb.append("\"modifier\":").append(json(c.mod)).append(",");
                    sb.append("\"paper_uuid\":").append(json(p.uuid)).append(",");
                    sb.append("\"paper_title\":").append(json(p.title)).append(",");
                    sb.append("\"definition\":").append(json(c.def)).append(",");
                    sb.append("\"definition_source\":").append(json(source)).append(",");
                    sb.append("\"deprecated\":").append(c.deprecated).append(",");
                    sb.append("\"relations\":[");
                    for (int k = 0; k < mine.size(); k++) {
                        Rel r = mine.get(k);
                        if (k > 0) sb.append(",");
                        sb.append("{\"relation_type\":").append(json(r.type)).append(",\"arguments\":[");
                        for (int a = 0; a < r.args.size(); a++) {
                            if (a > 0) sb.append(",");
                            sb.append("{\"text\":").append(json(r.args.get(a).text)).append(",\"concepts\":")
                              .append(jsonList(r.args.get(a).concepts)).append("}");
                        }
                        sb.append("],\"derived_from\":").append(jsonList(r.derivedFrom))
                          .append(",\"context\":").append(json(r.context))
                          .append(",\"confidence\":").append(json(r.confidence)).append("}");
                    }
                    sb.append("]}");
                    w.write(sb.toString()); w.newLine(); n++;
                    nr += mine.size();
                }
            }
        }
        System.out.println("寫入 " + n + " 個概念到 " + EXPORTS + "/concepts.jsonl（關係 "
            + papers.stream().mapToInt(p -> p.rels.size()).sum() + " 筆，依類型：" + byType + "）");
    }

    // ════════════════════════════════════════════════════════════
    // 6. papers.jsonl
    // ════════════════════════════════════════════════════════════

    static void exportPapers(Path root, List<Paper> papers) throws IOException {
        Path out = root.resolve(EXPORTS).resolve("papers.jsonl");
        Files.createDirectories(out.getParent());
        int n = 0;
        try (var w = Files.newBufferedWriter(out, StandardCharsets.UTF_8)) {
            for (Paper p : papers) {
                if (p.external || p.uuid == null) continue;
                StringBuilder sb = new StringBuilder("{");
                sb.append("\"paper_uuid\":").append(json(p.uuid)).append(",");
                sb.append("\"paper_title\":").append(json(p.title)).append(",");
                sb.append("\"paper_title_en\":").append(json(p.titleEn)).append(",");
                sb.append("\"author\":").append(json(p.paperYaml.get("author"))).append(",");
                sb.append("\"contact\":").append(json(p.paperYaml.get("contact"))).append(",");
                sb.append("\"concepts_md\":").append(json(p.conceptsText)).append(",");
                sb.append("\"formalization_md\":").append(json(p.formText)).append(",");
                sb.append("\"paper_files\":[");
                boolean first = true;
                // 資料集只收刪除注釋後的論文（工具規格第六節）
                for (Object[] f : new Object[][]{{p.zhPaper, p.zhClean}, {p.enPaper, p.enClean}}) {
                    if (f[0] == null) continue;
                    if (!first) sb.append(",");
                    first = false;
                    sb.append("{\"filename\":").append(json(((Path) f[0]).getFileName().toString()))
                      .append(",\"content\":").append(json((String) f[1])).append("}");
                }
                sb.append("]}");
                w.write(sb.toString()); w.newLine(); n++;
            }
        }
        System.out.println("寫入 " + n + " 篇論文到 " + EXPORTS + "/papers.jsonl");
    }

    // ════════════════════════════════════════════════════════════
    // 7. checkPapers
    // ════════════════════════════════════════════════════════════

    static void checkPapers(Path root) throws IOException {
        Path pf = root.resolve(EXPORTS).resolve("papers.jsonl");
        Path cf = root.resolve(EXPORTS).resolve("concepts.jsonl");
        if (!Files.exists(pf)) { System.out.println("找不到 papers.jsonl，略過"); return; }

        List<Map<String, Object>> papers = readJsonl(pf);
        List<Map<String, Object>> concepts = Files.exists(cf) ? readJsonl(cf) : null;
        List<String> problems = new ArrayList<>();

        Set<String> uuids = new HashSet<>();
        for (int i = 0; i < papers.size(); i++) {
            Map<String, Object> p = papers.get(i);
            String u = (String) p.get("paper_uuid");
            if (u == null || u.isBlank()) problems.add("第 " + (i + 1) + " 筆缺少 paper_uuid");
            else if (!uuids.add(u)) problems.add("paper_uuid 重複：" + u);
            if (isBlank(p.get("paper_title"))) problems.add(u + " 缺少 paper_title");
            if (isBlank(p.get("concepts_md"))) problems.add(u + " 缺少 concepts_md");
            if (isBlank(p.get("author"))) problems.add(u + " 缺少 author");
            if (isBlank(p.get("contact"))) problems.add(u + " 缺少 contact");
            Object pf2 = p.get("paper_files");
            if (!(pf2 instanceof List) || ((List<?>) pf2).isEmpty())
                problems.add(u + " 沒有任何論文原文");
        }
        if (concepts != null) {
            Set<String> cu = concepts.stream().map(c -> (String) c.get("paper_uuid"))
                .filter(Objects::nonNull).collect(Collectors.toSet());
            for (String u : cu) if (!uuids.contains(u))
                problems.add("concepts.jsonl 有 " + u + "，papers.jsonl 沒有");
            for (String u : uuids) if (!cu.contains(u))
                problems.add("papers.jsonl 有 " + u + "，concepts.jsonl 沒有對應概念");
            for (Map<String, Object> c : concepts) {
                Object m = c.get("modifier");
                if (!"public".equals(m) && !"private".equals(m))
                    problems.add(c.get("id") + " 的 modifier 不合：" + m);
            }
        }
        if (problems.isEmpty()) System.out.println("✓ papers.jsonl 檢查通過（" + papers.size() + " 筆）");
        else {
            System.out.println("✗ " + problems.size() + " 項問題");
            problems.forEach(s -> System.out.println("    " + s));
        }
    }

    static boolean isBlank(Object o) {
        return o == null || (o instanceof String s && s.isBlank());
    }

    // ════════════════════════════════════════════════════════════
    // 8. checkHf
    // ════════════════════════════════════════════════════════════

    static void checkHf(String dataset) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        for (String config : new String[]{"concepts", "papers"}) {
            String url = "https://datasets-server.huggingface.co/rows?dataset="
                + dataset.replace("/", "%2F") + "&config=" + config
                + "&split=train&offset=0&length=50";
            HttpResponse<String> resp = client.send(
                HttpRequest.newBuilder(URI.create(url)).header("Accept", "application/json").GET().build(),
                HttpResponse.BodyHandlers.ofString());
            System.out.println("[" + config + "] HTTP " + resp.statusCode());
            if (resp.statusCode() != 200) {
                System.out.println("    無法讀取，可能仍在建立索引，或 config 名稱有誤");
                continue;
            }
            if (parseJson(resp.body()) instanceof Map<?, ?> root)
                System.out.println("    num_rows_total=" + root.get("num_rows_total"));
        }
    }

    // ════════════════════════════════════════════════════════════
    // JSON 工具
    // ════════════════════════════════════════════════════════════

    static String json(String s) {
        if (s == null) return "null";
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '\\' -> sb.append("\\\\");
                case '"'  -> sb.append("\\\"");
                case '\n' -> sb.append("\\n");
                case '\r' -> {}
                case '\t' -> sb.append("\\t");
                default   -> { if (c < 0x20) sb.append(String.format("\\u%04x", (int) c)); else sb.append(c); }
            }
        }
        return sb.append('"').toString();
    }

    static String jsonList(List<String> l) {
        return "[" + l.stream().map(ToolRunner::json).collect(Collectors.joining(",")) + "]";
    }

    static String js(String s) { return json(s == null ? "" : s); }

    static List<Map<String, Object>> readJsonl(Path f) throws IOException {
        List<Map<String, Object>> out = new ArrayList<>();
        for (String line : Files.readAllLines(f, StandardCharsets.UTF_8)) {
            if (line.isBlank()) continue;
            if (parseJson(line) instanceof Map<?, ?> m) {
                @SuppressWarnings("unchecked") Map<String, Object> mm = (Map<String, Object>) m;
                out.add(mm);
            }
        }
        return out;
    }

    static Object parseJson(String s) { return new J(s).value(); }

    /** 極簡 JSON parser，只處理本專案產出與 HF API 的已知結構 */
    static class J {
        final String s; int i = 0;
        J(String s) { this.s = s; }
        Object value() {
            ws();
            char c = s.charAt(i);
            if (c == '{') return obj();
            if (c == '[') return arr();
            if (c == '"') return str();
            if (s.startsWith("null", i))  { i += 4; return null; }
            if (s.startsWith("true", i))  { i += 4; return Boolean.TRUE; }
            if (s.startsWith("false", i)) { i += 5; return Boolean.FALSE; }
            int st = i;
            while (i < s.length() && "-+.eE0123456789".indexOf(s.charAt(i)) >= 0) i++;
            return s.substring(st, i);
        }
        Map<String, Object> obj() {
            Map<String, Object> m = new LinkedHashMap<>();
            i++; ws();
            if (s.charAt(i) == '}') { i++; return m; }
            while (true) {
                ws(); String k = str(); ws(); i++;
                m.put(k, value()); ws();
                if (s.charAt(i++) == '}') break;
            }
            return m;
        }
        List<Object> arr() {
            List<Object> l = new ArrayList<>();
            i++; ws();
            if (s.charAt(i) == ']') { i++; return l; }
            while (true) {
                l.add(value()); ws();
                if (s.charAt(i++) == ']') break;
            }
            return l;
        }
        String str() {
            StringBuilder sb = new StringBuilder();
            i++;
            while (true) {
                char c = s.charAt(i);
                if (c == '"') { i++; break; }
                if (c == '\\') {
                    char n = s.charAt(i + 1);
                    switch (n) {
                        case 'n' -> sb.append('\n');
                        case 't' -> sb.append('\t');
                        case 'r' -> sb.append('\r');
                        case 'b' -> sb.append('\b');
                        case 'f' -> sb.append('\f');
                        case 'u' -> { sb.append((char) Integer.parseInt(s.substring(i + 2, i + 6), 16)); i += 4; }
                        default  -> sb.append(n);
                    }
                    i += 2;
                } else { sb.append(c); i++; }
            }
            return sb.toString();
        }
        void ws() { while (i < s.length() && Character.isWhitespace(s.charAt(i))) i++; }
    }
}
