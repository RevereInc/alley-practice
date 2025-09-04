package dev.revere.alley.core.config.internal.locale.impl;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.core.config.internal.locale.Locale;
import dev.revere.alley.common.text.CC;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 03/03/2025
 */
public enum ProfileLocale implements Locale {
    TOGGLED_PARTY_INVITES("messages.yml", "player-settings.party-invites"),
    TOGGLED_PARTY_MESSAGES("messages.yml", "player-settings.party-messages"),
    TOGGLED_SCOREBOARD("messages.yml", "player-settings.scoreboard"),
    TOGGLED_SCOREBOARD_LINES("messages.yml", "player-settings.scoreboard-lines"),
    TOGGLED_TABLIST("messages.yml", "player-settings.tablist"),
    TOGGLED_PROFANITY_FILTER("messages.yml", "player-settings.profanity-filter"),
    TOGGLED_DUEL_REQUESTS("messages.yml", "player-settings.duel-requests"),
    TOGGLED_LOBBY_MUSIC("messages.yml", "player-settings.lobby-music"),
    TOGGLED_SERVER_TITLES("messages.yml", "player-settings.server-titles"),

    ACCEPTED_DUEL_REQUEST("messages.yml", "duel-requests.accepted"),

    CANT_DUEL_SELF("messages.yml", "duel-requests.error.cant-duel-self"),
    PLAYER_DUEL_REQUESTS_DISABLED("messages.yml", "duel-requests.error.player-duel-requests-disabled"),

    NO_PENDING_DUEL_REQUEST("messages.yml", "duel-requests.info.no-pending-request"),
    PENDING_DUEL_REQUEST("messages.yml", "duel-requests.info.pending-request"),

    JOIN_MESSAGE_CHAT_ENABLED("messages.yml", "join-message.chat.enabled"),
    JOIN_MESSAGE_CHAT_MESSAGE_LIST("messages.yml", "join-message.chat.message"),

    JOIN_MESSAGE_TITLE_ENABLED("messages.yml", "join-message.title.enabled"),
    JOIN_MESSAGE_TITLE_HEADER("messages.yml", "join-message.title.header"),
    JOIN_MESSAGE_TITLE_SUBHEADER("messages.yml", "join-message.title.subheader"),

    ;

    private final String configName;
    private final String configString;

    /**
     * Constructor for the ProfileLocale enum.
     *
     * @param configName   The name of the config.
     * @param configString The string of the config.
     */
    ProfileLocale(String configName, String configString) {
        this.configName = configName;
        this.configString = configString;
    }

    @Override
    public String getMessage() {
        return CC.translate(AlleyPlugin.getInstance().getService(ConfigService.class).getConfig(this.configName).getString(this.configString));
    }

    @Override
    public boolean getBoolean() {
        return AlleyPlugin.getInstance().getService(ConfigService.class).getConfig(this.configName).getBoolean(this.configString);
    }

    @Override
    public List<String> getList() {
        return CC.translateList(AlleyPlugin.getInstance().getService(ConfigService.class).getConfig(this.configName).getStringList(this.configString));
    }
}