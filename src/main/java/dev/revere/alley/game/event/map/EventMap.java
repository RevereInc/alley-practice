package dev.revere.alley.game.event.map;

import dev.revere.alley.game.event.map.enums.EventMapType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
@Getter
@Setter
public class EventMap {
    private EventMapType type;

    private String displayName;
    private String name;

    private Location spawn;
    private Location center;

    private Location pos1;
    private Location pos2;

    /**
     * Constructor for the EventMap class.
     *
     * @param name the name of the map.
     */
    public EventMap(String name) {
        this.name = name;
        this.displayName = "&6" + name;
        this.spawn = null;
        this.center = null;
        this.pos1 = null;
        this.pos2 = null;
        this.type = null;
    }
}