package dev.revere.alley.profile.enums;

import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @date 22/10/2024 - 11:57
 */
@Getter
public enum ChatChannel {
    GLOBAL("Global", "The players chat channel is set to default."),
    PARTY("Party", "The players chat channel is set to party.");

    private final String name;
    private final String description;

    /**
     * Constructor for the EnumChatChannel class.
     *
     * @param name        The name of the chat channel.
     * @param description The description of the chat channel.
     */
    ChatChannel(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Get the chat channels sorted.
     *
     * @return The chat channels sorted.
     */
    public static String getChatChannelsSorted() {
        StringBuilder stringBuilder = new StringBuilder();
        for (ChatChannel chatChannel : ChatChannel.values()) {
            stringBuilder.append(chatChannel.getName()).append(", ");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 2);
        return stringBuilder.toString();
    }

    /**
     * Get the exact chat channel.
     *
     * @param chatChannel The chat channel to get.
     * @return The exact chat channel.
     */
    public static String getExactChatChannel(String chatChannel, boolean upperCase) {
        for (ChatChannel enumChatChannel : ChatChannel.values()) {
            if (enumChatChannel.getName().equalsIgnoreCase(chatChannel)) {
                if (upperCase) {
                    return enumChatChannel.toString().toUpperCase();
                } else {
                    return enumChatChannel.getName();
                }
            }
        }
        return null;
    }
}