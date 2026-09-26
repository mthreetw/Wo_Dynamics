package tools;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 論文註冊（說明文件 4.1 節）
 *
 * 直接執行即可，不需要任何輸入。產生 UUID v4，並從模板建立論文資料夾與四個檔案。
 * 兩個論文檔名是標題佔位符（「中文標題」「English Title」），由 AI 在撰寫過程中替換；
 * 佔位符未替換時，ToolRunner 的 validate 會以論-04、譯-01 攔下。
 * 時間戳取自本機時鐘。
 */
public class NewPaper {

    public static void main(String[] args) throws IOException {
        Path root = ToolRunner.resolveRoot();
        Path src = root.resolve(ToolRunner.SRC_DIR);
        if (!Files.isDirectory(src)) {
            System.out.println("找不到 " + src + "，結束。");
            return;
        }

        String uuid = UUID.randomUUID().toString();
        Path dir = src.resolve("paper_" + uuid.replace('-', '_'));
        if (Files.exists(dir)) {
            System.out.println("✗ 資料夾已存在：" + dir.getFileName() + "，未建立任何檔案。");
            return;
        }

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        String zh = ToolRunner.ZH_TITLE_PLACEHOLDER, en = ToolRunner.EN_TITLE_PLACEHOLDER;
        String yaml = "---\nuuid: " + uuid + "\nlast-modified: " + now + "\n---\n\n";

        Files.createDirectories(dir);
        write(dir.resolve(zh + ".md"), yaml + "# " + zh + "\n\n");
        write(dir.resolve(en + ".md"), yaml + "# " + en + "\n\n");
        write(dir.resolve("concepts.md"), yaml
            + "## 不登記\n\n無\n\n"
            + "## 待決項\n\n無\n");
        write(dir.resolve("formalization.md"),
            "論文：《" + zh + "》\n"
            + "UUID：" + uuid + "\n\n"
            + "## 記號約定\n\n來源：論文全文\n\n無\n\n"
            + "## 待決項\n\n無\n");

        System.out.println("✓ 已建立 " + dir.getFileName());
        System.out.println("  UUID：" + uuid);
        System.out.println("\n中文標題佔位符出現在三處，撰寫時由 AI 一併替換：");
        System.out.println("  中文論文的檔名與一級標題、formalization.md 第一行的論文標題。");
        System.out.println("英文標題佔位符出現在兩處，翻譯時替換：英文翻譯的檔名與一級標題。");
    }

    static void write(Path f, String text) throws IOException {
        Files.writeString(f, text, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
    }
}
