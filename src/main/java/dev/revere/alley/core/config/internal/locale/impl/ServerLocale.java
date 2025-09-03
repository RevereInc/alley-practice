package dev.revere.alley.core.config.internal.locale.impl;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.core.config.internal.locale.Locale;
import dev.revere.alley.common.text.CC;

import java.util.List;

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

    @Override
    public String getMessage() {
        return CC.translate(AlleyPlugin.getInstance().getService(ConfigService.class).getConfig(this.configName).getString(this.configString));
    }

    @Override
    public List<String> getList() {
        return CC.translateList(AlleyPlugin.getInstance().getService(ConfigService.class).getConfig(this.configName).getStringList(this.configString));
    }
}