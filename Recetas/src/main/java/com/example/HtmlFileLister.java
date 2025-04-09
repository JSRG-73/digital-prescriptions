package com.example;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HtmlFileLister {
    public static void main(String[] args) {
        try {
            // Get reference to the templates directory
            URL templatesUrl = HtmlFileLister.class.getClassLoader().getResource("templates");

            if (templatesUrl == null) {
                System.out.println("❌ Templates directory not found in resources!");
                return;
            }

            System.out.println("🔍 Found templates at: " + templatesUrl);

            // Convert URL to file path
            Path templatesPath = Paths.get(templatesUrl.toURI());
            System.out.println("📂 Absolute path: " + templatesPath.toAbsolutePath());

            // Walk through all files
            Files.walk(templatesPath)
                    .filter(HtmlFileLister::isHtmlFile)
                    .forEach(path -> {
                        String relativePath = templatesPath.relativize(path).toString();
                        System.out.println("✅ Found HTML file: " + relativePath);
                    });

        } catch (Exception e) {
            System.out.println("🚨 Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean isHtmlFile(Path path) {
        String fileName = path.getFileName().toString();
        return fileName.endsWith(".html") || fileName.endsWith(".htm");
    }
}