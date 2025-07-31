package dev.revere.alley.game.event.enums;

import lombok.Getter;

/**
 * @author Emmy
 * @project alley-practice
 * @since 31/07/2025
 */
@Getter
public enum EventTeamSize {
    SOLO(1, "1v1"),
    DUO(2, "2v2"),
    TRIO(3, "3v3"),

    ;

    private final int size;
    private final String displayName;

    /**
     * Constructor for the EventTeamSize enum.
     *
     * @param size        the size of the team.
     * @param displayName the display name of the team size.
     */
    EventTeamSize(int size, String displayName) {
        this.size = size;
        this.displayName = displayName;
    }
}