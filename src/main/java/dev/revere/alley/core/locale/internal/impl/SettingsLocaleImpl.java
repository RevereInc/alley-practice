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
public enum SettingsLocaleImpl implements LocaleEntry {
    AUTO_UPDATE_BOOLEAN("settings.yml", "plugin.auto-update", true),

    BLOCKED_CRAFTING_ITEMS_LIST("settings.yml", "crafting-operations.blocked-items", Collections.emptyList()),

    COMMAND_ANTI_SYNTAX_MESSAGE("settings.yml", "commands.anti-syntax-message", "&cYou cannot execute {argument}."),

    CONFIG_ARENA_DEFAULT_DISPLAY_NAME_FFA("settings.yml", "configuration.arena-default-display-name.ffa", "&6{arena-name}"),
    CONFIG_ARENA_DEFAULT_DISPLAY_NAME_SHARED("settings.yml", "configuration.arena-default-display-name.shared", "&6{arena-name}"),
    CONFIG_ARENA_DEFAULT_DISPLAY_NAME_STANDALONE("settings.yml", "configuration.arena-default-display-name.standalone", "&6{arena-name}"),

    CONFIG_KIT_DEFAULT_DESCRIPTION("settings.yml", "configuration.default-kit-values.description", ""),
    CONFIG_KIT_DEFAULT_DISCLAIMER("settings.yml", "configuration.default-kit-values.disclaimer", "&7{kit-name} kit disclaimer."),
    CONFIG_KIT_DEFAULT_DISPLAYNAME("settings.yml", "configuration.default-kit-values.display-name", "&6{kit-name}"),
    CONFIG_KIT_DEFAULT_MENU_TITLE("settings.yml", "configuration.default-kit-values.menu-title", "&6&l{kit-name}"),

    EXPLOSIVE_ENABLED("settings.yml", "explosive.enabled", true),
    EXPLOSIVE_FIREBALL_EXPLOSION_RANGE_VALUE("settings.yml", "explosive.values.fireball.explosion-range", 3.0),
    EXPLOSIVE_FIREBALL_HORIZONTAL_KB_VALUE("settings.yml", "explosive.values.fireball.horizontal-kb", 2.5),
    EXPLOSIVE_FIREBALL_THROW_SPEED_VALUE("settings.yml", "explosive.values.fireball.throw-speed", 0.7),
    EXPLOSIVE_FIREBALL_VERTICAL_KB_VALUE("settings.yml", "explosive.values.fireball.vertical-kb", 2.2),
    EXPLOSIVE_TNT_EXPLOSION_RANGE_VALUE("settings.yml", "explosive.values.tnt.explosion-range", 3.0),
    EXPLOSIVE_TNT_FUSE_TICKS_VALUE("settings.yml", "explosive.values.tnt.fuse-ticks", 60),

    GAME_ARENA_PORTAL_RADIUS("settings.yml", "game.portal-radius", 5),
    GAME_BLOCKED_COMMANDS_DURING_MATCH_LIST("settings.yml", "game.blocked-commands", Arrays.asList("kill", "tp")),
    GAME_LIVES_PER_MATCH("settings.yml", "game.lives-per-game", 3),

    GRANT_COSMETIC_PERMISSION_COMMAND("settings.yml", "commands.grant-cosmetic-permission", "permission set {player} {permission}"),

    MONGO_CREDENTIALS_DATABASE("database/database.yml", "mongo.database", "A" + "l" + "l" + "e" + "y"),
    MONGO_CREDENTIALS_URI("database/database.yml", "mongo.uri", "mongodb://localhost:27017"),

    PERMISSION_USE_OF_COLOR_CODES_IN_CHAT("settings.yml", "permissions.color-code-usage-permission", "alley.chat.color"),
    PERMISSION_COMMAND_SYNTAX_BYPASS("settings.yml", "permissions.syntax-bypass-permission", "alley.syntax.bypass"),

    PERMISSION_DONATOR_PARTY_ARENA_SELECTOR("settings.yml", "permissions.donators.party-arena-selector", "alley.donator.duel.arena.selector"),
    PERMISSION_DONATOR_DUEL_ARENA_SELECTOR("settings.yml", "permissions.donators.duel-arena-selector", "alley.donator.duel.arena.selector"),
    PERMISSION_DONATOR_LOBBY_FLIGHT_BYPASS("settings.yml", "permissions.donators.lobby-flight-bypass", "alley.donator.lobby.flight"),
    PERMISSION_DONATOR_EMOJI_USAGE("settings.yml", "permissions.donators.emoji-usage-permission", "alley.donator.chat.emoji"),

    PROFANITY_FILTER_ADD_DEFAULT_WORDS_BOOLEAN("settings.yml", "profanity-filter.add-default-words", true),
    PROFANITY_FILTER_FILTERED_WORDS_LIST("settings.yml", "profanity-filter.filtered-words", Arrays.asList("shit", "fuck", "bitch")),
    PROFANITY_FILTER_STAFF_NOTIFICATION_FORMAT("settings.yml", "profanity-filter.staff-notification-format", "&6[S] &f(&6Alley: &fProfanity Detected) &c{player}&f: &7{message}"),

    SERVER_CHAT_FORMAT_ENABLED_BOOLEAN("settings.yml", "server.chat-format.global.enabled", true),
    SERVER_CHAT_FORMAT_GLOBAL("settings.yml", "server.chat-format.global.format", "&7[{level}&7]&r {prefix}{rank-color}{name-color}{player}{suffix}{tag}{separator}{message}"),
    SERVER_CHAT_FORMAT_TAG_APPEARANCE_FORMAT("settings.yml", "server.chat-format.tag-appearance.format", " {tag-color}{tag-prefix}"),
    SERVER_CHAT_FORMAT_SEPARATOR("settings.yml", "server.chat-format.global.separator", "&7: &f"),

    SERVER_CHAT_FORMAT_PARTY("settings.yml", "server.chat-format.party.format", "&7[&6Party&7] &6{player}&7: &f{message}"),

    SERVER_ESSENTIAL_EMOJI_FEATURE_BOOLEAN("settings.yml", "server.essentials.emoji-feature", true),
    SERVER_SPAWN_LOCATION("settings.yml", "server.locations.spawn", "world:0.5:73.0:0.5:90.0:0.0"),

    SOUND_MATCH_PLATFORM_DECAY_NOTIFICATION_50_ENABLED_BOOLEAN("settings.yml", "sounds.match.platform-decay-notifications.50.enabled", true),
    SOUND_MATCH_PLATFORM_DECAY_NOTIFICATION_50("settings.yml", "sounds.match.platform-decay-notifications.50.value", "WITHER_HURT"),
    SOUND_MATCH_PLATFORM_DECAY_NOTIFICATION_25_ENABLED_BOOLEAN("settings.yml", "sounds.match.platform-decay-notifications.25.enabled", true),
    SOUND_MATCH_PLATFORM_DECAY_NOTIFICATION_25("settings.yml", "sounds.match.platform-decay-notifications.25.value", "ENDERDRAGON_GROWL"),
    SOUND_MATCH_PLATFORM_DECAY_NOTIFICATION_75_ENABLED_BOOLEAN("settings.yml", "sounds.match.platform-decay-notifications.75.enabled", true),
    SOUND_MATCH_PLATFORM_DECAY_NOTIFICATION_75("settings.yml", "sounds.match.platform-decay-notifications.75.value", "DIG_STONE");

    private final String configName;
    private final String configPath;
    private final Object defaultValue;

    /**
     * Constructor for the SettingsLocaleImpl enum.
     *
     * @param configName   The name of the configuration file.
     * @param configPath   The path to the specific string within the configuration file.
     * @param defaultValue The default value for the locale entry.
     */
    SettingsLocaleImpl(String configName, String configPath, Object defaultValue) {
        this.configName = configName;
        this.configPath = configPath;
        this.defaultValue = defaultValue;
    }
}