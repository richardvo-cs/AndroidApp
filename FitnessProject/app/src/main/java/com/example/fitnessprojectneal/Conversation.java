package com.example.fitnessprojectneal;

import java.io.Serializable;

public class Conversation implements Serializable {
    private String name;
    private String lastMessage;
    private long timestamp;
    private boolean hasUnreadMessages;

    public Conversation(String name, String lastMessage, long timestamp, boolean hasUnreadMessages) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.hasUnreadMessages = hasUnreadMessages;
    }

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean hasUnreadMessages() {
        return hasUnreadMessages;
    }
}
