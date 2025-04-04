package com.jorgerosas.recetas.Models;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
public class Try3 {
    public static void main(String[] args) {
        // System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        // Add these arguments for better font handling
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");

        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get("file:///D:/Projects/DrBarragan/templates/UC/UC-1.html");

            // Wait for fonts to load using JavaScript
            ((JavascriptExecutor) driver).executeAsyncScript(
                    "var callback = arguments[arguments.length - 1];" +
                            "document.fonts.ready.then(function() {" +
                            "  callback();" +
                            "});"
            );

            // Enhanced print parameters
            Map<String, Object> printParams = new HashMap<>();
            printParams.put("landscape", false);
            printParams.put("displayHeaderFooter", false);
            printParams.put("printBackground", true);
            printParams.put("preferCSSPageSize", true);
            // Add these parameters for better font rendering
            printParams.put("transferMode", "ReturnAsBase64");
            HashMap<String, Object> pageParams = new HashMap<>();
            pageParams.put("width", 8.27);   // A4 width in inches
            pageParams.put("height", 11.69);  // A4 height in inches
            printParams.put("paperWidth", 8.27);
            printParams.put("paperHeight", 11.69);
            printParams.put("marginTop", 0.4);
            printParams.put("marginBottom", 0.4);
            printParams.put("marginLeft", 0.4);
            printParams.put("marginRight", 0.4);

            Map<String, Object> result = ((ChromeDriver) driver)
                    .executeCdpCommand("Page.printToPDF", printParams);

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