package com.example.roomiz;

public class ChatItem {
    private final String profileId;  // Profile document id.
    private final String fullName;  // Profile name.
    private final String imageName;  // Local image name.
    private String lastMessage;  // Latest message preview.

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