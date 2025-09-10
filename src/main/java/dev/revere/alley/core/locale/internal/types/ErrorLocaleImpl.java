package dev.revere.alley.core.locale.internal.types;

import dev.revere.alley.core.locale.LocaleEntry;
import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 09/09/2025
 */
@Getter
public enum ErrorLocaleImpl implements LocaleEntry {
    INVALID_NUMBER(
            "messages.yml",
            "error-messages.invalid.number",
            "&c'{input}' is not a valid number! Please enter a valid number."
    ),
    INVALID_PLAYER(
            "messages.yml",
            "error-messages.invalid.player",
            "&cThat player could not be found!"
    ),
    INVALID_TYPE(
            "messages.yml",
            "error-messages.invalid.type",
            "&cInvalid {type}. Available types: &6{types}&c."
    ),

    MUST_BE_IN_LOBBY(
            "messages.yml",
            "error-messages.player.must-be-in-lobby",
            "&cYou must be in the lobby to do that!"
    ),
    MUST_HOLD_ITEM(
            "messages.yml",
            "error-messages.player.must-hold-item",
            "&cYou need to be holding an item!"
    ),
    IS_BUSY(
            "messages.yml",
            "error-messages.player.is-busy",
            "&6{color}{player} &cis busy."
    ),

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
        this.configPath = configPath;
        this.defaultValue = defaultValue;
    }
}