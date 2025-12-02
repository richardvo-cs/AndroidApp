package com.example.fitnessprojectneal;

public class Message {
    private String text;
    private long timestamp;
    private boolean isSentByUser; // To distinguish between sent and received

    public Message(String text, long timestamp, boolean isSentByUser) {
        this.text = text;
        this.timestamp = timestamp;
        this.isSentByUser = isSentByUser;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isSentByUser() {
        return isSentByUser;
    }
}
