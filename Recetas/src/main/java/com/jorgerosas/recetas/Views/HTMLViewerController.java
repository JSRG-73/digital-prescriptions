package com.jorgerosas.recetas.Views;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.web.WebView;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class HTMLViewerController implements Initializable {
    @FXML private ListView<File> fileListView;
    @FXML private WebView webView;

    private Path baseDir;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Automatically load files from resources/templates
        loadHTMLFilesFromResources();

        fileListView.setCellFactory(lv -> new ListCell<File>() {
            @Override
            protected void updateItem(File item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : getRelativePath(item));
            }

            private String getRelativePath(File file) {
                return baseDir != null ?
                        baseDir.relativize(file.toPath()).toString() :
                        file.getAbsolutePath();
            }
        });

        fileListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                webView.getEngine().load(newVal.toURI().toString());
            }
        });
    }

    private void loadHTMLFilesFromResources() {
        try {
            // Get the actual filesystem path for resources/templates
            URL templatesUrl = getClass().getClassLoader().getResource("templates");
            if (templatesUrl != null) {
                File templatesDir = new File(templatesUrl.toURI());
                baseDir = templatesDir.toPath();

                // Walk through all files in templates directory
                fileListView.getItems().setAll(
                        java.nio.file.Files.walk(baseDir)
                                .filter(path -> {
                                    String fileName = path.getFileName().toString();
                                    return fileName.toLowerCase().endsWith(".html") ||
                                            fileName.toLowerCase().endsWith(".htm");
                                })
                                .map(Path::toFile)
                                .collect(Collectors.toList())
                );

                if (fileListView.getItems().isEmpty()) {
                    showAlert("No HTML files found in templates directory!");
                }
            } else {
                showAlert("Templates directory not found in resources!");
            }
        } catch (Exception e) {
            showAlert("Error loading templates: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.WARNING
        );
        alert.setContentText(message);
        alert.show();
    }
}