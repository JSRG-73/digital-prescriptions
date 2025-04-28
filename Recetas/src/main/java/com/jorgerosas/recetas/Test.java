package com.jorgerosas.recetas;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Test extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a TextArea
        TextArea textArea = new TextArea();
        textArea.setPromptText("Enter text here...");

        // Create a Button
        Button button = new Button("Print Text");
        button.setOnAction(event -> {
            // Store the content of the TextArea in a String
            String textContent = textArea.getText();
            // Print the content to the console
            System.out.println(textContent.length());
        });

        // Create a layout and add the controls
        VBox root = new VBox(10, textArea, button);

        // Set up the scene and stage
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setTitle("TextArea Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}