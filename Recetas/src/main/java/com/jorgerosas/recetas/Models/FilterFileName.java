package com.jorgerosas.recetas.Models;

import java.util.Set;

public class FilterFileName {
    public static String safeFilename(String input, String extension) {
        // Step 1: Remove existing extension (e.g., .json, .pdf)
        String baseName = input.replaceFirst("(?i)\\." + extension + "$", "");

        // Step 2: Sanitize the base name (allow letters, digits, _-., and spaces)
        String sanitized = baseName
                .replaceAll("[^\\p{L}0-9_\\-. ]", "_")  // Allow spaces!
                .replaceAll("[/:*?\"<>|]", "_")         // Block OS-specific invalid chars
                .replaceAll(" {2,}", " ")                // Collapse multiple spaces
                .replaceAll("^[\\s.]+|[\\s.]+$", "");   // Trim leading/trailing spaces/dots

        // Step 3: Truncate to avoid overflows
        int maxBaseLength = 255 - extension.length() - 1; // Account for "." + extension
        if (sanitized.length() > maxBaseLength) {
            sanitized = sanitized.substring(0, maxBaseLength);
        }

        // Step 4: Handle reserved names (e.g., CON, NUL)
        Set<String> reserved = Set.of("CON", "PRN", "AUX", "NUL");
        String baseWithoutExt = sanitized.split("\\.")[0];
        if (reserved.contains(baseWithoutExt.toUpperCase())) {
            sanitized = "unnamed_file";
        }

        // Step 5: Add extension
        return sanitized + "." + extension;
    }
}
