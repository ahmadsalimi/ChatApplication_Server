package server;

import logger.LogLevel;
import logger.Logger;
import models.Config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Formatter;

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
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Formatter writer = new Formatter(socket.getOutputStream());
                String response = clientRequestHandler.getResponseJson(reader.readLine());
                writer.format(response + "\n");
                writer.flush();
            }

        } catch (Exception e) {
            logger.log(LogLevel.Error, e.getMessage());
            throw e;
        }
    }
}
