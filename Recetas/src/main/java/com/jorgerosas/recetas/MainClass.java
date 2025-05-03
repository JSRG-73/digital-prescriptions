package com.jorgerosas.recetas;
import com.jorgerosas.recetas.Models.StageManager;
import com.jorgerosas.recetas.Models.TemplateCopier;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class MainClass extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        StageManager.setPrimaryStage(primaryStage);

        Image icon = new Image(getClass().getResourceAsStream("/assets/fav-icon.png"));
        primaryStage.getIcons().add(icon);

        VBox root = (VBox) FXMLLoader.load(getClass().getResource("Main-view.fxml"));
        Scene scene = new Scene(root, 800, 500);
        primaryStage.setTitle("Recetas Digitales");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {

        String baseDir = AppConfig.getInstance().getBaseDirectory();

        try {
            TemplateCopier.copyTemplates(baseDir);
            System.out.println("Templates copied successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        launch(args);
    }
}