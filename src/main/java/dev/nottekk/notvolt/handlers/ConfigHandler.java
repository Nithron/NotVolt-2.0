package dev.nottekk.notvolt.handlers;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.List;

public class ConfigHandler {

    private final String[] dotenvFilePaths;
    private static Dotenv[] dotenvFiles;

    public ConfigHandler(final String... dotenvFilePaths) {
        this.dotenvFilePaths = dotenvFilePaths;
    }

    public void load() {
        List<Dotenv> res = new ArrayList<>();
        for (String dotenvFilePath : dotenvFilePaths) {
            res.add(Dotenv.configure()
                    .directory(dotenvFilePath)
                    .load());
        }

        if (null != res) {
            dotenvFiles = res.toArray(Dotenv[]::new);
        }
    }

    public static String get(String key) {
        String res = null;
        for (Dotenv dotenv : dotenvFiles) {
            res = dotenv.get(key.toUpperCase());
            if (!res.isEmpty()) {
                break;
            }
        }
        return res;
    }

}
