package com.jorgerosas.recetas.Models;

import java.io.*;
import java.nio.charset.StandardCharsets;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Pdf;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.print.PrintOptions;
import java.time.Duration;
import io.github.bonigarcia.wdm.WebDriverManager;

public class PrescriptionBuilder {

    private String html;
    private final String path;

    public PrescriptionBuilder(String path) {
        this.path = path;
    }

    public void replaceDataShort(String patient, String date, String description) {
        html = html.replace("{{patient}}", patient);
        html = html.replace("{{date}}", date);
        html = html.replace("{{description}}", description);
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

    public void generateHTML(String filePath) {
        try {
            File dir = new File(filePath).getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(html);
                System.out.println("HTML file generated successfully at: " + filePath);
            }
        } catch (IOException e) {
            System.out.println("Error writing HTML file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        PrescriptionBuilder pb = new PrescriptionBuilder("/templates/UC/UC.html");
        pb.readHtmlFromResources();
        pb.replaceDataShort("Jorge Rosas", "12/12/12", "Descripción");

        
        // Define file paths for the generated HTML and PDF
        String htmlFilePath = "templates/UC/UC-1.html";
        String pdfFilePath = "templates/UC/UC-1.pdf";

        // Generate HTML 
        pb.generateHTML(htmlFilePath);

        // Generate PDF
        //pb.generatePDF(pdfFilePath);
        
    }
}
