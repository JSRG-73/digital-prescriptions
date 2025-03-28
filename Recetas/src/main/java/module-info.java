module com.jorgerosas.recetas {
    requires javafx.controls;
    requires javafx.fxml;
    requires itextpdf;
    requires openhtmltopdf.pdfbox;
    //requires org.xhtmlrenderer;
    requires com.github.librepdf.openpdf;
    //requires openhtmltopdf.svg.support;
    requires openhtmltopdf.core;

    requires org.seleniumhq.selenium.api;
    requires org.seleniumhq.selenium.chrome_driver; // If you're using ChromeDriver
    requires org.seleniumhq.selenium.remote_driver; // If using RemoteWebDriver
    requires org.seleniumhq.selenium.support; // For additional Selenium support
    requires io.github.bonigarcia.webdrivermanager; // Add WebDriverManager module
    requires org.seleniumhq.selenium.firefox_driver;

    opens com.jorgerosas.recetas to javafx.fxml;
    opens com.jorgerosas.recetas.Controllers to javafx.fxml;
    //exports com.jorgerosas.recetas;
    exports com.jorgerosas.recetas.Models;
    opens com.jorgerosas.recetas.Models to javafx.fxml;
}