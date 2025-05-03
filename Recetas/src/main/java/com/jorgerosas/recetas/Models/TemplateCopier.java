package com.jorgerosas.recetas.Models;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class TemplateCopier {

    private static final String RESOURCE_FOLDER = "HtmlCompliments";

    /**
     * Copies all files from the classpath folder HtmlCompliments into
     * {destinationDir}/savedrecipes, handling both IDE (file://) and JAR (jar://) modes.
     *
     * @param destinationDir the external base directory; "savedrecipes" is created inside it
     * @throws IOException        on I/O errors
     * @throws URISyntaxException on malformed resource URL
     */
    public static void copyTemplates(String destinationDir) throws IOException, URISyntaxException {
        // Prepare target directory: {destinationDir}/savedrecipes
        Path targetDir = Paths.get(destinationDir, "Html");
        if (Files.notExists(targetDir)) {
            Files.createDirectories(targetDir);  // create parent dirs if needed :contentReference[oaicite:3]{index=3}
        }

        // Locate the resource folder on the classpath
        URL url = Thread.currentThread()
                .getContextClassLoader()
                .getResource(RESOURCE_FOLDER);
        Objects.requireNonNull(url, "Resource folder not found: " + RESOURCE_FOLDER);

        String protocol = url.getProtocol();

        if ("file".equals(protocol)) {
            // Development mode: resources are exploded on the filesystem
            Path resourcePath = Paths.get(url.toURI());
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(resourcePath)) {
                for (Path file : stream) {
                    if (Files.isRegularFile(file)) {
                        // Copy directly from the file system
                        Path dest = targetDir.resolve(file.getFileName());
                        Files.copy(file, dest, StandardCopyOption.REPLACE_EXISTING);  // simple NIO copy :contentReference[oaicite:4]{index=4}
                    }
                }
            }

        } else if ("jar".equals(protocol)) {
            // Production mode: resources are inside the running JAR
            JarURLConnection jarConn = (JarURLConnection) url.openConnection();
            try (JarFile jar = jarConn.getJarFile()) {
                String entryPrefix = jarConn.getEntryName() + "/";  // e.g. "HtmlCompliments/"
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (name.startsWith(entryPrefix) && !entry.isDirectory()) {
                        // Extract file name and copy via stream
                        String filename = Paths.get(name).getFileName().toString();
                        Path outFile = targetDir.resolve(filename);
                        try (InputStream in = jar.getInputStream(entry)) {
                            Files.copy(in, outFile, StandardCopyOption.REPLACE_EXISTING);  // stream-to-file copy :contentReference[oaicite:5]{index=5}
                        }
                    }
                }
            }

        } else {
            throw new UnsupportedOperationException("Unsupported protocol: " + protocol);
        }
    }
}
