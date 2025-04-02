package com.jorgerosas.recetas.Models;

import java.io.File;
import java.io.IOException;

public class Test {

    public static void createFile(String filePath) {
        try {
            File file = new File(filePath);

            if (file.createNewFile()) {
                System.out.println("File created: " + file.getAbsolutePath());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Example usage
    public static void main(String[] args) {
        createFile("example.txt");
    }
}