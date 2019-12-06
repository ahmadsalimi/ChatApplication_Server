package models;

import java.io.FileNotFoundException;

public class Config {
    private static final String configPath = ""; // TODO
    private static Config Instance;
    private final int Port = 12345;

    private Config() {
    }

    private static Config getInstance() {
        if (Instance == null) {
            try {
                JsonFileReader<Config> jsonReader = new JsonFileReader<>(configPath, Config.class);
                Instance = jsonReader.read();
            } catch (FileNotFoundException e) {
                Instance = new Config();
            }
        }
        return Instance;
    }

    public int getPort() {
        return Port;
    }
}
