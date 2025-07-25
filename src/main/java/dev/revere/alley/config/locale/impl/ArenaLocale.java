package dev.revere.alley.config.locale.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.config.locale.Locale;
import dev.revere.alley.util.chat.CC;

/**
 * @author Emmy
 * @project Alley
 * @since 26/06/2025
 */
public enum ArenaLocale implements Locale {

    ALREADY_EXISTS("messages.yml", "arena.error.already-exists"),
    NOT_FOUND("messages.yml", "arena.error.not-found"),
    NO_SELECTION("messages.yml", "arena.error.no-selection"),
    KIT_ALREADY_ADDED_TO_ARENA("messages.yml", "arena.error.kit-already-added-to-arena"),
    ARENA_DOES_NOT_HAVE_KIT("messages.yml", "arena.error.arena-does-not-have-kit"),

    SELECTION_TOOL_ADDED("messages.yml", "arena.manage.selection-tool.added"),
    SELECTION_TOOL_REMOVED("messages.yml", "arena.manage.selection-tool.removed"),

    CENTER_SET("messages.yml", "arena.data.center-set"),
    CUBOID_SET("messages.yml", "arena.data.cuboid-set"),
    HEIGHT_LIMIT_SET("messages.yml", "arena.data.height-limit-set"),
    PORTAL_SET("messages.yml", "arena.data.portal-set"),
    VOID_LEVEL_SET("messages.yml", "arena.data.void-level-set"),
    TOGGLED("messages.yml", "arena.data.toggled"),
    BLUE_SPAWN_SET("messages.yml", "arena.data.blue-spawn-set"),
    RED_SPAWN_SET("messages.yml", "arena.data.red-spawn-set"),
    FFA_SPAWN_SET("messages.yml", "arena.data.ffa-spawn-set"),
    KIT_ADDED("messages.yml", "arena.data.kit-added"),
    KIT_REMOVED("messages.yml", "arena.data.kit-removed"),

    CREATED("messages.yml", "arena.manage.created"),
    DELETED("messages.yml", "arena.manage.deleted"),

    SAVED_ALL("messages.yml", "arena.storage.saved-all"),

    ;


    private final String configName;
    private final String configString;

    /**
     * Constructor for the ArenaLocale enum.
     *
     * @param configName   The name of the config.
     * @param configString The string of the config.
     */
    ArenaLocale(String configName, String configString) {
        this.configName = configName;
        this.configString = configString;
    }

    /**
     * Gets the String from the config.
     *
     * @return The message from the config.
     */
    @Override
    public String getMessage() {
        return CC.translate(Alley.getInstance().getService(ConfigService.class).getConfig(this.configName).getString(this.configString));
    }
}