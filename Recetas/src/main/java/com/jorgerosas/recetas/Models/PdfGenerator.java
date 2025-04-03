package com.jorgerosas.recetas.Models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.aayushatharva.brotli4j.Brotli4jLoader;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Pdf;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.print.PrintOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

public class PdfGenerator {

    static {
        try {
            Brotli4jLoader.ensureAvailability();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load Brotli native libraries", e);
        }
    }

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

            System.out.println("Loading HTML from: " + fileUrl);

            // Navigate to the HTML file
            driver.get(fileUrl);

            // Wait a moment for rendering (adjust as needed)
            Thread.sleep(1000);

            // Print to PDF
            PrintOptions printOptions = new PrintOptions();
            //printOptions.setPageSize(org.openqa.selenium.print.PrintOptions.Size.A4);
            Pdf pdf = ((ChromeDriver) driver).print(printOptions);

            // Write PDF to file
            Path outputPath = Paths.get(outputPdfPath);
            Files.createDirectories(outputPath.getParent());

            // Convert Base64 PDF content to bytes and save
            byte[] pdfContent = OutputType.BYTES.convertFromBase64Png(pdf.getContent());
            Files.write(outputPath, pdfContent);

            System.out.println("PDF successfully generated at: " + outputPdfPath);
            return true;
        } catch (Exception e) {
            // Log the full exception for debugging
            System.err.println("PDF generation failed: " + e.getMessage());
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
     * @param resourcePath Path relative to resources folder
     * @return Absolute path to the resource (either as a file or extracted temp file)
     * @throws IOException if resource cannot be found or accessed
     */
    public String getResourcePath(String resourcePath) throws IOException {
        // First try to get as URL from class loader
        URL resourceUrl = getClass().getClassLoader().getResource(resourcePath);

        // If resource was found as URL
        if (resourceUrl != null) {
            try {
                // Try to convert to file path directly (works for non-JAR resources)
                File file = new File(resourceUrl.toURI());
                System.out.println("Resource found directly: " + file.getAbsolutePath());
                return file.getAbsolutePath();
            } catch (Exception e) {
                // For resources inside JAR or other non-file URLs, extract to temp file
                try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
                    if (inputStream != null) {
                        // Extract to temporary file
                        String fileName = Paths.get(resourcePath).getFileName().toString();
                        Path tempFile = Files.createTempFile("resource_", fileName);
                        Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("Resource extracted to: " + tempFile);
                        return tempFile.toString();
                    }
                }
            }
        }

        // Alternative approach: try direct file path in case it's not a classpath resource
        // but a file in the file system
        File directFile = new File(resourcePath);
        if (directFile.exists() && directFile.isFile()) {
            System.out.println("Resource found as direct file: " + directFile.getAbsolutePath());
            return directFile.getAbsolutePath();
        }

        // If we got here, resource wasn't found
        throw new FileNotFoundException("Resource not found: " + resourcePath);
    }

    /**
     * Creates a copy of the HTML template in a temporary location
     * Useful for modifications or dynamic content generation
     *
     * @param sourceTemplatePath Path to the source HTML template
     * @return Path to the temporary copy of the template
     * @throws IOException if template cannot be copied
     */
    public String prepareTempHtmlTemplate(String sourceTemplatePath) throws IOException {
        // Create a temporary file
        Path tempFile = Files.createTempFile("pdf_template_", ".html");

        // Copy the source template to the temporary location
        Files.copy(
                Paths.get(sourceTemplatePath),
                tempFile,
                StandardCopyOption.REPLACE_EXISTING
        );

        System.out.println("Template copied to: " + tempFile);
        return tempFile.toString();
    }

    /**
     * Example usage method with improved error handling
     */
    public static void main(String[] args) {
        PdfGenerator generator = new PdfGenerator();

        try {
            // IMPORTANT: Adjust this path to match your project structure
            String htmlTemplateResource = "templates/UC/UC-1.html";

            // Get path to the template
            String htmlFilePath = generator.getResourcePath(htmlTemplateResource);

            // Set output path
            String outputPdfPath = System.getProperty("user.home") + "/Desktop/UC-1.pdf";

            // Generate PDF
            boolean success = generator.generatePdfFromHtml(htmlFilePath, outputPdfPath);

            System.out.println("PDF Generation " + (success ? "Successful" : "Failed"));

        } catch (Exception e) {
            System.err.println("Error in main: " + e.getMessage());
            e.printStackTrace();

        }
    }
}