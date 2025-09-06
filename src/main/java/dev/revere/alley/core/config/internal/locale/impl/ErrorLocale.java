package dev.revere.alley.core.config.internal.locale.impl;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.core.config.internal.locale.Locale;

/**
 * @author Emmy
 * @project alley-practice
 * @since 02/09/2025
 */
public enum ErrorLocale implements Locale {
    INVALID_NUMBER("messages.yml", "error-messages.invalid.number"),
    INVALID_PLAYER("messages.yml", "error-messages.invalid.player"),
    INVALID_TYPE("messages.yml", "error-messages.invalid.type"),

    MUST_BE_IN_LOBBY("messages.yml", "error-messages.player.must-be-in-lobby"),
    MUST_HOLD_ITEM("messages.yml", "error-messages.player.must-hold-item"),
    IS_BUSY("messages.yml", "error-messages.player.is-busy"),

    ;

    private final String configName;
    private final String configString;

    /**
     * Constructor for the ErrorLocale enum.
     *
     * @param configName   The name of the config.
     * @param configString The string of the config.
     */
    ErrorLocale(String configName, String configString) {
        this.configName = configName;
        this.configString = configString;
    }

    @Override
    public String getMessage() {
        ConfigService configService = AlleyPlugin.getInstance().getService(ConfigService.class);
        return CC.translate(configService.getConfig(this.configName).getString(this.configString));
    }
}