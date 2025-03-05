package com.jorgerosas.recetas.Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class PrescriptionBuilder {

    private String html;
    private final String path;

    public PrescriptionBuilder(String path){
        this.path=path;
    }

    public void replaceDataShort(String patient, String date, String description){

        html=html.replace("{{patient}}", patient);
        html=html.replace("{{date}}", date);
        html=html.replace("{{description}}", description);

    }

    public String replaceData(String html, String patient, String date, String description){
        StringBuilder filledhtml =new StringBuilder();
        filledhtml.append(html);

        return filledhtml.toString();
    }

    public void readHtmlFromResources() {   //Converts html file to java String

        StringBuilder content = new StringBuilder();
        String line;

        try (InputStream inputStream = PrescriptionBuilder.class.getResourceAsStream(this.path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            while ((line = reader.readLine()) != null) {content.append(line).append("\n");}

        } catch (IOException | NullPointerException e) {
            System.out.println(e); }

        this.html = content.toString();
    }

    public String getHtml() {
        return html;
    }

    public static void main(String[] args) {

        PrescriptionBuilder pb = new PrescriptionBuilder("/templates/UC/UC.html");
        pb.readHtmlFromResources();
        pb.replaceDataShort("Jorge Rosas", "12/12/12", "Descripción");
        String htmlContent = pb.getHtml();

        if (htmlContent != null) {
            System.out.println(htmlContent);
        } else {
            System.out.println("Archivo no encontrado");
        }
    }
}
