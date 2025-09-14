package dev.revere.alley.core.locale.internal.impl.menu;

import dev.revere.alley.core.locale.LocaleEntry;
import lombok.Getter;

/**
 * @author Emmy
 * @project alley-practice
 * @since 13/09/2025
 */
@Getter
public enum QueueLocaleImpl implements LocaleEntry {

    //TODO: Fill out queue menu locale entries

    ;

    private final String configName;
    private final String configPath;
    private final Object defaultValue;

    /**
     * Constructor for the QueueLocaleImpl enum.
     *
     * @param configName   The name of the configuration file.
     * @param configPath   The path to the specific string within the configuration file.
     * @param defaultValue The default value for the locale entry.
     */
    QueueLocaleImpl(String configName, String configPath, Object defaultValue) {
        this.configName = configName;
        this.configPath = "menu-messages." + configPath;
        this.defaultValue = defaultValue;
    }
}