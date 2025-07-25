package dev.revere.alley.game.party.enums;

import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @date 24/05/2024 - 22:57
 */
@Getter
public enum PartyState {
    PRIVATE("Private", "Only invited players can join"),
    PUBLIC("Public", "Everyone can join");

    private final String name;
    private final String description;

    /**
     * Constructor for the EnumPartyState
     *
     * @param name        The name of the party state
     * @param description The description of the party state
     */
    PartyState(String name, String description) {
        this.name = name;
        this.description = description;
    }
}