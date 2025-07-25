package dev.revere.alley.config.locale.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.config.locale.Locale;
import dev.revere.alley.util.chat.CC;

/**
 * @author Emmy
 * @project alley-practice
 * @since 15/07/2025
 */
public enum ServerLocale implements Locale {
    CRAFTING_TOGGLED("messages.yml", "server.crafting-operations.toggled"),
    MUST_HOLD_CRAFTABLE_ITEM("messages.yml", "server.crafting-operations.must-hold-craftable-item"),

    ;

    private final String configName, configString;

    /**
     * Constructor for the ServerLocale enum.
     *
     * @param configName   The name of the config.
     * @param configString The string of the config.
     */
    ServerLocale(String configName, String configString) {
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
        return CC.translate(Alley.getInstance().getService(ConfigService.class).getConfig(this.configName).getString(this.configString));
    }
}