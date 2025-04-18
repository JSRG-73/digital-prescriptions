package com.jorgerosas.recetas.Models;

import java.io.*;
import java.nio.charset.StandardCharsets;

import com.jorgerosas.recetas.AppConfig;
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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.stream.Collectors;

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



    public void saveHtml(String htmlContent, String filename) throws IOException {

        String baseDir = AppConfig.getInstance().getBaseDirectory();
        Path outputPath = Paths.get(baseDir, "templates"+File.separator+"UC", filename);

        Files.createDirectories(outputPath.getParent());

        Files.write(outputPath, htmlContent.getBytes());

        System.out.println("HTML saved to: " + outputPath.toAbsolutePath());
    }


    public static void main(String[] args) throws IOException {

        String baseDir = AppConfig.getInstance().getBaseDirectory()+"/templates/UC/UC.html";

        PrescriptionBuilder pb = new PrescriptionBuilder("/templates/UC/UC.html");
        pb.readHtmlFromResources();
        pb.replaceDataShort("Pedro Pérez", "12/12/12", "Descripción");
        System.out.println("String: "+baseDir);

        String htmlFilePath = baseDir+"/templates/UC/alic.html";

        // Generate HTML 
        pb.saveHtml(pb.getHtml(),"cine.html");

    }
}