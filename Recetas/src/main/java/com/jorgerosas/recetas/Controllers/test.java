package com.jorgerosas.recetas.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class test {
    @FXML
    private StackPane bluePane;

    @FXML
    public void initialize() {
        // Hover animation
        bluePane.getParent().setOnMouseEntered(e -> animateWidth(184));
        bluePane.getParent().setOnMouseExited(e -> animateWidth(60));
    }

    private void animateWidth(int targetWidth) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(500),
                        new KeyValue(bluePane.minWidthProperty(), targetWidth)
                )
        );
        timeline.play();
    }
}
