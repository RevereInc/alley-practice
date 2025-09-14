package dev.revere.alley.core.locale.internal.impl.provider;

import dev.revere.alley.core.locale.LocaleEntry;
import lombok.Getter;

/**
 * @author Emmy
 * @project alley-practice
 * @since 13/09/2025
 */
@Getter
public enum ScoreboardLocaleImpl implements LocaleEntry {

    ;

    private final String configName;
    private final String configPath;
    private final Object defaultValue;

    /**
     * Constructor for the ScoreboardLocaleImpl enum.
     *
     * @param configName   The name of the configuration file.
     * @param configPath   The path within the configuration file.
     * @param defaultValue The default value if not set in the configuration.
     */
    ScoreboardLocaleImpl(String configName, String configPath, Object defaultValue) {
        this.configName = configName;
        this.configPath = configPath;
        this.defaultValue = defaultValue;
    }
}