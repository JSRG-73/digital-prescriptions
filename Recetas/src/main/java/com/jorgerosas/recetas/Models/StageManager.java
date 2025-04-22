package com.jorgerosas.recetas.Models;

import javafx.stage.Stage;

public class StageManager {
    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
        primaryStage.setResizable(false);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}