module com.jorgerosas.recetas {
    requires javafx.controls;
    requires javafx.fxml;
    requires itextpdf;
    requires openhtmltopdf.pdfbox;
    //requires org.xhtmlrenderer;
    requires com.github.librepdf.openpdf;
    requires openhtmltopdf.core;
    requires com.aayushatharva.brotli4j;
    //requires brotli4j.native.windows.x86_64;
    requires com.aayushatharva.brotli4j.service;

    requires org.seleniumhq.selenium.api;
    requires org.seleniumhq.selenium.chrome_driver; // If you're using ChromeDriver
    requires org.seleniumhq.selenium.remote_driver; // If using RemoteWebDriver
    requires org.seleniumhq.selenium.support; // For additional Selenium support
    requires io.github.bonigarcia.webdrivermanager; // Add WebDriverManager module
    requires org.seleniumhq.selenium.firefox_driver;
    requires org.apache.commons.lang3;
    requires org.apache.pdfbox;
    requires dev.failsafe.core;


    // Add this to allow reflective access
    opens com.jorgerosas.recetas.Models to brotli4j;
    //opens com.jorgerosas.recetas.Models to brotli4j.native.windows.x86_64, brotli4j.service;

    opens com.jorgerosas.recetas to javafx.fxml;
    opens com.jorgerosas.recetas.Controllers to javafx.fxml;
    //exports com.jorgerosas.recetas;
    exports com.jorgerosas.recetas.Models;

}