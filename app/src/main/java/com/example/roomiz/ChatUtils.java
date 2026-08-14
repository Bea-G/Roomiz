package com.example.roomiz;

public final class ChatUtils {
    private ChatUtils() { }

    public static String conversationId(String name) {
        String normalized = name == null ? "" : name.toLowerCase().replaceAll("[^a-z0-9]+", "_");
        normalized = normalized.replaceAll("^_+|_+$", "");
        return "me_" + normalized;
    }

    public static String firstName(String name) {
        String trimmed = name == null ? "" : name.trim();
        int firstSpace = trimmed.indexOf(' ');
        return firstSpace == -1 ? trimmed : trimmed.substring(0, firstSpace);
    }
}