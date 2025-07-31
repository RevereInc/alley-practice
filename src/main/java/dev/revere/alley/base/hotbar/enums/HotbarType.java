package dev.revere.alley.base.hotbar.enums;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public enum HotbarType {
    LOBBY,
    PARTY,
    QUEUE,
    SPECTATOR,
    TOURNAMENT,
    EVENT,

    ;

    /**
     * Returns a string representation of all available hotbar types.
     *
     * @return A comma-separated string of all hotbar types in lowercase.
     */
    public static String availableTypes() {
        StringBuilder types = new StringBuilder();
        for (HotbarType type : values()) {
            if (types.length() > 0) {
                types.append(", ");
            }
            types.append(type.name().toLowerCase());
        }
        return types.toString();
    }
}