package com.jorgerosas.recetas;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AppConfig {
    private static AppConfig instance;
    private String baseDirectory;

    // Private constructor to prevent direct instantiation
    private AppConfig() {
        initializeBaseDirectory();
    }

    // Thread-safe singleton initialization
    public static synchronized AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    // Initialize the base directory based on OS
    private void initializeBaseDirectory() {
        String osName = System.getProperty("os.name").toLowerCase();
        String userHome = System.getProperty("user.home");

        if (osName.contains("win")) {
            baseDirectory = Paths.get(userHome, "AppData", "Recetas Digitales").toString();
        } else if (osName.contains("mac")) {
            baseDirectory = Paths.get(userHome, "Library", "Application Support", "Recetas Digitales").toString();
        } else {
            // Linux/Unix/other
            baseDirectory = Paths.get(userHome, ".recetas_digitales").toString();
        }
    }

    // Getter for the base directory path
    public String getBaseDirectory() {
        return baseDirectory;
    }

    // Optional: Get as a `Path` object for file operations
    public Path getBaseDirectoryPath() {
        return Paths.get(baseDirectory);
    }

    // Optional: Ensure the directory exists
    public void ensureDirectoryExists() throws IOException {
        Path path = getBaseDirectoryPath();
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }
}