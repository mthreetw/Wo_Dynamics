package tools;

public class UUID_maker {
	 public static void main(String[] args) throws Exception {
	        String id = "paper_" + java.util.UUID.randomUUID().toString().replace("-", "_");
	        java.nio.file.Path dir = java.nio.file.Path.of("src/main/java", id);
	        java.nio.file.Files.createDirectories(dir);
	        java.nio.file.Files.writeString(dir.resolve("package-info.java"),
	            "/** 論文：" + (args.length > 0 ? args[0] : "未命名") + " */\npackage " + id + ";\n");
	        System.out.println("已建立 " + id);
	    }
}
