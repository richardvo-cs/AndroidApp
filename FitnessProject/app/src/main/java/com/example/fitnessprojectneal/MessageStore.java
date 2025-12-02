package com.example.fitnessprojectneal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageStore {

    private static final Map<String, List<Message>> conversationMessages = new HashMap<>();

    public static List<Message> getMessages(String conversationId) {
        if (!conversationMessages.containsKey(conversationId)) {
            if ("John Doe".equals(conversationId)) {
                List<Message> messages = new ArrayList<>();
                messages.add(new Message("Hello!", System.currentTimeMillis() - 86400000, false));
                messages.add(new Message("Hi there!", System.currentTimeMillis() - 86300000, true));
                conversationMessages.put(conversationId, messages);
            } else {
                conversationMessages.put(conversationId, new ArrayList<>());
            }
        }
        return conversationMessages.get(conversationId);
    }

    public static void addMessage(String conversationId, Message message) {
        getMessages(conversationId).add(message);
    }
}
