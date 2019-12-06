package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Channel {
    public static final Channel Empty = new Channel("empty");
    private final ArrayList<Message> messages = new ArrayList<>();
    private final String name;
    private final HashSet<User> members = new HashSet<>();

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

    public List<String> getMembersList() {
        return members.stream().map(User::getUsername).collect(Collectors.toList());
    }

    public void removeMember(User user) {
        members.remove(user);
    }

    public int getMessagesCount() {
        return messages.size();
    }

    public List<String> getRefreshedMessages(int from) {
        if (messages.size() <= from) {
            return Collections.emptyList();
        }
        return messages.subList(from, messages.size()).stream().map(message -> message.getSender().getUsername() + ": " + message.getContent()).collect(Collectors.toList());
    }
}
