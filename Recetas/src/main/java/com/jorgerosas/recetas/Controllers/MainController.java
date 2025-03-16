package com.jorgerosas.recetas.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainController {

    @FXML private Label messageLabel;
    @FXML private Button clickButton;

    @FXML
    private void initialize() {
        clickButton.setOnAction(e -> messageLabel.setText("Button Clicked!"));
    }

}
