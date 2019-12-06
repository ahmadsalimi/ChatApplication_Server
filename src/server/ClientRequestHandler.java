package server;

import exception.BadRequestException;
import models.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientRequestHandler {
    private static final Pattern registerPattern = Pattern.compile("^register\\s+(\\w+)\\s*,\\s*(\\w+)$");
    private static final Pattern loginPattern = Pattern.compile("^login\\s+(\\w+)\\s*,\\s*(\\w+)$");
    private static final Pattern createChannelPattern = Pattern.compile("^create\\s+channel\\s+(\\w+)\\s*,\\s*(.+)$");
    private static final Pattern joinChannelPattern = Pattern.compile("^join\\s+channel\\s+(\\w+)\\s*,\\s*(.+)$");
    private static final Pattern logoutPattern = Pattern.compile("^logout\\s+(.+)$");
    private static final Pattern sendMessagePattern = Pattern.compile("^send\\s+(.+)\\s*,\\s*(.+)$");
    private static final Pattern refreshPattern = Pattern.compile("^refresh\\s+(.+)$");
    private static final Pattern channelMembersPattern = Pattern.compile("^channel\\s+members\\s+(.+)$");
    private static final Pattern leavePattern = Pattern.compile("^leave\\s+(.+)$");
    private final DataCenter dataCenter;

    public ClientRequestHandler(DataCenter dataCenter) {
        this.dataCenter = dataCenter;
    }

    String executeRequest(String request) {
        try {
            Matcher requestMatcher;
            if ((requestMatcher = registerPattern.matcher(request)).find())
            {
                return register(requestMatcher).toJson();
            }
            else if ((requestMatcher = loginPattern.matcher(request)).find())
            {
                return login(requestMatcher).toJson();
            }
            else if ((requestMatcher = createChannelPattern.matcher(request)).find())
            {
                return createChannel(requestMatcher).toJson();
            }
            else if ((requestMatcher = joinChannelPattern.matcher(request)).find())
            {
                return joinChannel(requestMatcher).toJson();
            }
            else if ((requestMatcher = logoutPattern.matcher(request)).find())
            {
                return logout(requestMatcher).toJson();
            }
            else if ((requestMatcher = sendMessagePattern.matcher(request)).find())
            {
                return sendMessage(requestMatcher).toJson();
            }
            else if ((requestMatcher = refreshPattern.matcher(request)).find())
            {
                return refresh(requestMatcher).toJson();
            }
            else if ((requestMatcher = channelMembersPattern.matcher(request)).find())
            {
                return channelMembers(requestMatcher).toJson();
            }
            else if ((requestMatcher = leavePattern.matcher(request)).find())
            {
                return leave(requestMatcher).toJson();
            }
            throw new BadRequestException("Unknown request pattern.");
        } catch (BadRequestException e) {
            return new Response<>(ResponseType.Error, e.getMessage()).toJson();
        }
    }

    private Response<String> register(Matcher request) {
        dataCenter.registerUser(request.group(1), request.group(2));
        return new Response<>(ResponseType.Successful, "");
    }

    private Response<String> login(Matcher requestMatcher) {
        String authToken = dataCenter.loginUser(requestMatcher.group(1), requestMatcher.group(2));
        return new Response<>(ResponseType.AuthToken, authToken);
    }

    private Response<String> createChannel(Matcher requestMatcher) {
        dataCenter.createChannel(requestMatcher.group(2), requestMatcher.group(1));
        return new Response<>(ResponseType.Successful, "");
    }

    private Response<String> joinChannel(Matcher requestMatcher) {
        dataCenter.joinChannel(requestMatcher.group(2), requestMatcher.group(1));
        return new Response<>(ResponseType.Successful, "");
    }

    private Response<String> logout(Matcher requestMatcher) {
        dataCenter.logoutUser(requestMatcher.group(1));
        return new Response<>(ResponseType.Successful, "");
    }

    private Response<String> sendMessage(Matcher requestMatcher) {
        dataCenter.sendMessage(requestMatcher.group(2), requestMatcher.group(1));
        return new Response<>(ResponseType.Successful, "");
    }

    private Response<List<String>> refresh(Matcher requestMatcher) {
        List<String> messages = dataCenter.refresh(requestMatcher.group(1));
        return new Response<>(ResponseType.List, messages);
    }

    private Response<List<String>> channelMembers(Matcher requestMatcher) {
        List<String> members = dataCenter.channelMembers(requestMatcher.group(1));
        return new Response<>(ResponseType.List, members);
    }

    private Response<String> leave(Matcher requestMatcher) {
        dataCenter.leaveChannel(requestMatcher.group(1));
        return new Response<>(ResponseType.Successful, "");
    }
}
