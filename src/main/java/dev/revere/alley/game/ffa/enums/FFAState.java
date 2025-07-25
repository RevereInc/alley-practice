package dev.revere.alley.game.ffa.enums;

import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 14:25
 */
@Getter
public enum FFAState {
    SPAWN("Spawn", "The player is in the safezone."),
    FIGHTING("Fighting", "The player is fighting outside safezone.");

    private final String name;
    private final String description;

    /**
     * Constructor for the EnumFFAState enum.
     *
     * @param name        The name of the state.
     * @param description The description of the state.
     */
    FFAState(String name, String description) {
        this.name = name;
        this.description = description;
    }
}