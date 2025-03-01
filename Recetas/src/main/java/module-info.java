module com.jorgerosas.recetas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.apache.pdfbox;

    opens com.jorgerosas.recetas to javafx.fxml;
    exports com.jorgerosas.recetas;
}