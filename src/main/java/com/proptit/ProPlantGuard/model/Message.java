package com.proptit.ProPlantGuard.model;

public class Message {
    private final String sender;
    private final String content;
    private final boolean isUserMessage;

    public Message(String sender, String content, boolean isUserMessage) {
        this.sender = sender;
        this.content = content;
        this.isUserMessage = isUserMessage;
    }

    public String getSender() { return sender; }
    public String getContent() { return content; }
    public boolean isUserMessage() { return isUserMessage; }
}

