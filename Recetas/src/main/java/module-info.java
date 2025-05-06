module com.jorgerosas.recetas {
    requires javafx.fxml;
    requires itextpdf;
    requires org.seleniumhq.selenium.api;
    requires org.seleniumhq.selenium.chrome_driver;
    requires org.seleniumhq.selenium.remote_driver;
    requires org.seleniumhq.selenium.support;
    requires io.github.bonigarcia.webdrivermanager;
    requires org.seleniumhq.selenium.firefox_driver;

    requires dev.failsafe.core;
    requires javafx.web;
    requires json.simple;
    requires org.apache.pdfbox;
    requires org.apache.pdfbox.io;
    requires org.controlsfx.controls;

    exports com.jorgerosas.recetas to javafx.graphics, javafx.fxml;
    exports com.jorgerosas.recetas.Models;
    exports com.jorgerosas.recetas.Controllers to javafx.graphics;

    opens com.jorgerosas.recetas to javafx.fxml;
    opens com.jorgerosas.recetas.Controllers to javafx.fxml;

    //opens com.example to javafx.graphics, javafx.fxml;
    opens com.jorgerosas.recetas.Models to brotli4j, javafx.fxml;
}