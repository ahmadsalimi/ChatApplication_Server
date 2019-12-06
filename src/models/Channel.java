package models;

import java.util.ArrayList;

public class Channel {
    private final ArrayList<Message> messages = new ArrayList<>();
    private final String channelName;
    private final ArrayList<User> members = new ArrayList<>();

    public Channel(String channelName) {
        this.channelName = channelName;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void addMember(User member) {
        members.add(member);
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public String getChannelName() {
        return channelName;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public boolean removeMember(User user) {
        return members.remove(user);
    }
}
