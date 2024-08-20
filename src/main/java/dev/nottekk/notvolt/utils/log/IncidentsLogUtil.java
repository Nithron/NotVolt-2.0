package dev.nottekk.notvolt.utils.log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class IncidentsLogUtil {

    public static String buildIncidentLine(String date, String guildName, String username, String profanity, String suggestionWithIncident) {
        return String.format("[%s] [%s] [%s] Profanity: %s | Incident: %s", date, guildName, username, profanity,suggestionWithIncident);
    }

    public static void logSuggestion(String suggestion) {
        String fileName = "incidents.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(suggestion);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

}
