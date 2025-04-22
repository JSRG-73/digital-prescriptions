package com.jorgerosas.recetas;

import javafx.application.Application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Test {

    public static void createJsonFile(String key, String value, String folderPath, String fileName) {
        // Create the JSON content as a simple string
        String jsonContent = "{\n  \"" + key + "\": \"" + value + "\"\n}";

        try {
            // Ensure the folder exists
            Path folder = Paths.get(folderPath);

            // Create the full path to the file
            Path filePath = folder.resolve(fileName);

            // Write the JSON string to the file
            try (FileWriter writer = new FileWriter(filePath.toFile())) {
                writer.write(jsonContent);
                System.out.println("JSON file created at: " + filePath);
            }
        } catch (IOException e) {
            System.err.println("Error creating JSON file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        String baseDir = AppConfig.getInstance().getBaseDirectory();
        baseDir += File.separator +"templates"+ File.separator +"UC"+ File.separator;

        //File myObj = new File(baseDir+"Carlos Alberto Castro Jiménez 12-abril-2025.html");
        //myObj.delete();


    }
}