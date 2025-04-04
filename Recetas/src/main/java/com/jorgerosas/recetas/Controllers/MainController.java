package com.jorgerosas.recetas.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

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
        System.out.println("Nueva receta creada!");
        txtPatientName.clear();
        txtDate.clear();
        txtDescription.clear();
    }

    // Action when GUARDADAS is clicked
    @FXML
    private void showRecipes(ActionEvent event) {
        System.out.println("Mostrando recetas guardadas...");
        // Logic to show saved recipes here
    }

    // Action when GENERAR PDF is clicked
    @FXML
    private void generatePDF(ActionEvent event) {
        System.out.println("Generando PDF...");
        String patientName = txtPatientName.getText();
        String date = txtDate.getText();
        String description = txtDescription.getText();

        if (patientName.isEmpty() || date.isEmpty() || description.isEmpty()) {
            System.out.println("Por favor, completa todos los campos antes de generar el PDF.");
        } else {
            System.out.println("PDF generado para: " + patientName);
            // Add your PDF generation logic here
        }
    }
}
