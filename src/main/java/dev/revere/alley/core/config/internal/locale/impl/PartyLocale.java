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
public enum PartyLocale implements Locale {
    PARTY_CREATED("messages.yml", "party.created"),
    PARTY_DISBANDED("messages.yml", "party.disbanded"),
    NOT_IN_PARTY("messages.yml", "party.not-in-party"),
    NOT_LEADER("messages.yml", "party.not-leader"),
    ALREADY_IN_PARTY("messages.yml", "party.already-in-party"),
    PARTY_LEFT("messages.yml", "party.left"),
    PLAYER_DISABLED_PARTY_INVITES("messages.yml", "party.target-disabled-invites"),
    DISABLED_PARTY_CHAT("messages.yml", "party.disabled-chat"),
    NO_PARTY_INVITE("messages.yml", "party.no-invite"),
    JOINED_PARTY("messages.yml", "party.joined"),

    PARTY_LOOKUP_LIST("messages.yml", "party.lookup")

    ;

    private final String configName;
    private final String configString;

    /**
     * Constructor for the PartyLocale enum.
     *
     * @param configName   The name of the config.
     * @param configString The string of the config.
     */
    PartyLocale(String configName, String configString) {
        this.configName = configName;
        this.configString = configString;
    }

    @Override
    public String getMessage() {
        return CC.translate(AlleyPlugin.getInstance().getService(ConfigService.class).getConfig(this.configName).getString(this.configString));
    }

    /**
     * Gets a list of messages from the configuration.
     *
     * @return A list of translated messages.
     */
    @Override
    public List<String> getList() {
        return CC.translateList(AlleyPlugin.getInstance().getService(ConfigService.class).getConfig(this.configName).getStringList(this.configString));
    }
}