package com.jorgerosas.recetas.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.util.Objects;

public class MainView{
    public static Scene getMainScene() {
        try {
            Parent root = FXMLLoader.load(
                    Objects.requireNonNull(MainView.class.getResource("/com/jorgerosas.recetas/Main-view.fxml"))
            );
            return new Scene(root, 400, 300);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
            
        }
    }
}