package com.jorgerosas.recetas.Controllers;

import com.jorgerosas.recetas.Models.PrescriptionBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainController {

    @FXML
    private Button btnnewRecipe;

    @FXML
    private Button btnshowRecipes;

    @FXML
    private Button btnGeneratePDF; // Fixed the typo here

    @FXML
    private TextArea txtPatientName;

    @FXML
    private TextArea txtDate;

    @FXML
    private TextArea txtDescription;

    // Action when NUEVA RECETA is clicked
    @FXML
    private void newRecipe(ActionEvent event) {
        try {
            // 1. Load the FXML file
            Parent root = FXMLLoader.load(getClass().getResource("/com/jorgerosas/recetas/newRecipe-view.fxml"));

            // 2. Get the current stage (window)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 3. Set the new scene
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Action when GUARDADAS is clicked
    @FXML
    private void showRecipes(ActionEvent event) {
        System.out.println("Mostrando recetas guardadas...");
        // Logic to show saved recipes here
    }

    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Action when GENERAR PDF is clicked
    @FXML
    private void generatePDF(ActionEvent event) {

    }
}
