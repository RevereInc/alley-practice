package dev.revere.alley.game.match.player.enums;

import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 13/06/2025
 */
@Getter
public enum BaseRaiderRole {
    RAIDER("&cRaider"),
    TRAPPER("&eTrapper");

    private final String displayName;

    /**
     * Constructor for the EnumBaseRaiderRole enum.
     *
     * @param displayName The display name of the raider role.
     */
    BaseRaiderRole(String displayName) {
        this.displayName = displayName;
    }
}