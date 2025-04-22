package com.jorgerosas.recetas.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class TextController {
    private static final int MAX_LINE_LENGTH = 70; // Characters per line
    private static final int MAX_LINES = 40;       // Total lines allowed

    @FXML private TextArea contentArea;
    @FXML private Label remainingLabel;

    @FXML
    public void initialize() {
        setupTextLimiter();
        setupLabelUpdater();
    }

    // ================== TEXT LIMITING LOGIC ==================
    private void setupTextLimiter() {
        contentArea.textProperty().addListener((obs, oldVal, newVal) -> {
            String processedText = processText(newVal);
            
            if (!newVal.equals(processedText)) {
                Platform.runLater(() -> {
                    // Save caret position before modification
                    int caretPos = contentArea.getCaretPosition();
                    contentArea.setText(processedText);
                    // Restore caret position (clamped to new text length)
                    caretPos = Math.min(caretPos, processedText.length());
                    contentArea.positionCaret(caretPos);
                });
            }
        });
    }

    private String processText(String text) {
        String[] lines = text.split("\n", -1); // Keep empty trailing lines
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < Math.min(lines.length, MAX_LINES); i++) {
            String line = lines[i];
            if (line.length() > MAX_LINE_LENGTH) {
                result.append(line, 0, MAX_LINE_LENGTH);
            } else {
                result.append(line);
            }
            if (i < Math.min(lines.length, MAX_LINES) - 1) {
                result.append("\n");
            }
        }
        return result.toString();
    }

    // ================== USER FEEDBACK ==================
    private void setupLabelUpdater() {
        contentArea.textProperty().addListener((obs, oldVal, newVal) -> {
            String[] lines = newVal.split("\n");
            int currentLine = getCurrentLineNumber(contentArea.getText(), 
                                                 contentArea.getCaretPosition());
            
            updateLabel(
                lines.length,
                currentLine < lines.length ? lines[currentLine].length() : 0
            );
        });
    }

    private int getCurrentLineNumber(String text, int caretPos) {
        return text.substring(0, caretPos).split("\n", -1).length - 1;
    }

    private void updateLabel(int totalLines, int currentLineLength) {
        String status = String.format(
            "Renglones: %d/%d | Linea Actual: %d/%d",
            totalLines, MAX_LINES,
            currentLineLength, MAX_LINE_LENGTH
        );
        remainingLabel.setText(status);
        
        // Visual feedback
        if (totalLines >= MAX_LINES || currentLineLength >= MAX_LINE_LENGTH) {
            remainingLabel.setStyle("-fx-text-fill: red;");
        } else {
            remainingLabel.setStyle("-fx-text-fill: #666;");
        }
    }
}