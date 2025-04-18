package com.jorgerosas.recetas.Controllers;

import com.jorgerosas.recetas.AppConfig;
import com.jorgerosas.recetas.Models.JsonCreator;
import com.jorgerosas.recetas.Models.PdfGenerator;
import com.jorgerosas.recetas.Models.PrescriptionBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class NRController implements Initializable {
    @FXML
    private TextField txtPatientName;

    @FXML
    private TextArea txtDescription;

    @FXML
    private DatePicker datePicker;

    private final String Name;
    private final String Description;

    public NRController(String name, String description) {
        this.Name = name;
        this.Description = description;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        datePicker.setValue(LocalDate.now());
        System.out.println(Name);
        System.out.println(Description);

        txtPatientName.setText(Name);
        txtDescription.setText(Description);
    }


    @FXML
    private void openSearcher(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/jorgerosas/recetas/LoadRecipes.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void clearFields(ActionEvent event) {
        System.out.println("Limpiar campos");
        txtPatientName.clear();
        txtDescription.clear();
    }

    @FXML
    private void goHome(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/jorgerosas/recetas/Main-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openPdfFolder(ActionEvent event) throws IOException {

        String baseDir = AppConfig.getInstance().getBaseDirectory();
        baseDir += File.separator +"Recetas";

        File dir = new File(baseDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Failed to create directory: " + baseDir);
        }

        // Use the Desktop API if supported (works on Windows and macOS)
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(dir);
        } else {
            // Fallback to OS-specific command execution
            String osName = System.getProperty("os.name").toLowerCase();
            if (osName.contains("win")) {
                Runtime.getRuntime().exec("explorer " + dir.getAbsolutePath());
            } else if (osName.contains("mac")) {
                Runtime.getRuntime().exec("open " + dir.getAbsolutePath());
            } else {
                throw new UnsupportedOperationException("Open folder not supported on this OS: " + osName);
            }
        }
    }


    private String formatDate(){

        LocalDate selectedDate = datePicker.getValue();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMMM-yyyy", new Locale("es", "ES"));
        String formattedDate = selectedDate.format(formatter);
        System.out.println(formattedDate);
        return formattedDate;
    }

    @FXML
    private void generatePDF(ActionEvent event) throws IOException {
        System.out.println("Generando PDF...");
        String patientName = txtPatientName.getText();
        String date = formatDate();
        String description = txtDescription.getText();

        if (patientName.isEmpty() || date.isEmpty() || description.isEmpty()) {
            System.out.println("Por favor, completa todos los campos antes de generar el PDF.");
        } else {

            patientName = patientName.replaceAll("[/\\\\]", "-");
            date = date.replaceAll("[/\\\\]", "-");
            description = description.replaceAll("[/\\\\]", "-");

            PdfGenerator pdfG = new PdfGenerator();
            String baseDir = AppConfig.getInstance().getBaseDirectory();

            String filename = patientName + " " + date;

            String htmlFilePath = baseDir + File.separator +"templates"+File.separator+"UC"+File.separator + filename +".html";
            String htmlTemplatePath= "/templates/UC/UC.html";
            String simplePath = pdfG.pathHandler(filename+".html");

            System.out.println("htmltemplatepath: " + htmlTemplatePath);

            PrescriptionBuilder pb = new PrescriptionBuilder(htmlTemplatePath);
            pb.readHtmlFromResources();
            pb.replaceDataShort(patientName, date, description);
            pb.saveHtml(pb.getHtml(),filename+".html");

            System.out.println("PDF generado para: " + patientName);

            pdfG.generate(simplePath);
            pdfG.savePdf(pdfG.getPdfBytes(),filename+".pdf");
            pdfG.generate(simplePath);

            JsonCreator js = new JsonCreator();
            boolean b = js.createJsonFile(patientName,date,description);
            System.out.println(b);

            File myObj = new File(htmlFilePath);
            myObj.delete();

            txtPatientName.clear();
            txtDescription.clear();
        }
    }
}
