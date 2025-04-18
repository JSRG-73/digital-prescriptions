package com.jorgerosas.recetas.Controllers;

import com.jorgerosas.recetas.AppConfig;
import com.jorgerosas.recetas.Models.JsonReader;
import com.jorgerosas.recetas.Models.StageManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;


public class SearchController implements Initializable {

    @FXML
    private TableView<String> filesTable;
    @FXML
    private TableColumn<String, String> fileNameColumn;
    @FXML
    private TextField searchField;

    private FilteredList<String> filteredFiles;
    private ObservableList<String> fileNames = FXCollections.observableArrayList();

    // The main method to load files
    public void loadJsonFiles(Path directory) {
        try {

            Files.walk(directory, 2) .filter(path -> {
                        return Files.isRegularFile(path) &&
                        path.getFileName().toString().toLowerCase().endsWith(".json");
                    })
                    .forEach(path -> fileNames.add(path.getFileName().toString()));

            filesTable.setItems(fileNames);

        } catch (IOException e) {
            System.err.println("Error loading files: " + e.getMessage());
        }
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //This sets the path to templates directory
        String baseDir = AppConfig.getInstance().getBaseDirectory();
        Path templatesDir= Paths.get(baseDir, "templates");

        //loads all json files from templates
        fileNameColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue())
        );
        loadJsonFiles(templatesDir);

        //live search
        filteredFiles = new FilteredList<>(fileNames, s -> true);
        filesTable.setItems(filteredFiles);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredFiles.setPredicate(fileName -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseQuery = newValue.toLowerCase();
                return fileName.toLowerCase().contains(lowerCaseQuery);
            });
        });

        //When the user selects a row, it loads the new recipe view with the json data
        filesTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {

                        try {

                            String baseDir1 = AppConfig.getInstance().getBaseDirectory();
                            baseDir1 += File.separator +"templates"+ File.separator +"UC" + File.separator + newValue;
                            JsonReader jr = new JsonReader();
                            jr.readJsonFile(baseDir1);

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jorgerosas/recetas/NewRecipe.fxml"));

                            loader.setControllerFactory(type -> {
                                if (type == NRController.class) {
                                    return new NRController(jr.getName(), jr.getDescription()); // Pass parameters from json
                                } else {
                                    try {
                                        return type.getDeclaredConstructor().newInstance();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });

                            Parent root = loader.load();
                            Stage stage = StageManager.getPrimaryStage();
                            stage.setScene(new Scene(root));
                            stage.show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }
}