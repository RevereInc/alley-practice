package dev.revere.alley.feature.arena;

import dev.revere.alley.feature.arena.enums.EnumArenaType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 20/05/2024 - 16:42
 */
@Getter
@Setter
public abstract class AbstractArena {
    private List<String> kits = new ArrayList<>();

    private String name;
    private String displayName;

    private EnumArenaType type;

    private boolean enabled;

    private Location pos1;
    private Location pos2;

    private Location center;

    private Location minimum;
    private Location maximum;

    /**
     * Constructor for the Arena class.
     *
     * @param name The name of the arena.
     * @param minimum The minimum location of the arena.
     * @param maximum The maximum location of the arena.
     */
    public AbstractArena(String name, Location minimum, Location maximum) {
        this.name = name;
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public abstract void saveArena();
    public abstract void createArena();
    public abstract void deleteArena();
}