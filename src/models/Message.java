package models;

import com.google.gson.annotations.Expose;

public class Message {
    @Expose
    private String sender;
    @Expose
    private String content;

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }
}
