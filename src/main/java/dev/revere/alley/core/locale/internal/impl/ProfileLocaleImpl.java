package dev.revere.alley.core.locale.internal.impl;

import dev.revere.alley.core.locale.LocaleEntry;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 09/09/2025
 */
@Getter
public enum ProfileLocaleImpl implements LocaleEntry {
    JOIN_MESSAGE_CHAT_ENABLED("messages.yml", "join-message.chat.enabled", true),
    JOIN_MESSAGE_CHAT_MESSAGE_LIST("messages.yml", "join-message.chat.message", Arrays.asList(
            "",
            "&6&lAlley Practice Core",
            " &6&l│ &rWebsite: &6revere.dev",
            " &6&l│ &rDiscord: &6discord.gg/p3R5qhfWAx",
            " &6&l│ &rGitHub: &6github.com/RevereInc/alley-practice",
            "",
            "&6&lMade by &f{author} &7(v{version})",
            ""
    )),

    JOIN_MESSAGE_TITLE_ENABLED("messages.yml", "join-message.title.enabled", true),
    JOIN_MESSAGE_TITLE_HEADER("messages.yml", "join-message.title.header", "&6&lWelcome to Alley Practice"),
    JOIN_MESSAGE_TITLE_SUBHEADER("messages.yml", "join-message.title.subheader", "&fMade by &6Emmy &f& &6Remi"),

    TOGGLED_PARTY_INVITES("messages.yml", "player-settings.party-invites", "&aYou've {status} &aparty invites."),
    TOGGLED_PARTY_MESSAGES("messages.yml", "player-settings.party-messages", "&aYou've {status} &aparty messages."),
    TOGGLED_SCOREBOARD("messages.yml", "player-settings.scoreboard", "&aYou've {status} &athe scoreboard."),
    TOGGLED_SCOREBOARD_LINES("messages.yml", "player-settings.scoreboard-lines", "&aYou've {status} &athe scoreboard lines."),
    TOGGLED_TABLIST("messages.yml", "player-settings.tablist", "&aYou've {status} &athe tablist."),
    TOGGLED_PROFANITY_FILTER("messages.yml", "player-settings.profanity-filter", "&aYou've {status} &athe profanity filter."),
    TOGGLED_DUEL_REQUESTS("messages.yml", "player-settings.duel-requests", "&aYou've {status} &areceiving duel requests."),
    TOGGLED_LOBBY_MUSIC("messages.yml", "player-settings.lobby-music", "&aYou've {status} &alobby music."),
    TOGGLED_SERVER_TITLES("messages.yml", "player-settings.server-titles", "&aYou've {status} &aserver titles."),

    ACCEPTED_DUEL_REQUEST("messages.yml", "duel-requests.accepted", "&aYou have accepted the duel request from &6{color}{player}&a!"),
    CANT_DUEL_SELF("messages.yml", "duel-requests.error.cant-duel-self", "&cYou can't duel yourself!"),
    PLAYER_DUEL_REQUESTS_DISABLED("messages.yml", "duel-requests.error.player-duel-requests-disabled", "&c{color}{player} has duel requests disabled."),
    DUEL_REQUEST_EXPIRED("messages.yml", "duel-requests.error.request-expired", "&cThat duel request has expired."),
    DUEL_REQUEST_NO_ARENA("messages.yml", "duel-requests.error.no-arenas", "&cThere are no arenas available for that kit."),

    NO_PENDING_DUEL_REQUEST("messages.yml", "duel-requests.info.no-pending-request", "&cYou do not have a pending duel request from that player."),
    PENDING_DUEL_REQUEST("messages.yml", "duel-requests.info.pending-request", "&cYou already have a pending duel request from that player."),

    COSMETIC_ALREADY_SELECTED("messages.yml", "personal.cosmetics.already-selected", "&cYou already selected the &6{cosmetic-name}&c cosmetic!"),
    COSMETIC_NOT_OWNED("messages.yml", "personal.cosmetics.not-owned", "&cYou do not own the &6{cosmetic-name}&c cosmetic!"),
    COSMETIC_SELECTED("messages.yml", "personal.cosmetics.selected", "&aSuccessfully selected the &6{cosmetic-name} &acosmetic!"),
    ALREADY_IN_MATCH("messages.yml", "personal.error.already-in-match", "&cYou are already in a match!"),
    FFA_SPAWN_ENTER("messages.yml", "personal.informative.ffa-spawn-enter", "&cYou cannot enter an FFA spawn while in a match!"),
    FFA_SPAWN_LEAVE("messages.yml", "personal.informative.ffa-spawn-leave", "&aYou have left the FFA spawn."),
    WORLD_TIME_SET("messages.yml", "personal.informative.world-time-set", "&aSuccessfully set your personal world time to &6{time}&a."),
    WORLD_TIME_RESET("messages.yml", "personal.informative.world-time-reset", "&aSuccessfully reset your personal world time to the server's time.");

    private final String configName;
    private final String configPath;
    private final Object defaultValue;

    /**
     * Constructor for the ProfileLocale enum.
     *
     * @param configName   The name of the configuration file.
     * @param configPath   The path to the specific string within the configuration file.
     * @param defaultValue The default value for the locale entry.
     */
    ProfileLocaleImpl(String configName, String configPath, Object defaultValue) {
        this.configName = configName;
        this.configPath = "profile-messages." + configPath;
        this.defaultValue = defaultValue;
    }
}