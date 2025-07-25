package dev.revere.alley.profile.enums;

import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @date 13/10/2024 - 10:17
 */
@Getter
public enum WorldTime {
    DAY("DAY"),
    SUNSET("SUNSET"),
    NIGHT("NIGHT"),
    DEFAULT("DEFAULT");

    private final String name;

    /**
     * Constructor for the EnumWorldTime enum.
     *
     * @param name The name of the world time type.
     */
    WorldTime(String name) {
        this.name = name;
    }

    /**
     * Get an EnumWorldTime by its name.
     *
     * @param name The name of the EnumWorldTime.
     * @return The EnumWorldTimeType with the given name.
     */
    public static WorldTime getByName(String name) {
        for (WorldTime worldTimeType : values()) {
            if (worldTimeType.getName().equalsIgnoreCase(name)) {
                return worldTimeType;
            }
        }
        return null;
    }
}