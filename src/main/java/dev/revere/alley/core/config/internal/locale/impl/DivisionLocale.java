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
public enum DivisionLocale implements Locale {
    NOT_FOUND("messages.yml", "division.error.not-found"),
    TIER_NOT_FOUND("messages.yml", "division.error.tier-not-found"),
    ALREADY_EXISTS("messages.yml", "division.error.already-exists"),

    DESCRIPTION_SET("messages.yml", "division.data.description-set"),
    DISPLAY_NAME_SET("messages.yml", "division.data.display-name-set"),
    ICON_SET("messages.yml", "division.data.icon-set"),
    WINS_SET("messages.yml", "division.data.wins-set"),

    CREATED("messages.yml", "division.manage.created"),
    DELETED("messages.yml", "division.manage.deleted"),

    ;

    private final String configName;
    private final String configString;

    /**
     * Constructor for the DivisionLocale enum.
     *
     * @param configName   The name of the configuration file.
     * @param configString The path to the specific string within the configuration file.
     */
    DivisionLocale(String configName, String configString) {
        this.configName = configName;
        this.configString = configString;
    }

    @Override
    public String getMessage() {
        return CC.translate(AlleyPlugin.getInstance().getService(ConfigService.class).getConfig(this.configName).getString(this.configString));
    }
}
