package com.jorgerosas.recetas.Models;

import java.util.ArrayList;
import java.util.List;

public class StringHandler {

    public static String removeLineBreaks(String text) {
        return text.replaceAll("\\r?\\n", "");
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
                originalIndex++;
            } else {
                originalIndex++;
            }
        }
        return originalIndex;
    }

}