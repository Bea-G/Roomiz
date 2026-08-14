package com.example.roomiz;

import com.google.firebase.Timestamp;

public class ChatMessage {
    private final String text;
    private final String senderId;
    private final Timestamp timestamp;

    public ChatMessage(String text, String senderId, Timestamp timestamp) {
        this.text = text;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }

    public String getText() { return text; }
    public String getSenderId() { return senderId; }
    public Timestamp getTimestamp() { return timestamp; }
}