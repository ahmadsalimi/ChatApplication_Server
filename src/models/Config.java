package models;

import json.JsonFileReader;
import logger.LogLevel;

import java.io.FileNotFoundException;

public class Config {
    private static final String configPath = ""; // TODO
    private static Config Instance;
    private final int port = 12345;
    private final LogLevel minLogLevel = LogLevel.Info;
    private final String usersPath = "";
    private final String channelsPath = "";

    private Config() {
    }

    public static Config getInstance() {
        if (Instance == null) {
            try {
                JsonFileReader jsonReader = new JsonFileReader();
                Instance = jsonReader.read(configPath, Config.class);
            } catch (FileNotFoundException e) {
                Instance = new Config();
            }
        }
        return Instance;
    }

    public int getPort() {
        return port;
    }

    public LogLevel getMinLogLevel() {
        return minLogLevel;
    }

    public String getUsersPath() {
        return usersPath;
    }

    public String getChannelsPath() {
        return channelsPath;
    }
}
