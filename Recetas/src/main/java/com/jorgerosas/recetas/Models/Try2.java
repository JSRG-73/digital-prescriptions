package com.jorgerosas.recetas.Models;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Try2 {
    public static void main(String[] args) {
        // Set ChromeDriver path (update with your actual path)
        // System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");  // New headless mode
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");

        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get("file:///D:/Projects/DrBarragan/templates/UC/UC-1.html");

            // Create print options map
            Map<String, Object> printParams = new HashMap<>();
            printParams.put("landscape", false);
            printParams.put("displayHeaderFooter", false);
            printParams.put("printBackground", true);
            printParams.put("preferCSSPageSize", true);

            // Execute Chrome DevTools Protocol command
            Map<String, Object> result = ((ChromeDriver) driver)
                    .executeCdpCommand("Page.printToPDF", printParams);

            // Decode and save PDF
            byte[] pdfBytes = Base64.getDecoder().decode((String) result.get("data"));
            Files.write(Paths.get("output.pdf"), pdfBytes);
            System.out.println("PDF successfully saved as output.pdf");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
} 
