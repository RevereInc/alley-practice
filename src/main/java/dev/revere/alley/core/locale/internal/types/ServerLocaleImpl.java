package dev.revere.alley.core.locale.internal.types;

import dev.revere.alley.core.locale.LocaleEntry;
import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 09/09/2025
 */
@Getter
public enum ServerLocaleImpl implements LocaleEntry {
    CRAFTING_TOGGLED(
            "messages.yml",
            "server.crafting-operations.toggled",
            "&aCrafting operations for &6{item} &aare now &6{status}&a."
    ),
    MUST_HOLD_CRAFTABLE_ITEM(
            "messages.yml",
            "server.crafting-operations.must-hold-craftable-item",
            "&cYou must be holding a craftable item to manage crafting operations."
    ),

    QUEUE_TEMPORARILY_DISABLED(
            "messages.yml",
            "server.queue.temporarily-disabled",
            "&cQueueing is temporarily disabled. Please try again later."
    ),
    QUEUE_TOGGLED(
            "messages.yml",
            "server.queue.toggled",
            "&aYou've temporarily {status} &aqueueing for all players."
    ),
    QUEUE_RELOADED(
            "messages.yml",
            "server.queue.reloaded",
            "&aSuccessfully reloaded all queues!"
    ),
    QUEUE_FORCED_PLAYER(
            "messages.yml",
            "server.queue.forced-player",
            "&aSuccessfully forced &6{player} &ainto the &6{ranked} {kit} &aqueue!"
    ),

    SPAWN_SET(
            "messages.yml",
            "server.spawn.set",
            "&aSuccessfully set the new spawn location of &6Alley Practice&a! \n &8- &7{world}: {x}, {y}, {z} (Yaw: {yaw}, Pitch: {pitch})"
    ),
    SPAWN_TELEPORTED(
            "messages.yml",
            "server.spawn.teleported",
            "&6Teleported you to spawn!"
    ),
    SPAWN_ITEMS_GIVEN(
            "messages.yml",
            "server.spawn.items-given",
            "&aSuccessfully received the spawn items!"
    ),

    ;

    private final String configName;
    private final String configPath;
    private final Object defaultValue;

    /**
     * Constructor for the ServerLocale enum.
     *
     * @param configName   The name of the configuration file.
     * @param configPath   The path to the specific string within the configuration file.
     * @param defaultValue The default value for the locale entry.
     */
    ServerLocaleImpl(String configName, String configPath, Object defaultValue) {
        this.configName = configName;
        this.configPath = configPath;
        this.defaultValue = defaultValue;
    }
}