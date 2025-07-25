package dev.revere.alley.adapter.knockback.enums;

import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 26/04/2025
 */
@Getter
public enum KnockbackType {
    DEFAULT("Default", "Revere Incorporate"),
    ZONE("ZoneSpigot", "Revere Incorporate"),

    ;

    private final String spigotName;
    private final String spigotAuthor;

    /**
     * Constructor for the EnumKnockbackType enum.
     *
     * @param spigotName   The name of the spigot.
     * @param spigotAuthor The author of the spigot.
     */
    KnockbackType(String spigotName, String spigotAuthor) {
        this.spigotName = spigotName;
        this.spigotAuthor = spigotAuthor;
    }
}