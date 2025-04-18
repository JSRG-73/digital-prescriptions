module com.jorgerosas.recetas {
    requires javafx.controls;
    requires javafx.fxml;
    requires itextpdf;
    requires openhtmltopdf.pdfbox;
    requires com.github.librepdf.openpdf;
    requires openhtmltopdf.core;
    requires com.aayushatharva.brotli4j;
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
    requires javafx.web;
    requires json.simple;

    // Export your package to JavaFX modules
    exports com.jorgerosas.recetas to javafx.graphics, javafx.fxml;
    exports com.jorgerosas.recetas.Models;
    exports com.jorgerosas.recetas.Controllers to javafx.graphics;

    // Add this to allow reflective access
    opens com.jorgerosas.recetas to javafx.fxml;
    opens com.jorgerosas.recetas.Controllers to javafx.fxml;
    exports com.jorgerosas.recetas.Views to javafx.fxml, javafx.graphics;
    opens com.jorgerosas.recetas.Views to javafx.fxml;

    //opens com.example to javafx.graphics, javafx.fxml;
    opens com.jorgerosas.recetas.Models to brotli4j, javafx.fxml;
}