package com.jorgerosas.recetas.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private void newRecipe(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/jorgerosas/recetas/newRecipe-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openPdfFolder(ActionEvent event) {
        System.out.println("Open Folder");
        // Logic to show saved recipes here
    }

    @FXML
    private void searchRecipes(ActionEvent event) {
        System.out.println("open search view");
        // Logic to show saved recipes here
    }
}
