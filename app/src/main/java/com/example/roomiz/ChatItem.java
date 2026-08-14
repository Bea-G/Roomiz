package com.example.roomiz;

public class ChatItem {
    private final String profileId;
    private final String fullName;
    private final String imageName;
    private String lastMessage;

    public ChatItem(String profileId, String fullName, String imageName) {
        this.profileId = profileId;
        this.fullName = fullName;
        this.imageName = imageName;
        this.lastMessage = "";
    }

    public String getProfileId() { return profileId; }
    public String getFullName() { return fullName; }
    public String getImageName() { return imageName; }
    public String getLastMessage() { return lastMessage; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }
}