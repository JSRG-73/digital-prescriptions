package com.jorgerosas.recetas.Models;

public class FragmentIndexFinder {
    public static int findOriginalIndex(String original, String fragment) {
        int fragmentIndex = 0;
        int fragmentLength = fragment.length();

        for (int originalIndex = 0; originalIndex < original.length(); originalIndex++) {
            char c = original.charAt(originalIndex);

            // Skip newline characters in the original string
            if (c == '\n') {
                continue;
            }

            // Check if we've already matched the entire fragment
            if (fragmentIndex >= fragmentLength) {
                return originalIndex - 1;
            }

            // Compare characters
            if (c != fragment.charAt(fragmentIndex)) {
                throw new IllegalArgumentException("Fragment does not match original string");
            }

            fragmentIndex++;

            // Return when fragment is fully matched
            if (fragmentIndex == fragmentLength) {
                return originalIndex;
            }
        }

        // Handle case where fragment ends at the last character
        if (fragmentIndex == fragmentLength) {
            return original.length() - 1;
        } else {
            throw new IllegalArgumentException("Fragment is longer than the original's content");
        }
    }

    public static void main(String[] args) {
        String original = "La nave llegó sin aviso.\n"
                + "No emitía señal alguna.\n"
                + "Solo estaba ahí, suspendida.\n"
                + "Negra. Angular. Silenciosa.\n"
                + "No parecía moverse, pero se acercaba.\n"
                + "Las alarmas no sonaron.";

        String fragment = "La nave llegó sin aviso.No emitía señal alguna.Solo estaba ahí, suspendida.Negra. Angular.";

        try {
            int index = findOriginalIndex(original, fragment);
            System.out.println("Corresponding index in original string: " + index);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}