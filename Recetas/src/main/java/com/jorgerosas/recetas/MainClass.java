package com.jorgerosas.recetas;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainClass extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = (VBox) FXMLLoader.load(getClass().getResource("newRecipe-view.fxml")); // Use VBox correctly
        Scene scene = new Scene(root, 640, 400); // Set the scene dimensions
        primaryStage.setTitle("My Recipe App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}