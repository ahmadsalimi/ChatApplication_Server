package server;

import exception.BadRequestException;
import logger.Logger;
import models.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
            return new Response<>(ResponseType.Error, "Unknown request pattern.").toJson();
        } catch (BadRequestException e) {
            return new Response<>(ResponseType.Error, e.getMessage()).toJson();
        }
    }

    private Response<String> register(Matcher request) {
        User user = new User(request.group(1), request.group(2));
        dataCenter.registerUser(user);
        return new Response<>(ResponseType.Successful, "");
    }

    private Response<String> login(Matcher requestMatcher) {
        User user = dataCenter.getUserByUsername(requestMatcher.group(1));
        dataCenter.authenticateLogin(user, requestMatcher.group(2));
        dataCenter.loginUser(user);
        return new Response<>(ResponseType.AuthToken, user.getAuthToken());
    }

    private Response<String> createChannel(Matcher requestMatcher) {
        dataCenter.authenticate(requestMatcher.group(2));
        Channel channel = new Channel(requestMatcher.group(1));
        dataCenter.addChannel(channel);
        User user = dataCenter.getUserByAuthToken(requestMatcher.group(2));
        user.setCurrentChannel(channel);
        channel.addMember(user);
        return new Response<>(ResponseType.Successful, "");
    }

    private Response<String> joinChannel(Matcher requestMatcher) {
        dataCenter.authenticate(requestMatcher.group(2));
        Channel channel = dataCenter.getChannel(requestMatcher.group(1));
        User user = dataCenter.getUserByAuthToken(requestMatcher.group(2));
        if (user.getCurrentChannel() != null) {
            throw new BadRequestException("You are in another channel.");
        }
        channel.addMember(user);
        user.setCurrentChannel(channel);
        return new Response<>(ResponseType.Successful, "");
    }

    private Response<String> logout(Matcher requestMatcher) {
        dataCenter.authenticate(requestMatcher.group(1));
        dataCenter.logoutUser(requestMatcher.group(1));
        return new Response<>(ResponseType.Successful, "");
    }

    private Response<String> sendMessage(Matcher requestMatcher) {
        dataCenter.authenticate(requestMatcher.group(2));
        User user = dataCenter.getUserByAuthToken(requestMatcher.group(2));
        if (user.getCurrentChannel() == null) {
            throw new BadRequestException("You aren't in any channel");
        }
        Message message = new Message(user, requestMatcher.group(1));
        user.getCurrentChannel().addMessage(message);
        return new Response<>(ResponseType.Successful, "");
    }

    private Response<List<String>> refresh(Matcher requestMatcher) {
        dataCenter.authenticate(requestMatcher.group(1));
        User user = dataCenter.getUserByAuthToken(requestMatcher.group(2));
        if (user.getCurrentChannel() == null) {
            throw new BadRequestException("You aren't in any channel");
        }
        List<String> messages = user.getCurrentChannel().getRefreshedMessages(user.getLastRefreshedIndex() + 1);
        user.setLastRefreshedIndex(user.getCurrentChannel().getLastMessageIndex());
        return new Response<>(ResponseType.List, messages);
    }

    private Response<List<String>> channelMembers(Matcher requestMatcher) {
        dataCenter.authenticate(requestMatcher.group(1));
        User user = dataCenter.getUserByAuthToken(requestMatcher.group(2));
        if (user.getCurrentChannel() == null) {
            throw new BadRequestException("You aren't in any channel");
        }
        List<String> members = user.getCurrentChannel().getMembers().stream().map(User::getUsername).collect(Collectors.toList());
        return new Response<>(ResponseType.List, members);
    }

    private Response<String> leave(Matcher requestMatcher) {
        dataCenter.authenticate(requestMatcher.group(1));
        User user = dataCenter.getUserByAuthToken(requestMatcher.group(2));
        if (user.getCurrentChannel() == null) {
            throw new BadRequestException("You aren't in any channel");
        }
        user.getCurrentChannel().removeMember(user);
        user.leaveChannel();
        return new Response<>(ResponseType.Successful, "");
    }
}
