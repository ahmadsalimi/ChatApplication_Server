package models;

public class Config {
    private static Config INSTANCE;
    private final int Port = 12345;

    private Config() {
        // TODO: read from config file
    }

    private static Config getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Config();
        }
        return INSTANCE;
    }

    public int getPort() {
        return Port;
    }
}
