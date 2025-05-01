package com.jorgerosas.recetas.Models;

import java.util.ArrayList;
import java.util.List;

public class StringHandler {

    public static String removeLineBreaks(String text) {

        if (text == null || text.isEmpty()) {
            return text;
        }

        return text.replaceAll("\\r?\\n", "");
    }

    public static List<Integer> getLineBreakIndexes(String text) {
        List<Integer> indexes = new ArrayList<>();
        if (text == null || text.isEmpty()) {return indexes;}

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

    public static String addLinebreaks(List<Integer> indexes, String text){
        String newString = "";

            for(int i=0;i<indexes.size()-1;i++){
                System.out.println(i+" "+i+1);
            }


        return newString;
    }


    public static void main(String[] args) {

        String story = "The Aurelia was built to cross the Silent Void, a region of space without stars, without planets, without echoes of life.\n"
                + "A deep exploration vessel, designed to endure years of travel without external contact.\n"
                + "Its mission was simple: push beyond known space, record what could be found, and return with knowledge.\n"
                + "\n"
                + "But something changed.\n"
                + "\n"
                + "Somewhere beyond the Epsilon quadrant, the Aurelia stopped sending reports.\n"
                + "It wasn't a sudden cut, but a fading.\n"
                + "First the signals grew weaker. Then more erratic.\n"
                + "Then, only silence.\n"
                + "\n"
                + "Years later, it was found by a Consortium satellite, drifting near an uncharted system.\n"
                + "Intact. Minimal power. No visible damage. But no response.\n"
                + "\n"
                + "A team was sent to board her. Inside, everything seemed normal.\n"
                + "Lights flickered. The hydroponic garden still thrived. Recordings were complete.\n"
                + "\n"
                + "But no one was there.\n"
                + "\n"
                + "The hibernation pods were empty. Beds made.\n"
                + "Food still on trays, as if they had vanished in the middle of the day.\n"
                + "No signs of struggle, or escape, or system failure.\n"
                + "Only absence.\n"
                + "\n"
                + "In the final log, the ship’s AI left a cryptic message:\n"
                + "\"Crew delivered to the horizon. Heading: forward.\"\n"
                + "\n"
                + "The authorities sealed the case.\n"
                + "Said it was a massive malfunction. Psychological collapse.\n"
                + "Collective loss.\n"
                + "\n"
                + "But the Aurelia was never decommissioned.\n"
                + "\n"
                + "It still flies.\n"
                + "\n"
                + "Occasionally it pings distant stations, drifting along the same paths it once charted with its crew.\n"
                + "Silent. Lights on.\n"
                + "\n"
                + "As if it's still searching for something.\n"
                + "\n"
                + "Or waiting to come home.";

        List<Integer> indexes = getLineBreakIndexes(story);
        String nlb = removeLineBreaks(story);
        System.out.println(nlb);
        System.out.println(indexes);
        System.out.println(addLinebreaks(indexes,nlb));
    }
}