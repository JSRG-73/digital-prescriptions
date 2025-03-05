package com.jorgerosas.recetas;

import java.nio.file.*;
import java.io.IOException;

public class PrescriptionApplication {

    public static String generateHTML(String templatePath, String name, String date, String amount) throws IOException {
        // Read HTML template as a String
        String html = new String(Files.readAllBytes(Paths.get(templatePath)));

        // Replace placeholders with actual values
        html = html.replace("{{patient}}", name)
                .replace("{{date}}", date)
                .replace("{{description}}", amount);

        return html;
    }
    public static void main(String[] args){
        String templatePath = "folder/UC.html";
        try{
            System.out.println(generateHTML(templatePath, "Jorge Rosas", "12/02/25","Descripción"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
