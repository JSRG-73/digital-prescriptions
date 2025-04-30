package com.jorgerosas.recetas.Models;

public class StringHandler {

    public static String addInvisibleChar(String text){
        String lineBreakRegex = "\\r?\\n";
        String marker = "\u200B\n";
        return text.replaceAll(lineBreakRegex, marker);
    }



}
