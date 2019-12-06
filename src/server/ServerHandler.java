package server;

import logger.LogLevel;
import logger.Logger;
import models.Config;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerHandler {
    private final Logger logger;
    private ClientRequestHandler clientRequestHandler;
    private DataCenter dataCenter;
    private ServerSocket serverSocket;

    public ServerHandler(Logger logger) {
        this.logger = logger;
        dataCenter = new DataCenter(logger);
        clientRequestHandler = new ClientRequestHandler(dataCenter, logger);
    }

    public void init() {
        dataCenter.initFromFile();
    }

    public void Run() throws Exception {
        try {
            serverSocket = new ServerSocket(Config.getInstance().getPort());


            while (true) {
                Socket socket = serverSocket.accept();

            }


        } catch (Exception e) {
            logger.log(LogLevel.Error, e.getMessage());
            throw e;
        }
    }
}
