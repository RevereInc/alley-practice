package dev.revere.alley.core.enums;

import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 26/04/2025
 */
@Getter
public enum EnumCoreType {
    DEFAULT("Default", "Revere Inc."),
    PHOENIX("Phoenix", "Refine Development"),
    AQUA("AquaCore", "Activated_, FaceSlap_"),
    HELIUM("Helium", "Plasma Services"),

    ;

    private final String pluginName;
    private final String pluginAuthor;

    /**
     * Constructor for the EnumCoreType enum.
     *
     * @param pluginName   The name of the plugin.
     * @param pluginAuthor The author of the plugin.
     */
    EnumCoreType(String pluginName, String pluginAuthor) {
        this.pluginName = pluginName;
        this.pluginAuthor = pluginAuthor;
    }
}