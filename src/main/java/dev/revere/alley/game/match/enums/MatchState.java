package dev.revere.alley.game.match.enums;

import lombok.Getter;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Getter
public enum MatchState {
    STARTING("Starting", "Starting"),
    RUNNING("Running", "In-Game"),
    ENDING_ROUND("Ending Round", "Ending"),
    RESTARTING_ROUND("Restarting Round", "Restarting"),
    ENDING_MATCH("Ending Match", "Ending");

    private final String name;
    private final String description;

    /**
     * Constructor for the EnumMatchState enum.
     *
     * @param name        The name of the match state.
     * @param description The description of the match state.
     */
    MatchState(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
