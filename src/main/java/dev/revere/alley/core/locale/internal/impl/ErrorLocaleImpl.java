package dev.revere.alley.core.locale.internal.impl;

import dev.revere.alley.core.locale.LocaleEntry;
import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 09/09/2025
 */
@Getter
public enum ErrorLocaleImpl implements LocaleEntry {
    INVALID_NUMBER("messages.yml", "invalid.number", "&c'{input}' is not a valid number! Please enter a valid number."),
    INVALID_PLAYER("messages.yml", "invalid.player", "&cThat player could not be found!"),
    INVALID_TYPE("messages.yml", "invalid.type", "&cInvalid {type}. Available types: &6{types}&c."),

    MUST_BE_IN_LOBBY("messages.yml", "player.must-be-in-lobby", "&cYou must be in the lobby to do that!"),
    MUST_HOLD_ITEM("messages.yml", "player.must-hold-item", "&cYou need to be holding an item!"),
    MUST_LEAVE_PARTY_TO_JOIN_FFA("messages.yml", "player.must-leave-party-to-join-ffa", "&cYou must leave your party to join an FFA match!"),
    NOT_IN_FFA_MATCH("messages.yml", "player.not-in-ffa-match", "&cYou are not in an FFA match!"),
    ALREADY_SPECTATING_FFA("messages.yml", "player.already-spectating-ffa", "&cYou are already spectating FFA!"),

    IS_BUSY("messages.yml", "player.is-busy", "&6{color}{player} &cis busy."),

    ;

    private final String configName;
    private final String configPath;
    private final Object defaultValue;

    /**
     * Constructor for the ErrorLocale enum.
     *
     * @param configName   The name of the configuration file.
     * @param configPath   The path to the specific string within the configuration file.
     * @param defaultValue The default value for the locale entry.
     */
    ErrorLocaleImpl(String configName, String configPath, Object defaultValue) {
        this.configName = configName;
        this.configPath = "error-messages." + configPath;
        this.defaultValue = defaultValue;
    }
}