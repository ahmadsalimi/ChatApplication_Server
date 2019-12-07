package models;

import com.google.gson.annotations.Expose;

public class User {
    public static final User Empty = new User("empty", "empty");
    @Expose
    private final String username;
    @Expose
    private final String password;
    private Channel currentChannel;
    private String authToken;
    private int refreshIndex;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Channel getCurrentChannel() {
        return currentChannel;
    }

    public String getAuthToken() {
        return authToken;
    }

    public int getRefreshIndex() {
        return refreshIndex;
    }

    public void setCurrentChannel(Channel currentChannel) {
        this.currentChannel = currentChannel;
        this.refreshIndex = 0;
    }

    public void leaveChannel() {
        this.currentChannel = null;
        this.refreshIndex = 0;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setRefreshIndex(int refreshIndex) {
        this.refreshIndex = refreshIndex;
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
