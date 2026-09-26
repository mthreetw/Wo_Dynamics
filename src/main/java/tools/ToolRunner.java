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
 *   0. stamp      更新 Git 暫存區中檔案的時間戳（只改時間戳那一行）
 *   1. validate   格式與連結檢查（稽核總表中檢查者為「工具」或「工具＋AI」的條目）
 *   2. bundle     在論文資料夾中產出「形式化輸入包.md」與「稽核輸入包.md」
 *   3. collect    彙整所有 formalization.md 到 formalizations/
 *   4. tree       產出 PaperTree.html（引用關係圖）
 *   5. starmap    產出 starmap.html（概念關係星圖）
 *   6. concepts   產出 exports/concepts.jsonl（依工具規格第四節抽取並分類關係）
 *   7. papers     產出 exports/papers.jsonl
 *   8. checkPapers  檢查 papers.jsonl 結構
 *   9. checkHf    遠端檢查 HF dataset 狀態（需要網路；未設定 HF_DATASET 時略過）
 *
 * validate 有「不通過」時停止，不執行後續步驟；「警告」不阻擋。
 * Issue 代碼即稽核總表的約束編號（論、譯、概、存、形、跨）。
 * 規格見《群星規範——工具規格》。
 */
public class ToolRunner {

    // ════════════════════════════════════════════════════════════
    // 設定
    // ════════════════════════════════════════════════════════════

    static final String SRC_DIR    = "src/main/java";
    static final String EXPORTS    = "exports";
    /** HF 資料集名稱（例如「帳號/資料集」）。尚未發布時留空，第 9 步會略過。 */
    static final String HF_DATASET = "";
    /** 規範文件所在目錄（相對於專案根目錄） */
    static final String DOCS_DIR   = "src/main/resources";
    /** 形式化手冊（附在形式化輸入包中）；找不到時不產生形式化輸入包 */
    static final String FORM_MANUAL = "群星規範-形式化手冊.md";
    /** 稽核總表（附在稽核輸入包中，報告讀取「檢查者」欄）；找不到時不產生稽核輸入包 */
    static final String AUDIT_TABLE = "群星規範-稽核總表.md";
    /** 論文資料夾中的輸入包（生成物，每次執行先刪除再重建；不視為論文檔） */
    static final String FORM_BUNDLE  = "形式化輸入包.md";
    static final String AUDIT_BUNDLE = "稽核輸入包.md";
    /** 形式化文件頭的行數：論文、UUID */
    static final int F_HEADER_LINES = 2;

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
        List<Rel> rels = new ArrayList<>();

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
        + "([^\\s，,、。；;（(）)：:「」]+)");

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
    static final String SPLITTER = "[\\s，。、；：:（）()「」《》〈〉\\[\\]\\-_+,/]+";

    static final Pattern FOREIGN = Pattern.compile("[A-Za-z\\u0370-\\u03FF]+");
    static final String NUM = "一二三四五六七八九十";

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

        section("0. stamp");
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
            System.out.println("\n格式檢查未通過，後續步驟中止。");
            return;
        }

        Map<String, Paper> byFolder = byFolder(papers);
        for (Paper p : papers) if (!p.external) p.rels = extractRelations(p, byFolder);

        section("2. bundle");
        bundle(root, papers, warnings);

        section("3. collect");
        collect(root, papers);

        section("4. tree");
        tree(root, papers);

        section("5. starmap");
        starmap(root, papers);

        section("6. concepts.jsonl");
        exportConcepts(root, papers);

        section("7. papers.jsonl");
        exportPapers(root, papers);

        section("8. checkPapers");
        checkPapers(root);

        section("9. checkHf");
        if (HF_DATASET.isBlank()) System.out.println("尚未設定 HF 資料集，略過");
        else try {
            checkHf(HF_DATASET);
        } catch (Exception e) {
            System.out.println("（遠端檢查略過：" + e.getClass().getSimpleName() + " " + e.getMessage() + "）");
        }

        System.out.println("\n✓ 完成");
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
            if (path.endsWith(FORM_BUNDLE) || path.endsWith(AUDIT_BUNDLE)) continue;   // 生成物不蓋時間戳
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
                               && !n.equals(FORM_BUNDLE) && !n.equals(AUDIT_BUNDLE)
                               && !n.equals("README.md");
                       })
                       .sorted().collect(Collectors.toList());
            }
            for (Path md : mds) {
                String text = Files.readString(md, StandardCharsets.UTF_8);
                String name = md.getFileName().toString();
                String base = name.substring(0, name.length() - 3);
                if (isMostlyAscii(base)) {
                    p.enPaper = md; p.enText = text; p.titleEn = base;
                    p.linksEn = scanLinks(text, name);
                } else {
                    p.zhPaper = md; p.zhText = text;
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

            // ── 英文翻譯：檔名與標題（譯-01、譯-02）。尚未開始翻譯時，譯組與跨-19 暫不檢查 ──
            boolean en = p.enStarted();
            if (en) {
                String enBase = baseName(p.enPaper), eh1 = firstH1(p.enText);
                if (enBase.equals(EN_TITLE_PLACEHOLDER))
                    is.add(new Issue("譯-01", w, "英文翻譯的檔名仍是佔位符「" + EN_TITLE_PLACEHOLDER + "」"));
                if (!enBase.equals(eh1))
                    is.add(new Issue("譯-02", w, "英文翻譯的一級標題「" + (eh1 == null ? "（沒有）" : eh1)
                        + "」與英文檔名「" + enBase + "」不一致"));
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

            // ── 形式化文件（形-01～形-33、跨-23～跨-25、跨-28、跨-29） ──
            if (p.formalizationFile == null)
                is.add(new Issue("形-01", w, "缺少 formalization.md"));
            else
                checkFormalization(is, p, byFolder, zhBase);
        }

        // ── 循環引用（跨-20） ──
        for (Paper p : papers) {
            if (p.external) continue;
            List<String> cycle = findCycle(p.folder, byFolder);
            if (cycle != null)
                is.add(new Issue("跨-20", p.folder, "循環引用：" + String.join(" → ", cycle)));
        }
        return is;
    }

    /** 概念表定義中不得出現的固定字眼（概-14 的工具部分） */
    static final List<String> CONTEXT_WORDS = List.of("如上", "前述", "上述", "本文第");

    /** 連結檢查的代碼：中文論文與英文翻譯分屬不同條目 */
    record LinkCodes(String which, String format, String uuid, String anchor, String display,
                     String heading, String placeholderUp, String placeholderLeft) {}

    static final LinkCodes ZH_CODES = new LinkCodes("中文", "論-07", "論-08", "論-09", "論-10", "論-11", "論-12", "論-13");
    static final LinkCodes EN_CODES = new LinkCodes("英文", "譯-03", "譯-03", "譯-03", "譯-05", "譯-04", "譯-03", "譯-03");

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

    static void checkFormalization(List<Issue> is, Paper p, Map<String, Paper> byFolder, String zhBase) {
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
        for (String tok : PREFIXED_REF.matcher(proseText).replaceAll(" ").split(SPLITTER)) {
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

        // 形-29、形-30、形-33（工具部分）：待決項
        List<String> keys = new ArrayList<>(secs.keySet());
        if (keys.isEmpty() || !keys.get(keys.size() - 1).equals("待決項"))
            is.add(new Issue("形-29", w, "文件末尾缺少「待決項」區塊"));
        else if (!pendingEmpty(p.formText))
            is.add(new Issue("形-30", w, "論證層待決項未清零，不得進入稽核"));
        for (String s : sources)
            if (s.contains("待決項")) is.add(new Issue("形-33", w, "待決項不得作為推導來源：" + s));
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
            "依稽核總表，以下條目交由 AI 稽核（標 * 者為「工具＋AI」，只查工具未涵蓋的部分）：\n");
        for (String pf : PREFIXES) {
            List<String> ai = new ArrayList<>();
            for (var e : table.entrySet())
                if (e.getKey().startsWith(pf + "-")) {
                    if (e.getValue().startsWith("AI")) ai.add(e.getKey());
                    else if (e.getValue().startsWith("工具＋AI")) ai.add(e.getKey() + "*");
                }
            if (!ai.isEmpty()) sb.append("  ").append(pf).append("　").append(String.join("、", ai)).append('\n');
        }
        List<String> author = table.entrySet().stream()
            .filter(e -> e.getValue().startsWith("作者")).map(Map.Entry::getKey).toList();
        if (!author.isEmpty())
            sb.append("作者把關（工具不追蹤定稿狀態與版本歷史）：").append(String.join("、", author)).append('\n');
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
            String raw = prose[i].trim();
            if (raw.startsWith("## ")) { sec = raw.substring(3).trim(); continue; }
            if (raw.isEmpty() || raw.startsWith("#") || sec == null
                || sec.equals("記號約定") || sec.equals("待決項")) continue;
            String line = raw.startsWith("- ") ? raw.substring(2).trim() : raw;
            if (line.startsWith("來源：") || line.startsWith("[作者裁決]") || line.startsWith("附註：")) continue;

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

    /** 刪除所有論文資料夾中的舊輸入包；validate 未通過時不會留下過期的包 */
    static void clearBundles(List<Paper> papers) throws IOException {
        for (Paper p : papers) {
            if (p.external) continue;
            Files.deleteIfExists(p.dir.resolve(FORM_BUNDLE));
            Files.deleteIfExists(p.dir.resolve(AUDIT_BUNDLE));
        }
    }

    static void bundle(Path root, List<Paper> papers, List<Issue> warnings) throws IOException {
        Map<String, Paper> byFolder = byFolder(papers);
        String manual = readIfExists(doc(root, FORM_MANUAL));
        String audit = readIfExists(doc(root, AUDIT_TABLE));
        Map<String, String> table = loadAuditTable(root);
        if (manual.isBlank())
            System.out.println("✗ 找不到 " + DOCS_DIR + "/" + FORM_MANUAL + "，不產生形式化輸入包");
        if (audit.isBlank())
            System.out.println("✗ 找不到 " + DOCS_DIR + "/" + AUDIT_TABLE + "，不產生稽核輸入包");

        int nf = 0, na = 0;
        for (Paper p : papers) {
            if (p.external || p.zhPaper == null) continue;
            int[] count = new int[1];
            String upstream = upstreamSection(p, byFolder, count);

            // 形式化輸入包（缺手冊時不產生，以免 AI 在缺少規則的情況下形式化）
            if (!manual.isBlank()) {
                StringBuilder sb = new StringBuilder();
                sb.append("# 形式化輸入包\n\n")
                  .append("本檔案包含形式化任務所需的全部材料。任何不在此列的上游概念都視為缺失，\n")
                  .append("發現時應回報，不得自行推測其定義。標為 private 的上游概念只作為理解定義的脈絡，\n")
                  .append("不得在本篇中引用。\n\n---\n\n");
                sb.append("# 形式化手冊\n\n").append(manual)
                  .append("\n\n---\n\n").append(upstream);
                sb.append("---\n\n# 本篇概念表\n\n").append(p.conceptsText)
                  .append("\n\n---\n\n# 本篇論文\n\n").append(p.zhText).append('\n');
                Files.writeString(p.dir.resolve(FORM_BUNDLE), sb.toString(), StandardCharsets.UTF_8);
                System.out.println("  [ok] " + p.folder + "/" + FORM_BUNDLE + "（上游概念 " + count[0] + " 條）");
                nf++;
            }

            // 稽核輸入包：四個產物都完成才產生（validate 已通過，兩個待決項區塊必為「無」）
            if (audit.isBlank()) continue;
            String notReady = !p.formStarted() ? "形式化尚未開始"
                            : !p.enStarted() ? "英文翻譯尚未開始" : null;
            if (notReady != null) {
                System.out.println("  [略過] " + p.folder + "/" + AUDIT_BUNDLE + "：" + notReady);
                continue;
            }
            StringBuilder ab = new StringBuilder();
            ab.append("# 稽核輸入包\n\n")
              .append("本檔案包含稽核任務所需的全部材料：稽核總表、工具檢查結果、上游概念、本篇四個檔案。\n")
              .append("依稽核總表第零節執行。任何不在此列的上游概念都視為缺失，不得自行推測其定義。\n\n---\n\n");
            ab.append("# 稽核總表\n\n").append(audit)
              .append("\n\n---\n\n# 工具檢查結果\n\n")
              .append("validate 全部通過。檢查者為「工具」的條目一律記為「通過（工具）」。\n\n");
            List<Issue> mine = warnings.stream().filter(i -> i.where.startsWith(p.folder)).toList();
            if (mine.isEmpty()) ab.append("本篇沒有警告。\n\n");
            else {
                ab.append("本篇的警告（工具無法判斷，須在對應的「工具＋AI」條目中確認）：\n\n");
                for (Issue i : mine) ab.append("- [").append(i.code).append("] ").append(i.detail).append('\n');
                ab.append('\n');
            }
            if (!table.isEmpty()) ab.append("```text\n").append(auditScope(table)).append("```\n\n");
            ab.append("---\n\n").append(upstream);
            ab.append("---\n\n# 本篇檔案\n\n");
            for (Path f : new Path[]{p.zhPaper, p.enPaper, p.conceptsFile, p.formalizationFile}) {
                ab.append("## 檔案：").append(f.getFileName()).append("\n\n")
                  .append(Files.readString(f, StandardCharsets.UTF_8)).append("\n\n");
            }
            Files.writeString(p.dir.resolve(AUDIT_BUNDLE), ab.toString(), StandardCharsets.UTF_8);
            System.out.println("  [ok] " + p.folder + "/" + AUDIT_BUNDLE);
            na++;
        }
        System.out.println("產出 " + nf + " 份形式化輸入包、" + na + " 份稽核輸入包");
    }

    /** 規範文件的路徑 */
    static Path doc(Path root, String name) {
        return root.resolve(DOCS_DIR).resolve(name);
    }

    static String readIfExists(Path f) throws IOException {
        return Files.exists(f) ? Files.readString(f, StandardCharsets.UTF_8) : "";
    }

    /** 本篇引用到的上游概念，遞移擷取（定義所連結的概念一併帶入，private 條目作為脈絡） */
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
        StringBuilder sb = new StringBuilder("# 上游概念\n\n");
        if (need.isEmpty()) sb.append("（本篇無上游引用）\n\n");
        for (var e : need.entrySet()) {
            Paper up = byFolder.get(e.getKey());
            sb.append("## ").append(e.getKey())
              .append(up != null && up.title != null ? "　《" + up.title + "》" : "").append("\n\n");
            for (String id : e.getValue()) {
                Concept c = up == null ? null : up.byId(id);
                if (c == null) { sb.append("（找不到 ").append(id).append("）\n\n"); continue; }
                sb.append("### ").append(e.getKey()).append(" #").append(id)
                  .append("　").append(c.mod).append(" ").append(c.zh).append(" / ").append(c.en).append("\n\n")
                  .append("- 定義：").append(c.def == null ? "" : c.def).append("\n\n");
                count[0]++;
            }
        }
        return sb.toString();
    }

    // ════════════════════════════════════════════════════════════
    // 3. collect
    // ════════════════════════════════════════════════════════════

    static void collect(Path root, List<Paper> papers) throws IOException {
        Path outDir = root.resolve("formalizations");
        Files.createDirectories(outDir);
        int n = 0;
        for (Paper p : papers) {
            if (p.formalizationFile == null) continue;
            String name = p.title != null ? p.title : p.folder;
            Files.copy(p.formalizationFile, outDir.resolve("formalization_" + name + ".md"),
                StandardCopyOption.REPLACE_EXISTING);
            n++;
        }
        System.out.println("彙整 " + n + " 份到 " + outDir.getFileName());
    }

    // ════════════════════════════════════════════════════════════
    // 4. tree
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
    // 5. starmap
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
    // 6. concepts.jsonl
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
    // 7. papers.jsonl
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
                sb.append("\"concepts_md\":").append(json(p.conceptsText)).append(",");
                sb.append("\"formalization_md\":").append(json(p.formText)).append(",");
                sb.append("\"paper_files\":[");
                boolean first = true;
                for (Object[] f : new Object[][]{{p.zhPaper, p.zhText}, {p.enPaper, p.enText}}) {
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
    // 8. checkPapers
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
    // 9. checkHf
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
