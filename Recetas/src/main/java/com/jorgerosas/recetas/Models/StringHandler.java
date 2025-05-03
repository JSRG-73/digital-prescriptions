package com.jorgerosas.recetas.Models;

import java.util.ArrayList;
import java.util.List;

public class StringHandler {

    public static String removeLineBreaks(String text) {
        return text.replaceAll("\\r?\\n", "");
    }

    public static List<Integer> getLineBreakIndexes(String text) {
        List<Integer> indexes = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            return indexes;
        }

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            // UNIX/macOS: '\n'
            if (c == '\n') {
                indexes.add(i);
            }
            // Windows: '\r\n'
            else if (c == '\r') {
                if (i + 1 < text.length() && text.charAt(i + 1) == '\n') {
                    indexes.add(i);
                    i++;
                } else {
                    indexes.add(i);
                }
            }
        }
        return indexes;
    }

    public static String addLinebreaks(List<Integer> indexes, String text) {

        if (indexes.isEmpty()) return text;

        StringBuilder newString = new StringBuilder(text);

        for (int i = 0; i < indexes.size(); i++) {
            newString.insert(indexes.get(i), "\n");
        }
        return newString.toString();
    }

    public static int findEndOfFragment(String original, String fragment) {
        int originalIndex = 0;
        int fragmentIndex = 0;

        while (fragmentIndex < fragment.length() && originalIndex < original.length()) {
            char originalChar = original.charAt(originalIndex);
            char fragmentChar = fragment.charAt(fragmentIndex);

            if (originalChar == fragmentChar) {
                originalIndex++;
                fragmentIndex++;
            } else if (!Character.isWhitespace(originalChar)) {
                // Si los caracteres no coinciden y el original no es un espacio en blanco,
                // avanzamos en el original para buscar la siguiente posible coincidencia.
                originalIndex++;
            } else {
                // Si el carácter en el original es un espacio en blanco (incluyendo saltos de línea),
                // simplemente avanzamos en el original para intentar encontrar la siguiente
                // parte relevante del fragmento.
                originalIndex++;
            }
        }
        return originalIndex;
    }

    public static void main(String[] args) {
        String originalString = "La nave llegó sin aviso.\n\nNo emitía señal alguna.\n\nSolo estaba ahí, suspendida.\n\nNegra. Angular. Silenciosa.\n\nNo parecía moverse, pero se acercaba.\n\nLas alarmas no sonaron.";
        String fragmentString = "La nave llegó sin aviso.No emitía señal alguna.Solo estaba ahí,suspendida.Negra. Angular. Silenciosa.No parecíamoverse, pero se acercaba.Las alarmas nosonaron";

        int endIndex = findEndOfFragment(originalString, fragmentString);
        System.out.println("El fragmento termina en el índice: " + endIndex);
        System.out.println("Texto original hasta ese índice:\n" + originalString.substring(0, endIndex));
        System.out.println(originalString.length());
    }
}