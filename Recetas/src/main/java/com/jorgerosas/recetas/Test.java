package com.jorgerosas.recetas;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Font;

public class Test extends Application {

    private final BorderPane parent = new BorderPane();

    @Override
    public void init() {
        buildUI();
    }

    private void buildUI() {
        Label label = new Label("Hello, ControlsFX!");
        Button fontButton = new Button("Select Font");
        VBox container = new VBox(10, label, fontButton);
        container.setAlignment(Pos.CENTER);

        fontButton.setOnAction(e -> {
            // Create custom dialog
            Dialog<Font> dialog = new Dialog<>();
            dialog.setTitle("Font Family Selection");
            dialog.setHeaderText("Choose a Font Family");

            // Set up ComboBox with font families
            ComboBox<String> fontFamilyBox = new ComboBox<>();
            fontFamilyBox.setItems(FXCollections.observableArrayList(Font.getFamilies()));
            fontFamilyBox.setValue(label.getFont().getFamily());

            // Create dialog layout
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));
            grid.add(new Label("Font Family:"), 0, 0);
            grid.add(fontFamilyBox, 1, 0);

            // Add buttons
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            dialog.getDialogPane().setContent(grid);

            // Convert result to Font
            dialog.setResultConverter(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    Font currentFont = label.getFont();
                    return Font.font(
                            fontFamilyBox.getValue(),
                            currentFont.getSize()
                    );
                }
                return null;
            });

            // Apply selected font
            dialog.showAndWait().ifPresent(label::setFont);
        });

        parent.setCenter(container);
    }

    @Override
    public void start(Stage stage) {
        setupStage(stage);
    }

    private void setupStage(Stage stage) {
        Scene scene = new Scene(parent, 640, 480);
        stage.setTitle("Font Family Selection");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}