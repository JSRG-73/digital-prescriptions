package com.jorgerosas.recetas.Models;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.Pdf;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.print.PrintOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class PdfGenerator {

    public boolean generatePdfFromHtml(String htmlFilePath, String outputPdfPath) {
        WebDriver driver = null;

        try {
            // Prepare Chrome options for headless PDF generation
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");

            // Setup ChromeDriver
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver(options);

            // Prepare file URL (ensure proper file protocol formatting)
            File htmlFile = new File(htmlFilePath);
            String fileUrl = "file:///" + htmlFile.getAbsolutePath().replace("\\", "/");

            // Navigate to the HTML file
            driver.get(fileUrl);

            // Wait a moment for rendering (adjust as needed)
            Thread.sleep(1000);

            // Print to PDF
            Pdf pdf = ((ChromeDriver) driver).print(new PrintOptions());

            // Write PDF to file
            Path outputPath = Paths.get(outputPdfPath);
            Files.createDirectories(outputPath.getParent());

            // Convert Base64 PDF content to bytes and save
            byte[] pdfContent = OutputType.BYTES.convertFromBase64Png(pdf.getContent());
            Files.write(outputPath, pdfContent);

            return true;
        } catch (Exception e) {
            // Log the full exception for debugging
            e.printStackTrace();
            return false;
        } finally {
            // Ensure driver is closed
            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Utility method to resolve paths relative to the application's resources
     *
     * @param resourcePath Path relative to resources folder
     * @return Absolute path to the resource
     */
    public String getResourcePath(String resourcePath) {
        try {
            // Try to get resource from classpath
            File file = new File(getClass().getClassLoader().getResource(resourcePath).toURI());
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates a copy of the HTML template in a temporary location
     * Useful for modifications or dynamic content generation
     *
     * @param sourceTemplatePath Path to the source HTML template
     * @return Path to the temporary copy of the template
     */
    public String prepareTempHtmlTemplate(String sourceTemplatePath) {
        try {
            // Create a temporary file
            Path tempFile = Files.createTempFile("pdf_template_", ".html");

            // Copy the source template to the temporary location
            Files.copy(
                    Paths.get(sourceTemplatePath),
                    tempFile,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return tempFile.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Example usage method
     */
    public static void main(String[] args) {
        PdfGenerator generator = new PdfGenerator();

        // Example of usage
        String htmlTemplatePath = "templates/report_template.html";
        String outputPdfPath = System.getProperty("user.home") + "/Desktop/generated_report.pdf";

        boolean success = generator.generatePdfFromHtml(
                generator.getResourcePath(htmlTemplatePath),
                outputPdfPath
        );

        System.out.println("PDF Generation " + (success ? "Successful" : "Failed"));
    }
}