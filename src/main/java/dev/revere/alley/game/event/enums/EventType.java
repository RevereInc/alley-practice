package dev.revere.alley.game.event.enums;

import lombok.Getter;
import org.bukkit.Material;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
@Getter
public enum EventType {
    SUMO("Sumo", Material.LEASH, 0),

    ;

    private final String name;
    private final Material icon;
    private final int durability;

    /**
     * Constructor for the EventType class.
     *
     * @param name       the name of the event.
     * @param icon       the icon of the event.
     * @param durability the icon durability/data of the map
     */
    EventType(String name, Material icon, int durability) {
        this.name = name;
        this.icon = icon;
        this.durability = durability;
    }
}