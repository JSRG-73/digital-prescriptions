package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HtmlViewer extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a ListView to display the HTML file paths.
        ListView<String> listView = new ListView<>();

        // Define the path to your templates folder.
        // Adjust the path if your working directory is different.
        Path templatesDir = Paths.get("src", "main", "resources", "templates");

        // Recursively walk through the folder structure to find .html files
        try (Stream<Path> paths = Files.walk(templatesDir)) {
            List<String> htmlFiles = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(".html"))
                    .map(Path::toString)
                    .collect(Collectors.toList());
            listView.getItems().addAll(htmlFiles);
        } catch (IOException e) {
            e.printStackTrace();
            listView.getItems().add("Error reading templates folder.");
        }

        // Set up the scene using a BorderPane to hold our ListView.
        BorderPane root = new BorderPane();
        root.setCenter(listView);
        Scene scene = new Scene(root, 600, 400);

        primaryStage.setTitle("HTML Files List");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Entry point of the JavaFX application.
    public static void main(String[] args) {
        System.out.println("Working directory: " + System.getProperty("user.dir"));

        launch(args);
    }
}
