package models;

public class User {
    private final String username;
    private final String password;
    private Channel currentChannel;
    private String authToken;
    private int lastRefreshedIndex;

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

    public int getLastRefreshedIndex() {
        return lastRefreshedIndex;
    }

    public void setCurrentChannel(Channel currentChannel) {
        this.currentChannel = currentChannel;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setLastRefreshedIndex(int lastRefreshedIndex) {
        this.lastRefreshedIndex = lastRefreshedIndex;
    }
}
