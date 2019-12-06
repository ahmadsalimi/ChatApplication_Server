package server;

import exception.BadRequestException;
import logger.LogLevel;
import logger.Logger;
import models.AuthTokenGenerator;
import models.Response;
import models.ResponseType;
import models.User;

import java.util.ArrayList;
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
    private final AuthTokenGenerator authTokenGenerator = new AuthTokenGenerator();
    private final DataCenter dataCenter;
    private final Logger logger;

    public ClientRequestHandler(DataCenter dataCenter, Logger logger) {
        this.dataCenter = dataCenter;
        this.logger = logger;
    }

    String executeRequest(String request) {
        Matcher requestMatcher;
        try {
            if ((requestMatcher = registerPattern.matcher(request)).find())
            {
                return getRegisterResponse(requestMatcher).toJson();
            }
            else if ((requestMatcher = loginPattern.matcher(request)).find())
            {
                return getLoginResponse(requestMatcher).toJson();
            }
            else if ((requestMatcher = createChannelPattern.matcher(request)).find())
            {
                return getCreateChannelResponse(requestMatcher).toJson();
            }
            else if ((requestMatcher = joinChannelPattern.matcher(request)).find())
            {
                return getJoinChannelResponse(requestMatcher).toJson();
            }
            else if ((requestMatcher = logoutPattern.matcher(request)).find())
            {
                return getLogoutResponse(requestMatcher).toJson();
            }
            else if ((requestMatcher = sendMessagePattern.matcher(request)).find())
            {
                return getSendMessageResponse(requestMatcher).toJson();
            }
            else if ((requestMatcher = refreshPattern.matcher(request)).find())
            {
                return getRefreshResponse(requestMatcher).toJson();
            }
            else if ((requestMatcher = channelMembersPattern.matcher(request)).find())
            {
                return getChannelMembersResponse(requestMatcher).toJson();
            }
            else if ((requestMatcher = leavePattern.matcher(request)).find())
            {
                return getLeaveResponse(requestMatcher).toJson();
            }
            return new Response<>(ResponseType.Error, "Unknown request pattern.").toJson();
        } catch (BadRequestException e) {
            logger.log(LogLevel.Error, e.getMessage());
            return new Response<>(ResponseType.Error, e.getMessage()).toJson();
        }
    }

    private Response<String> getRegisterResponse(Matcher request) {
        User user = new User(request.group(1), request.group(2));
        dataCenter.registerUser(user);
        return new Response<>(ResponseType.Successful, "");
    }

    private Response<String> getLoginResponse(Matcher requestMatcher) {
        User user = dataCenter.getUserByUsername(requestMatcher.group(1));
        if (user == null) {
            throw new BadRequestException("Username is not valid.");
        }
        if (!user.getPassword().equals(requestMatcher.group(2))) {
            throw new BadRequestException("Wrong password.");
        }
        if (user.getAuthToken() != null) {
            throw new BadRequestException("The user " + user.getUsername() + " is already logged in.");
        }
        user.setAuthToken(authTokenGenerator.generateNewToken());
        dataCenter.loginUser(user);
        return new Response<>(ResponseType.AuthToken, user.getAuthToken());
    }

    private Response<String> getCreateChannelResponse(Matcher requestMatcher) {
        return null;
    }

    private Response<String> getJoinChannelResponse(Matcher requestMatcher) {
        return null;
    }

    private Response<String> getLogoutResponse(Matcher requestMatcher) {
        return null;
    }

    private Response<String> getSendMessageResponse(Matcher requestMatcher) {
        return null;
    }

    private Response<ArrayList<String>> getRefreshResponse(Matcher requestMatcher) {
        return null;
    }

    private Response<ArrayList<String>> getChannelMembersResponse(Matcher requestMatcher) {
        return null;
    }

    private Response<String> getLeaveResponse(Matcher requestMatcher) {
        return null;
    }
}
