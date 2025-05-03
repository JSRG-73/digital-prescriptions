package com.jorgerosas.recetas.Models;

import com.jorgerosas.recetas.AppConfig;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
public class PdfGenerator {
    private byte[] pdfBytes;

    public byte[] getPdfBytes() { return pdfBytes; }

    public void generate(String inputPath) {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");

        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get(inputPath);

            // Wait for fonts to load using JavaScript
            ((JavascriptExecutor) driver).executeAsyncScript(
                    "var callback = arguments[arguments.length - 1];" +
                            "document.fonts.ready.then(function() {" +
                            "  callback();" +
                            "});"
            );

            Map<String, Object> printParams = new HashMap<>();
            printParams.put("landscape", false);
            printParams.put("displayHeaderFooter", false);
            printParams.put("printBackground", true);
            printParams.put("preferCSSPageSize", true);
            printParams.put("transferMode", "ReturnAsBase64");
            HashMap<String, Object> pageParams = new HashMap<>();
            pageParams.put("width", 8.5);
            pageParams.put("height", 11.0);
            printParams.put("paperWidth", 8.5);
            printParams.put("paperHeight", 11.0);
            printParams.put("marginTop", 0.4);
            printParams.put("marginBottom", 0.4);
            printParams.put("marginLeft", 0.4);
            printParams.put("marginRight", 0.4);

            Map<String, Object> result = ((ChromeDriver) driver)
                    .executeCdpCommand("Page.printToPDF", printParams);

            pdfBytes = Base64.getDecoder().decode((String) result.get("data"));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    public void savePdf(byte[] pdfBytes, String filename) throws IOException {

        String baseDir = AppConfig.getInstance().getBaseDirectory();
        Path outputPath = Paths.get(baseDir, "Recetas", filename);

        Files.createDirectories(outputPath.getParent());
        Files.write(outputPath, pdfBytes);

        System.out.println("PDF saved to: " + outputPath.toAbsolutePath());
    }

    public String pathHandler(String filename){

        String baseDir = AppConfig.getInstance().getBaseDirectory();
        Path currentDir = Paths.get(baseDir, "Html", filename);
        //String currentDir = System.getProperty("user.dir");
        String fileUrl = "file:///" + currentDir.toString().replace("\\", "/");

        return fileUrl;
    }
}