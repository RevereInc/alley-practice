package dev.revere.alley.config.locale.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.config.locale.ILocale;
import dev.revere.alley.util.chat.CC;

/**
 * @author Emmy
 * @project Alley
 * @since 03/03/2025
 */
public enum ProfileLocale implements ILocale {
    TOGGLED_PARTY_INVITES("messages.yml", "player-settings.party-invites"),
    TOGGLED_PARTY_MESSAGES("messages.yml", "player-settings.party-messages"),
    TOGGLED_SCOREBOARD("messages.yml", "player-settings.scoreboard"),
    TOGGLED_SCOREBOARD_LINES("messages.yml", "player-settings.scoreboard-lines"),
    TOGGLE_TABLIST("messages.yml", "player-settings.tablist"),
    TOGGLED_PROFANITY_FILTER("messages.yml", "player-settings.profanity-filter");

    private final String configName, configString;

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

    /**
     * Gets the String from the config.
     *
     * @return The message from the config.
     */
    @Override
    public String getMessage() {
        return CC.translate(Alley.getInstance().getConfigService().getConfig(this.configName).getString(this.configString));
    }
}