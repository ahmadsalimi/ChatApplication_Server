import models.Config;
import logger.ConsoleLogger;
import logger.Logger;
import server.ServerHandler;

public class Main {
    public static void main(String[] args) {
        Logger logger = new ConsoleLogger(Config.getInstance().getMinLogLevel());
        ServerHandler serverHandler = new ServerHandler(logger);
        try {
            serverHandler.init();
            serverHandler.Run();
        } catch (Exception ignored) {
        }
    }
}
