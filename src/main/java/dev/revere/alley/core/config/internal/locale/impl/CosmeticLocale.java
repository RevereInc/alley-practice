package dev.revere.alley.core.config.internal.locale.impl;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.core.config.internal.locale.Locale;

/**
 * @author Emmy
 * @project alley-practice
 * @since 04/09/2025
 */
public enum CosmeticLocale implements Locale {
    COSMETIC_NOT_FOUND("messages.yml", "cosmetics.error.not-found"),
    SET_COSMETIC("messages.yml", "cosmetics.set")

    ;

    private final String configName;
    private final String configString;

    /**
     * Constructor for the CosmeticLocale enum.
     *
     * @param configName   The name of the configuration file.
     * @param configString The path to the specific string within the configuration file.
     */
    CosmeticLocale(String configName, String configString) {
        this.configName = configName;
        this.configString = configString;
    }

    @Override
    public String getMessage() {
        return CC.translate(AlleyPlugin.getInstance().getService(ConfigService.class).getConfig(this.configName).getString(this.configString));
    }
}
