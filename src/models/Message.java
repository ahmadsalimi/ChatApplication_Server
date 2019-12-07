package models;

import com.google.gson.annotations.Expose;

public class Message {
    @Expose
    private final String sender;
    @Expose
    private final String content;

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }
}
