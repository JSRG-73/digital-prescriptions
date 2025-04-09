package com.jorgerosas.recetas.Controllers;

import com.jorgerosas.recetas.Models.PdfGenerator;
import com.jorgerosas.recetas.Models.PrescriptionBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class NRController {
    @FXML
    private TextArea txtPatientName;

    @FXML
    private TextArea txtDate;

    @FXML
    private TextArea txtDescription;


    @FXML
    private void clearFields(ActionEvent event) {
        System.out.println("Limpiar campos");
        txtPatientName.clear();
        txtDate.clear();
        txtDescription.clear();
    }

    @FXML
    private void goHome(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/jorgerosas/recetas/main-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void generatePDF(ActionEvent event) {
        System.out.println("Generando PDF...");
        String patientName = txtPatientName.getText();
        String date = txtDate.getText();
        String description = txtDescription.getText();

        if (patientName.isEmpty() || date.isEmpty() || description.isEmpty()) {
            System.out.println("Por favor, completa todos los campos antes de generar el PDF.");
        } else {

            PdfGenerator pdfG = new PdfGenerator();

            // Define file paths for the generated PDF
            String htmlFilePath = "templates/UC/UC-1.html";
            String pdfFilePath = "templates/UC/UC-1.pdf";
            String htlmTemplatePath="/templates/UC/UC.html";
            String simplePath = pdfG.pathHandler("templates/UC/UC-1.html");

            PrescriptionBuilder pb = new PrescriptionBuilder(htlmTemplatePath);
            pb.readHtmlFromResources();
            pb.replaceDataShort(patientName, date, description);
            pb.generateHTML(htmlFilePath);

            System.out.println("PDF generado para: " + patientName);

            pdfG.generate(simplePath,"");
        }
    }
}
