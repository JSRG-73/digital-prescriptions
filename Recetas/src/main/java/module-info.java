module com.jorgerosas.recetas {
    requires javafx.controls;
    requires javafx.fxml;
    requires itextpdf;
    requires openhtmltopdf.pdfbox;
    //requires org.xhtmlrenderer;
    requires com.github.librepdf.openpdf;
    //requires openhtmltopdf.svg.support;
    requires openhtmltopdf.core;


    opens com.jorgerosas.recetas to javafx.fxml;
    opens com.jorgerosas.recetas.Controllers to javafx.fxml;
    //exports com.jorgerosas.recetas;
    exports com.jorgerosas.recetas.Models;
    opens com.jorgerosas.recetas.Models to javafx.fxml;
}