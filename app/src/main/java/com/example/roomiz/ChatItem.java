package com.example.roomiz;

public class ChatItem {

    private int ChatProfileImage;
    private String FullName;
    private String LastMessage;

    public ChatItem(int chatProfileImage, String fullName, String lastMessage) {
        ChatProfileImage = chatProfileImage;
        FullName = fullName;
        LastMessage = lastMessage;
    }

    public int getChatProfileImage() {
        return ChatProfileImage;
    }

    public String getFullName() {
        return FullName;
    }

    public String getLastMessage() {
        return LastMessage;
    }
    public void setLastMessage(String lastMessage) {
        LastMessage = lastMessage;
    }
}
