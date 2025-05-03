package com.jorgerosas.recetas.Models;

import java.io.File;
import java.util.List;

public class FileDeleter {

    public static void deleteFiles(List<String> fileNames, String directoryPath) {
        for (String fileName : fileNames) {
            File file = new File(directoryPath, fileName);
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    System.out.println("Deleted: " + file.getAbsolutePath());
                } else {
                    System.out.println("Failed to delete: " + file.getAbsolutePath());
                }
            } else {
                System.out.println("File not found: " + file.getAbsolutePath());
            }
        }
    }
}