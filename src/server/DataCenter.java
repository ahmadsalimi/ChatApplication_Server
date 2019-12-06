package server;

import exception.BadRequestException;
import json.JsonFileReader;
import logger.LogLevel;
import logger.Logger;
import models.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DataCenter {
    private final AuthTokenGenerator authTokenGenerator = new AuthTokenGenerator();
    private final Logger logger;
    private HashMap<String, User> usersByUsername = new HashMap<>();
    private HashMap<String, User> onlineUsersByAuthToken = new HashMap<>();
    private HashMap<String, Channel> channelsByName = new HashMap<>();

    public DataCenter(Logger logger) {
        this.logger = logger;
    }

    public void initFromFile() {
        JsonFileReader reader = new JsonFileReader();
        initUsers(reader);
        initChannels(reader);
    }

    private void initChannels(JsonFileReader reader) {
        File channelsDirectory = new File(Config.getInstance().getChannelsPath());
        File[] channelsFiles = channelsDirectory.listFiles();

        if (channelsFiles != null) {
            Arrays.stream(channelsFiles).map(file -> {
                try {
                    return reader.read(file, Channel.class);
                } catch (FileNotFoundException e) {
                    logger.log(LogLevel.Error, e.getMessage());
                }
                return Channel.Empty;
            }).forEach(this::addChannel);
        }

        logger.log(LogLevel.Info, "Channels initialized from file successfully");
    }

    private void initUsers(JsonFileReader reader) {
        File usersDirectory = new File(Config.getInstance().getUsersPath());
        File[] usersFiles = usersDirectory.listFiles();

        if (usersFiles != null) {
            Arrays.stream(usersFiles).map(file -> {
                try {
                    return reader.read(file, User.class);
                } catch (FileNotFoundException e) {
                    logger.log(LogLevel.Error, e.getMessage());
                }
                return User.Empty;
            }).forEach(this::registerUser);
        }

        logger.log(LogLevel.Info, "Users initialized from file successfully");
    }

    private User authenticate(String authToken) {
        if (!onlineUsersByAuthToken.containsKey(authToken)) {
            throw new BadRequestException("Authentication failed!");
        }
        return onlineUsersByAuthToken.get(authToken);
    }

    private User authenticateLogin(String username, String password) {
        User user = getUserByUsername(username);
        if (!user.getPassword().equals(password)) {
            throw new BadRequestException("Wrong password.");
        }
        if (user.getAuthToken() != null) {
            throw new BadRequestException("The user " + user.getUsername() + " is already logged in.");
        }
        return user;
    }

    private Channel getChannelByName(String name) {
        if (!channelsByName.containsKey(name)) {
            throw new BadRequestException("Channel not found.");
        }
        return channelsByName.get(name);
    }

    private User getUserByUsername(String username) {
        if (!usersByUsername.containsKey(username)) {
            throw new BadRequestException("Username is not valid.");
        }
        return usersByUsername.get(username);
    }

    private void addChannel(Channel channel) {
        if (channelsByName.containsKey(channel.getName())) {
            throw new BadRequestException("Channel name is not available.");
        }
        channelsByName.put(channel.getName(), channel);
        logger.log(LogLevel.Info, "Channel " + channel.getName() + " successfully Created.");
    }

    private void registerUser(User user) {
        if (usersByUsername.containsKey(user.getUsername())) {
            throw new BadRequestException("this username is not available.");
        }
        usersByUsername.put(user.getUsername(), user);
        logger.log(LogLevel.Info, "User " + user.getUsername() + " successfully registered.");
    }

    public void registerUser(String username, String password) {
        User user = new User(username, password);
        registerUser(user);
    }

    public String loginUser(String username, String password) {
        User user = authenticateLogin(username, password);
        user.setAuthToken(authTokenGenerator.generateNewToken());
        onlineUsersByAuthToken.put(user.getAuthToken(), user);
        logger.log(LogLevel.Info, "User " + user.getUsername() + " successfully logged in.");
        return user.getAuthToken();
    }

    public void logoutUser(String authToken) {
        User user = authenticate(authToken);
        user.setAuthToken(null);
        onlineUsersByAuthToken.remove(authToken);
        logger.log(LogLevel.Info, "User " + user.getUsername() + " successfully logged out.");
    }

    public void sendMessage(String authToken, String content) {
        User user = authenticate(authToken);
        if (user.getCurrentChannel() == null) {
            throw new BadRequestException("You aren't in any channel");
        }
        Message message = new Message(user, content);
        user.getCurrentChannel().addMessage(message);
    }

    public void createChannel(String authToken, String channelName) {
        User user = authenticate(authToken);
        Channel channel = new Channel(channelName);
        addChannel(channel);
        user.setCurrentChannel(channel);
        channel.addMember(user);
    }

    public void joinChannel(String authToken, String channelName) {
        User user = authenticate(authToken);
        Channel channel = getChannelByName(channelName);
        if (user.getCurrentChannel() != null) {
            throw new BadRequestException("You are in another channel.");
        }
        channel.addMember(user);
        user.setCurrentChannel(channel);
    }

    public List<String> refresh(String authToken) {
        User user = authenticate(authToken);
        if (user.getCurrentChannel() == null) {
            throw new BadRequestException("You aren't in any channel");
        }
        List<String> messages = user.getCurrentChannel().getRefreshedMessages(user.getRefreshIndex());
        user.setRefreshIndex(user.getCurrentChannel().getMessagesCount());
        return messages;
    }

    public List<String> channelMembers(String authToken) {
        User user = authenticate(authToken);
        if (user.getCurrentChannel() == null) {
            throw new BadRequestException("You aren't in any channel");
        }
        return user.getCurrentChannel().getMembers().stream().map(User::getUsername).collect(Collectors.toList());
    }

    public void leaveChannel(String authToken) {
        User user = authenticate(authToken);
        if (user.getCurrentChannel() == null) {
            throw new BadRequestException("You aren't in any channel");
        }
        user.getCurrentChannel().removeMember(user);
        user.leaveChannel();
    }
}
