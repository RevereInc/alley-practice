package dev.revere.alley.core.locale.internal.impl;

import dev.revere.alley.core.locale.LocaleEntry;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author Emmy
 * @project Alley
 * @since 09/09/2025
 */
@Getter
public enum ServerLocaleImpl implements LocaleEntry {
    AUTO_UPDATE_BOOLEAN("settings.yml", "plugin.auto-update", true),

    CHAT_FORMAT_GLOBAL("settings.yml", "server.chat-format.global", null), //TODO
    CHAT_FORMAT_PARTY("settings.yml", "server.chat-format.party", "&7[&6Party&7] &6{player}&7: &f{message}"),

    LOCATION_SPAWN("settings.yml", "server.locations.spawn", "world:0.5:73.0:0.5:90.0:0.0"),

    BLOCKED_CRAFTING_ITEMS_LIST("settings.yml", "crafting-operations.blocked-items", Collections.emptyList()),

    PROFANITY_FILTER_ADD_DEFAULT_WORDS_BOOLEAN("settings.yml", "profanity-filter.add-default-words", true),
    PROFANITY_FILTER_STAFF_NOTIFICATION_FORMAT("settings.yml", "profanity-filter.staff-notification-format", "&6[S] &f(&6Alley: &fProfanity Detected) &c{player}&f: &7{message}"),
    PROFANITY_FILTER_FILTERED_WORDS_LIST("settings.yml", "profanity-filter.filtered-words", Arrays.asList("shit", "fuck", "bitch")),

    ESSENTIAL_EMOJI_FEATURE_BOOLEAN("settings.yml", "server.essentials.emoji-feature", true),

    GRANT_COSMETIC_PERMISSION_COMMAND("settings.yml", "commands.grant-cosmetic-permission", "permission set {player} {permission}"),

    COMMAND_SYNTAX_BYPASS_PERMISSION("settings.yml", "permissions.syntax-bypass-permission", "alley.syntax.bypass"),
    COMMAND_ANTI_SYNTAX_MESSAGE("settings.yml", "commands.anti-syntax-message", "&cYou cannot execute {argument}."),

//    RESTART_SCHEDULER_BOOLEAN("settings.yml", "plugin.restart-scheduler.enabled", true),
//    RESTART_SCHEDULER_TIME("settings.yml", "plugin.restart-scheduler.time", "24h"),
//    RESTART_SCHEDULER_WARNING_TIMES("settings.yml", "plugin.restart-scheduler.warning-times", Arrays.asList("15m", "10m", "5m", "1m", "30s", "10s", "5s", "4s", "3s", "2s", "1s")),

    CRAFTING_TOGGLED("messages.yml", "crafting-operations.toggled", "&aCrafting operations for &6{item} &aare now &6{status}&a."),
    MUST_HOLD_CRAFTABLE_ITEM("messages.yml", "crafting-operations.must-hold-craftable-item", "&cYou must be holding a craftable item to manage crafting operations."),

    GIVEN_ITEM("messages.yml", "give-item.given-item", "&aYou have been given &6{amount} &a{item-name}&a."),
    ITEM_NOT_CONFIGURED("messages.yml", "give-item.item-not-configured", "&cThe item named &6{item-name} &cis not configured."),

    QUEUE_TEMPORARILY_DISABLED("messages.yml", "queue.temporarily-disabled", "&cQueueing is temporarily disabled. Please try again later."),
    QUEUE_TOGGLED("messages.yml", "queue.toggled", "&aYou've temporarily {status} &aqueueing for all players."),
    QUEUE_RELOADED("messages.yml", "queue.reloaded", "&aSuccessfully reloaded all queues!"),
    QUEUE_FORCED_PLAYER("messages.yml", "queue.forced-player", "&aSuccessfully forced &6{player} &ainto the &6{ranked} {kit} &aqueue!"),

    SPAWN_SET("messages.yml", "spawn.set", "&aSuccessfully set the new spawn location of &6Alley Practice&a! \n &8- &7{world}: {x}, {y}, {z} (Yaw: {yaw}, Pitch: {pitch})"),
    SPAWN_TELEPORTED("messages.yml", "spawn.teleported", "&6Teleported you to spawn!"),
    SPAWN_ITEMS_GIVEN("messages.yml", "spawn.items-given", "&aSuccessfully received the spawn items!"),

    EXPLOSIVE_ENABLED("settings.yml", "explosive.enabled", true),
    EXPLOSIVE_FIREBALL_EXPLOSION_RANGE_VALUE("settings.yml", "explosive.values.fireball.explosion-range", 3.0),
    EXPLOSIVE_FIREBALL_VERTICAL_KB_VALUE("settings.yml", "explosive.values.fireball.vertical-kb", 2.2),
    EXPLOSIVE_FIREBALL_HORIZONTAL_KB_VALUE("settings.yml", "explosive.values.fireball.horizontal-kb", 2.5),
    EXPLOSIVE_FIREBALL_THROW_SPEED_VALUE("settings.yml", "explosive.values.fireball.throw-speed", 0.7),
    EXPLOSIVE_TNT_EXPLOSION_RANGE_VALUE("settings.yml", "explosive.values.tnt.explosion-range", 3.0),
    EXPLOSIVE_TNT_FUSE_TICKS_VALUE("settings.yml", "explosive.values.tnt.fuse-ticks", 60),
    EXPLOSIVE_SETTING_UPDATED("messages.yml", "command-messages.explosive.setting-updated", "&aSuccessfully set the explosive {setting-name} value to &6{setting-value}&a."),

    GAME_BLOCKED_COMMANDS_DURING_MATCH_LIST("settings.yml", "game.blocked-commands", Arrays.asList("kill", "tp")),
    GAME_LIVES_PER_MATCH("settings.yml", "game.lives-per-game", 3),
    GAME_ARENA_PORTAL_RADIUS("settings.yml", "game.portal-radius", 5),

    MONGO_CREDENTIALS_URI("database/database.yml", "mongo.uri", "mongodb://localhost:27017"),
    MONGO_CREDENTIALS_DATABASE("database/database.yml", "mongo.database", "A" + "l" + "l" + "e" + "y"),

    TIPS_LIST("messages.yml", "tips", Arrays.asList(
            "&6Tip: &fUse F5 to look at your opponent one last time before they end you.",
            "&6Tip: &fW-tap like your life depends on it. It kinda does.",
            "&6Tip: &fKeep your crosshair at head level... unless you like tickling toes.",
            "&6Tip: &fPractice spacing — or just hug them and pray."
    )),

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