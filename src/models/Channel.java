package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Channel {
    public static final Channel Empty = new Channel("empty");
    private final ArrayList<Message> messages = new ArrayList<>();
    private final String name;
    private final ArrayList<User> members = new ArrayList<>();

    public Channel(String name) {
        this.name = name;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void addMember(User member) {
        members.add(member);
    }

    public String getName() {
        return name;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public void removeMember(User user) {
        members.remove(user);
    }

    public int getLastMessageIndex() {
        return messages.size() - 1;
    }

    public List<String> getRefreshedMessages(int from) {
        if (messages.size() <= from) {
            return Collections.emptyList();
        }
        return messages.subList(from, messages.size()).stream().map(message -> message.getSender() + ": " + message.getContent()).collect(Collectors.toList());
    }
}
