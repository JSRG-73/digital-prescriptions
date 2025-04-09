package com.jorgerosas.recetas.Views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.web.WebView;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HTMLBrowserController implements Initializable {

    @FXML
    private TreeView<String> fileTreeView;

    @FXML
    private WebView webView;

    private final List<String> htmlFiles = new ArrayList<>();
    private static final String TEMPLATES_PATH = "src/main/resources/templates";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the file tree
        TreeItem<String> rootItem = createFileTree();
        fileTreeView.setRoot(rootItem);
        fileTreeView.setShowRoot(true);

        // Set up tree selection listener
        fileTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String filePath = getFilePath(newValue);
                if (filePath != null && filePath.endsWith(".html")) {
                    loadHTML(filePath);
                }
            }
        });
    }

    private TreeItem<String> createFileTree() {
        // Get the path to the templates directory
        Path path = Paths.get(TEMPLATES_PATH);
        File templatesDir = path.toFile();

        if (!templatesDir.exists() || !templatesDir.isDirectory()) {
            System.err.println("Templates directory not found: " + TEMPLATES_PATH);
            return new TreeItem<>("Templates not found");
        }

        // Create root node
        TreeItem<String> rootItem = new TreeItem<>("Templates");
        rootItem.setExpanded(true);

        // Recursively add all directories and HTML files
        populateTreeItem(rootItem, templatesDir, TEMPLATES_PATH);

        return rootItem;
    }

    private void populateTreeItem(TreeItem<String> item, File dir, String basePath) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                String relativePath = file.getPath().replace(basePath, "").replace("\\", "/");
                if (relativePath.startsWith("/")) {
                    relativePath = relativePath.substring(1);
                }

                TreeItem<String> fileItem = new TreeItem<>(file.getName());

                if (file.isDirectory()) {
                    // Add subdirectory
                    item.getChildren().add(fileItem);
                    populateTreeItem(fileItem, file, basePath);
                } else if (file.getName().endsWith(".html")) {
                    // Add HTML file
                    item.getChildren().add(fileItem);
                    htmlFiles.add(file.getPath());
                }
            }
        }
    }

    private String getFilePath(TreeItem<String> item) {
        if (item == null) return null;

        StringBuilder path = new StringBuilder(item.getValue());
        TreeItem<String> parent = item.getParent();

        // If this is the root item, return null
        if (parent == null || parent.getValue().equals("templates")) {
            return TEMPLATES_PATH + "/" + path;
        }

        // Build path backwards from selected item to root
        while (parent != null && !parent.getValue().equals("templates")) {
            path.insert(0, parent.getValue() + "/");
            parent = parent.getParent();
        }

        return TEMPLATES_PATH + "/" + path;
    }

    private void loadHTML(String filePath) {
        try {
            File file = new File(filePath);
            URL url = file.toURI().toURL();
            webView.getEngine().load(url.toString());
            System.out.println("Loaded: " + filePath);
        } catch (Exception e) {
            System.err.println("Error loading HTML file: " + e.getMessage());
            webView.getEngine().loadContent("<html><body><h1>Error loading file</h1><p>" + e.getMessage() + "</p></body></html>");
        }
    }
}