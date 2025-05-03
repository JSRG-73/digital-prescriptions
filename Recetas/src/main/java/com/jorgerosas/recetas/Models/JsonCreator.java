package com.jorgerosas.recetas.Models;

import com.jorgerosas.recetas.AppConfig;
import org.json.simple.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonCreator {
    // Set your path here (no trailing slash needed)
    private static String BASE_DIRECTORY = "";

    public static void main(String[] args) {
        JsonCreator generator = new JsonCreator();
        boolean success = generator.createJsonFile("test", "2023-08-21", "Simple example");
        System.out.println("File creation " + (success ? "succeeded" : "failed"));
    }

    public JsonCreator(){
        String baseDir = AppConfig.getInstance().getBaseDirectory();
        baseDir += File.separator +"savedrecipes";
        this.BASE_DIRECTORY=baseDir;
    }

    public boolean createJsonFile(String name, String date, String description) {
        try {


            // Create filename
            String fileName = FilterFileName.safeFilename(name + " " +date, "json");

            // Create directory if needed
            Files.createDirectories(Paths.get(BASE_DIRECTORY));

            // Build JSON
            JSONObject json = new JSONObject();
            json.put("name", name);
            json.put("description", description);

            // Write to file
            try (FileWriter file = new FileWriter(BASE_DIRECTORY + File.separator + fileName)) {
                file.write(json.toJSONString());
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }

    private String sanitize(String input) {
        return input.replaceAll("[^\\p{L}0-9-._ Filter]", "_")
                .replaceAll("_+", "_");
    }
}