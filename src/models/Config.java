package models;

import json.JsonFileReader;
import logger.LogLevel;

import java.io.FileNotFoundException;

public class Config {
    private static final String configPath = "configuration.json";
    private static Config Instance;
    private int port = 12345;
    private LogLevel minLogLevel = LogLevel.Info;
    private String usersPath = "Resources/Users/";
    private String channelsPath = "Resources/Channels/";
    private int socketTimeout = 1000;

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

    public int getSocketTimeout() {
        return socketTimeout;
    }
}
