module com.jorgerosas.recetas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires itextpdf;
    //requires org.xhtmlrenderer;

    opens com.jorgerosas.recetas to javafx.fxml;
    exports com.jorgerosas.recetas;
}