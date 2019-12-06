package server;

import com.google.gson.Gson;
import logger.LogLevel;
import logger.Logger;
import models.Response;
import models.ResponseType;
import models.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientRequestHandler {
    private static final Pattern registerPattern = Pattern.compile("register (\\w+), (\\w+)");
    private static final Pattern loginPattern = Pattern.compile("login (\\w+), (\\w+)");
    private static final Pattern createChannelPattern = Pattern.compile("create channel (\\w+), (\\w+)");
    private static final Pattern joinChannelPattern = Pattern.compile("join channel (\\w+), (\\w+)");
    private static final Pattern logoutPattern = Pattern.compile("join channel (\\w+)");
    private static final Pattern sendMessagePattern = Pattern.compile("send (\\.+), (\\w+)");
    private static final Pattern refreshPattern = Pattern.compile("refresh (\\w+)");
    private static final Pattern channelMembersPattern = Pattern.compile("channel members (\\w+)");
    private static final Pattern leavePattern = Pattern.compile("leave (\\w+)");
    private final Gson gson = new Gson();
    private final DataCenter dataCenter;
    private final Logger logger;

    public ClientRequestHandler(DataCenter dataCenter, Logger logger) {
        this.dataCenter = dataCenter;
        this.logger = logger;
    }

    String getResponseJson(String request) {
        Matcher matcher;
        if ((matcher = registerPattern.matcher(request)).find()) {
            return getRegisterResponse(matcher);
        }
        return new Response<>(ResponseType.Error, "Unknown request pattern.").toJson();
    }

    private String getRegisterResponse(Matcher request) {
        User user = new User(request.group(1), request.group(2));
        try {
            dataCenter.registerUser(user);
            return new Response<>(ResponseType.Successful, "").toJson();
        } catch (IllegalArgumentException e) {
            logger.log(LogLevel.Error, e.getMessage());
            return new Response<>(ResponseType.Error, e.getMessage()).toJson();
        }
    }
}
