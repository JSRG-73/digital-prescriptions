package com.jorgerosas.recetas.Models;

import com.jorgerosas.recetas.AppConfig;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class JsonReader {
    private String name;
    private String description;

    public static void main(String[] args) {
        JsonReader reader = new JsonReader();

        String baseDir = AppConfig.getInstance().getBaseDirectory();
        baseDir += File.separator +"templates"+ File.separator +"UC" + File.separator;

        boolean success = reader.readJsonFile(baseDir+"test_2023-08-21.json");

        if(success) {
            System.out.println("Name: " + reader.name);
            System.out.println("Description: " + reader.description);
        }
    }

    public boolean readJsonFile(String filePath) {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            // Parse JSON file
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            // Extract values
            this.name = (String) jsonObject.get("name");
            this.description = (String) jsonObject.get("description");

            return true;
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
        } catch (Exception e) {
            System.err.println("Error reading JSON: " + e.getMessage());
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}