module com.jorgerosas.recetas {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.jorgerosas.recetas to javafx.fxml;
    exports com.jorgerosas.recetas;
}