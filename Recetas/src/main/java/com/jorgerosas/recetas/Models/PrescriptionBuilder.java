package com.jorgerosas.recetas.Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class PrescriptionBuilder {

    private String html;
    private String path;

    public PrescriptionBuilder(String path){
        this.path=path;
    }

    public static void main(String[] args) {

        PrescriptionBuilder pb = new PrescriptionBuilder();
        String htmlContent = pb.readHtmlFromResources("/templates/UC/UC.html");


        if (htmlContent != null) {
            System.out.println(htmlContent);
        } else {
            System.out.println("Archivo no encontrado");
        }
    }

    public String replaceDataShort(String html, String patient, String date, String description){

        html=html.replace("{{patient}}", patient);
        html=html.replace("{{date}}", date);
        html=html.replace("{{description}}", description);

        return html;
    }

    public String replaceData(String html, String patient, String date, String description){
        StringBuilder filledhtml =new StringBuilder();
        filledhtml.append(html);

        return filledhtml.toString();
    }

    public String readHtmlFromResources(String filePath) {   //Converts html file to java String

        StringBuilder content = new StringBuilder();
        String line;

        try (InputStream inputStream = PrescriptionBuilder.class.getResourceAsStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

        } catch (IOException | NullPointerException e) { return null; }

        return content.toString();
    }
}
