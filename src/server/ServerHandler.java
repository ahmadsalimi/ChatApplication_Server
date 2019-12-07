package server;

import logger.LogLevel;
import logger.Logger;
import models.Config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Formatter;

public class ServerHandler {
    private final Logger logger;
    private ClientRequestHandler clientRequestHandler;
    private DataCenter dataCenter;
    private ServerSocket serverSocket;

    public ServerHandler(Logger logger) {
        this.logger = logger;
        dataCenter = new DataCenter(logger);
        clientRequestHandler = new ClientRequestHandler(dataCenter);
    }

    public void init() {
        logger.log(LogLevel.Info, "Initializing...");
        dataCenter.initFromFile();
        logger.log(LogLevel.Info, "Initialized.");
    }

    public void Run() throws Exception {
        try {
            serverSocket = new ServerSocket(Config.getInstance().getPort());
            logger.log(LogLevel.Info, "Listening on port " + Config.getInstance().getPort());

            while (true) {
                Socket socket = serverSocket.accept();
                try {
                    socket.setSoTimeout(Config.getInstance().getSocketTimeout());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    Formatter writer = new Formatter(socket.getOutputStream());
                    String request = reader.readLine();
                    logger.log(LogLevel.Info, "Request from " + socket.getInetAddress() + ": " + request);
                    String response = clientRequestHandler.executeRequest(request);
                    writer.format(response + "\n");
                    writer.flush();
                    logger.log(LogLevel.Info, "Response sent: " + response);

                } catch (SocketTimeoutException | NullPointerException e) {
                    handleError(e);
                } finally {
                    socket.close();
                }
            }

        } catch (Exception e) {
            handleError(e);
            throw e;
        }
    }

    private <T extends Exception> void handleError(T e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        logger.log(LogLevel.Error, sw.toString());
    }
}
