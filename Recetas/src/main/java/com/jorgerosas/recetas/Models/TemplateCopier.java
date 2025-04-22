package com.jorgerosas.recetas.Models;

import com.jorgerosas.recetas.AppConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class TemplateCopier {

    public static void main() {
        try {
            copyTemplates();
            System.out.println("HtmlCompliments copied successfully.");
        } catch (IOException e) {
            System.err.println("Error copying templates: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Copies the templates from the resources folder to the destination directory.
     *
     * @throws IOException if any I/O error occurs
     */
    public static void copyTemplates() throws IOException {

        String baseDir = AppConfig.getInstance().getBaseDirectory();
        Path destDir = Paths.get(baseDir, "templates" + File.separator + "UC");

        Files.createDirectories(destDir);

        // Try to get the resource URL for "templates" (the folder in src/main/resources)
        ClassLoader cl = TemplateCopier.class.getClassLoader();
        URL url = cl.getResource("HtmlCompliments");
        if (url == null) {
            throw new IllegalArgumentException("HtmlCompliments folder not found in resources.");
        }

        // Check the URL protocol: jar or file system.
        if (url.getProtocol().equals("jar")) {
            copyTemplatesFromJar(url, destDir);
        } else if (url.getProtocol().equals("file")) {
            copyTemplatesFromFile(url, destDir);
        } else {
            throw new UnsupportedOperationException("Unsupported protocol: " + url.getProtocol());
        }
    }

    /**
     * Copies files from the "templates" folder when running from a jar.
     *
     * @param url the resource URL for the templates folder
     * @param destDir the destination directory
     * @throws IOException if any I/O error occurs
     */
    private static void copyTemplatesFromJar(URL url, Path destDir) throws IOException {
        // Open a connection to the jar file
        JarURLConnection jarConnection = (JarURLConnection) url.openConnection();
        JarFile jarFile = jarConnection.getJarFile();

        // Iterate over all entries in the jar
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();
            // Filter entries under "templates/" and that are not directories
            if (entryName.startsWith("HtmlCompliments/") && !entry.isDirectory()) {
                // Remove the "templates/" prefix to get the relative path
                String relativePath = entryName.substring("HtmlCompliments/".length());
                Path targetPath = destDir.resolve(relativePath);
                Files.createDirectories(targetPath.getParent());

                // Open an InputStream for the resource and copy to target location
                try (InputStream is = TemplateCopier.class.getClassLoader().getResourceAsStream(entryName)) {
                    if (is == null) {
                        throw new IOException("Unable to get resource stream for " + entryName);
                    }
                    Files.copy(is, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    /**
     * Copies files from the "templates" folder when resources are available on the file system.
     *
     * @param url the resource URL for the templates folder
     * @param destDir the destination directory
     * @throws IOException if any I/O error occurs
     */
    private static void copyTemplatesFromFile(URL url, Path destDir) throws IOException {
        // Convert URL to File and verify it's a directory
        File templatesDir = new File(url.getPath());
        if (!templatesDir.isDirectory()) {
            throw new IllegalArgumentException("Resource 'templates' is not a directory.");
        }
        // Recursively copy files from the templates directory to destDir
        copyDirectory(templatesDir.toPath(), destDir, templatesDir.toPath());
    }

    /**
     * Recursively copies all files from a source directory to a destination.
     *
     * @param sourceDir the source directory to copy (could be deep in a tree)
     * @param destDir the final destination directory where files will be placed
     * @param baseDir the base directory to relativize paths (usually the root templates folder)
     * @throws IOException if an I/O error occurs
     */
    private static void copyDirectory(Path sourceDir, Path destDir, Path baseDir) throws IOException {
        Files.walk(sourceDir)
                .filter(path -> !Files.isDirectory(path))
                .forEach(sourcePath -> {
                    try {
                        // Compute the relative path from the base templates folder
                        Path relativePath = baseDir.relativize(sourcePath);
                        Path targetPath = destDir.resolve(relativePath);
                        Files.createDirectories(targetPath.getParent());
                        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        System.err.println("Failed to copy file: " + sourcePath + " - " + e.getMessage());
                    }
                });
    }
}
