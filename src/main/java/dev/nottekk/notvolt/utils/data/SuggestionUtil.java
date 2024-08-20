package dev.nottekk.notvolt.utils.data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SuggestionUtil {

    public static String buildSuggestionLine(String date, String guildName, String username, String suggestion) {
        return String.format("[%s] [%s] [%s] Suggests: %s", date, guildName, username, suggestion);
    }

    public static void logSuggestion(String suggestion) {
        String fileName = "suggestions.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(suggestion);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

}
