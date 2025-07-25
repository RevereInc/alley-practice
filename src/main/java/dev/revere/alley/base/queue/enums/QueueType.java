package dev.revere.alley.base.queue.enums;

import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 13/03/2025
 */
@Getter
public enum QueueType {
    UNRANKED("Solo Unranked Queue"),
    DUOS("Duo Unranked Queue"),
    BOTS("Bots Unranked Queue"),
    FFA("FFA Unranked Queue"),

    ;

    private final String menuTitle;

    /**
     * Constructor for the EnumQueueType class.
     *
     * @param menuTitle The title of the menu.
     */
    QueueType(String menuTitle) {
        this.menuTitle = menuTitle;
    }
}