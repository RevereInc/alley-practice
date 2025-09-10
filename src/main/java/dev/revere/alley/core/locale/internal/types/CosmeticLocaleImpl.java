package dev.revere.alley.core.locale.internal.types;

import dev.revere.alley.core.locale.LocaleEntry;
import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 09/09/2025
 */
@Getter
public enum CosmeticLocaleImpl implements LocaleEntry {
    COSMETIC_NOT_FOUND("messages.yml", "cosmetics.error.not-found", "&aSuccessfully set &6{cosmetic} &a{type} as the active cosmetic for &6{player}&a!"),
    SET_COSMETIC("messages.yml", "cosmetics.set", "&cA cosmetic named &6{input} &cdoes not exist!"),

    ;

    private final String configName;
    private final String configPath;
    private final Object defaultValue;

    /**
     * Constructor for the CosmeticLocale enum.
     *
     * @param configName   The name of the configuration file.
     * @param configPath   The path to the specific string within the configuration file.
     * @param defaultValue The default value for the locale entry.
     */
    CosmeticLocaleImpl(String configName, String configPath, Object defaultValue) {
        this.configName = configName;
        this.configPath = configPath;
        this.defaultValue = defaultValue;
    }
}