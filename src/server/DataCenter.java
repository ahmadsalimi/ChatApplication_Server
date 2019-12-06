package server;

import exception.BadRequestException;
import json.JsonFileReader;
import logger.LogLevel;
import logger.Logger;
import models.Channel;
import models.Config;
import models.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;

public class DataCenter {
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
    }

    public Channel getChannel(String name) {
        return channelsByName.get(name);
    }

    public boolean addChannel(Channel channel) {
        if (!channelsByName.containsKey(channel.getName())) {
            channelsByName.put(channel.getName(), channel);
            return true;
        }
        return false;
    }

    public User getUserByUsername(String username) {
        return usersByUsername.get(username);
    }

    public User getUserByAuthToken(String authToken) {
        return onlineUsersByAuthToken.get(authToken);
    }

    public void registerUser(User user) {
        if (usersByUsername.containsKey(user.getUsername())) {
            throw new BadRequestException("this username is redundant.");
        }
        usersByUsername.put(user.getUsername(), user);
        logger.log(LogLevel.Info, "User " + user.getUsername() + " successfully registered.");
    }

    public boolean loginUser(User user) {
        if (!onlineUsersByAuthToken.containsKey(user.getAuthToken())) {
            onlineUsersByAuthToken.put(user.getAuthToken(), user);
            return true;
        }
        return false;
    }

    public boolean logoutUser(String authToken) {
        if (!onlineUsersByAuthToken.containsKey(authToken)) {
            onlineUsersByAuthToken.remove(authToken);
            return true;
        }
        return false;
    }
}
