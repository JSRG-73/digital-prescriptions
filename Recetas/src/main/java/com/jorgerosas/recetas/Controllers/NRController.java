package com.jorgerosas.recetas.Controllers;

import com.jorgerosas.recetas.AppConfig;
import com.jorgerosas.recetas.Models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import java.time.LocalTime;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

public class NRController implements Initializable {

    @FXML
    private Label statuslabel;

    @FXML
    private TextField txtPatientName;

    @FXML
    private TextArea txtDescription;

    @FXML
    private DatePicker datePicker;

    /*@FXML
    private ToggleGroup sizeGroup;*/

    @FXML
    private Pane mainPane;

    private final String Name;
    private final String Description;
    private double wWidth;

    public NRController(String name, String description) {
        this.Name = name;
        this.Description = description;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        datePicker.setValue(LocalDate.now());
        txtPatientName.setText(Name);
        txtDescription.setText(Description);
    }

    @FXML
    private void openSearcher(ActionEvent event) {
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
            stage.centerOnScreen();
            stage.setScene(new Scene(root));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openPdfFolder(ActionEvent event) throws IOException {
        String baseDir = AppConfig.getInstance().getBaseDirectory();
        baseDir += File.separator + "Recetas";

        File dir = new File(baseDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Failed to create directory: " + baseDir);
        }

        File[] files = dir.listFiles(File::isFile); // Get only files, not directories
        if (files != null && files.length > 0) {
            // Sort files by last modified date (newest first)
            Arrays.sort(files, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));
            File latestFile = files[0];
            String osName = System.getProperty("os.name").toLowerCase();

            try {
                if (osName.contains("win")) {
                    String command = "explorer /select,\"" + latestFile.getAbsolutePath() + "\"";
                    Runtime.getRuntime().exec(command);
                } else if (osName.contains("mac")) {
                    Runtime.getRuntime().exec(new String[]{"open", "-R", latestFile.getAbsolutePath()});
                } else {
                    Desktop.getDesktop().open(dir);
                }
            } catch (IOException e) {
                throw new IOException("Failed to open folder with file selection", e);
            }
        } else {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(dir);
            } else {
                // Fallback for systems without Desktop support
                String osName = System.getProperty("os.name").toLowerCase();
                String[] command;
                if (osName.contains("win")) {
                    command = new String[]{"explorer", dir.getAbsolutePath()};
                } else if (osName.contains("mac")) {
                    command = new String[]{"open", dir.getAbsolutePath()};
                } else {
                    throw new UnsupportedOperationException("Open folder not supported on this OS: " + osName);
                }
                Runtime.getRuntime().exec(command);
            }
        }
    }


    @FXML
    private void generatePDF(ActionEvent event) throws IOException {

        //RadioButton pageSize = (RadioButton) sizeGroup.getSelectedToggle();

        System.out.println("Generando PDF...");
        String patientName = txtPatientName.getText();
        String date = formatDate();
        String description = txtDescription.getText();

        LocalTime now = LocalTime.now();
        String time = now.format(DateTimeFormatter.ofPattern("hh-mm-ss a"));
        time = time.replace(".","");

        if (patientName.isEmpty() || date.isEmpty() || description.isEmpty()) {
            System.out.println("Por favor, completa todos los campos antes de generar el PDF.");
        } else {

            //**************Declarations*************************************************************
            patientName = patientName.replaceAll("[/\\\\]", "-");
            date = date.replaceAll("[/\\\\]", "-");
            description = description.replaceAll("[/\\\\]", "-");
            String auxDescription=description;
            String finalfilename = patientName + " " + date + " " + time;
            finalfilename=JsonCreator.sanitize(finalfilename);
            String htmlTemplatePath;

            //if(pageSize.getId().equals("fullSize")) {
                htmlTemplatePath = "/templates/UC/UC.html";
            /*}else{
                htmlTemplatePath = "/templates/UC/UC-half.html";
            }*/

            ArrayList<String> pdfList = new ArrayList<String>();
            ArrayList<String> htmlList = new ArrayList<String>();

            String baseDir = AppConfig.getInstance().getBaseDirectory();
            String pdfpath = baseDir + File.separator + "Recetas";
            String htmlpath = baseDir + File.separator + "Html";

            //Generate Json
            JsonCreator js = new JsonCreator();
            boolean b = js.createJsonFile(patientName, date, description, finalfilename);


            int i=0;
            do {
                auxDescription = singlePdf(i, htmlTemplatePath, patientName, date, auxDescription);
                pdfList.add("Pagina-" + i + ".pdf");
                htmlList.add("Pagina-" + i + ".html");

                if(auxDescription.isEmpty() || auxDescription.isBlank()) break;

                i++;
            } while (true);

            PdfMerger.merge(pdfList,pdfpath);

            FileDeleter.deleteFiles(pdfList, pdfpath);
            FileDeleter.deleteFiles(htmlList, htmlpath);

            txtPatientName.clear();
            txtDescription.clear();

            System.out.println("PDF generado para: " + patientName);

            File file = new File(pdfpath+File.separator+"MergedPdf.pdf");
            File rename = new File(pdfpath+File.separator+ finalfilename +".pdf");

            if (file.renameTo(rename)) {
                System.out.println("File Successfully Rename");
            }
            else {
                System.out.println("Operation Failed");
            }
        }
    }

    @FXML
    private void about(ActionEvent event) {
        InfoDialog.showInfo();
    }

    @FXML
    private void closeApp(ActionEvent event) {
        Stage stage = StageManager.getPrimaryStage();
        stage.close();
    }

    private String singlePdf(int currentNumber, String HtmlTemplatePath, String patientName, String date, String auxDescription) throws IOException {

        String currentname="Pagina-"+currentNumber;

        //Create html
        PrescriptionBuilder pb = new PrescriptionBuilder(HtmlTemplatePath);
        pb.readHtmlFromResources();
        pb.replaceDataShort(patientName, date, auxDescription);
        pb.saveHtml(pb.getHtml(), FilterFileName.safeFilename(currentname, "html"));

        //Generate pdf
        PdfGenerator pdfG = new PdfGenerator();
        String browserPath = pdfG.pathHandler(FilterFileName.safeFilename(currentname, "html"));
        pdfG.generate(browserPath);
        pdfG.savePdf(pdfG.getPdfBytes(), FilterFileName.safeFilename(currentname, "pdf"));

        //Separate pdf text from the remaining text
        String baseDir = AppConfig.getInstance().getBaseDirectory();
        String pdfpath = baseDir + File.separator + "Recetas" + File.separator + currentname + ".pdf";
        PdfReader reader = new PdfReader(pdfpath);
        String contentfrompdf = reader.getContent();
        contentfrompdf = StringHandler.removeLineBreaks(contentfrompdf);
        auxDescription=auxDescription.substring(StringHandler.findEndOfFragment(auxDescription,contentfrompdf));

        return auxDescription;
    }

    private String formatDate() {

        LocalDate selectedDate = datePicker.getValue();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMMM-yyyy", new Locale("es", "ES"));
        String formattedDate = selectedDate.format(formatter);
        return formattedDate;
    }
}