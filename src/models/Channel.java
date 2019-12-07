package models;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Channel {
    public static final Channel Empty = new Channel("empty");
    @Expose
    private ArrayList<Message> messages = new ArrayList<>();
    @Expose
    private String name;
    private HashSet<String> members = new HashSet<>();

    public Channel(String name) {
        this.name = name;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void addMember(String member) {
        if (members == null) {
            members = new HashSet<>();
        }
        members.add(member);
    }

    public String getName() {
        return name;
    }

    public List<String> getMembersList() {
        return new ArrayList<>(members);
    }

    public void removeMember(String username) {
        members.remove(username);
    }

    public int getMessagesCount() {
        return messages.size();
    }

    public List<Message> getRefreshedMessages(int from) {
        if (messages.size() <= from) {
            return Collections.emptyList();
        }
        return messages.subList(from, messages.size());
    }
}
