package com.jorgerosas.recetas.Controllers;

import com.jorgerosas.recetas.AppConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainController {

    @FXML
    private void newRecipe(ActionEvent event) throws IOException {

        // Your parameters to inject
        String str1 = "Hello";
        String str2 = "World";

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jorgerosas/recetas/NewRecipe.fxml"));

        String dynamicPrompt = "Dynamic prompt text " + System.currentTimeMillis();

        // Use a controller factory to inject the dynamic string into the controller constructor.
        loader.setControllerFactory(param -> new NRController("", ""));

        // Load the FXML. The controller will be constructed with your dynamic string.
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));

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

    @FXML
    private void searchRecipes(ActionEvent event) {
        System.out.println("open search view");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/jorgerosas/recetas/LoadRecipes.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
