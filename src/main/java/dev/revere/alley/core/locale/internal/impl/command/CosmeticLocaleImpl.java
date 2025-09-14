package dev.revere.alley.core.locale.internal.impl.command;

import dev.revere.alley.core.locale.LocaleEntry;
import lombok.Getter;

/**
 * An enum class that contains all locale entries for cosmetic commands.
 *
 * @author Emmy
 * @project Alley
 * @since 09/09/2025
 */
@Getter
public enum CosmeticLocaleImpl implements LocaleEntry {
    SET_COSMETIC("messages.yml", "cosmetics.error.not-found", "&aSuccessfully set &6{cosmetic} &a{type} as the active cosmetic for &6{player}&a!"),
    NO_COSMETICS_REGISTERED("messages.yml", "cosmetics.error.no-cosmetics-registered", "&cThere are no cosmetics registered on the server!"),
    COSMETIC_TYPE_NOT_SUPPORTED("messages.yml", "cosmetics.error.type-not-supported", "&cThe cosmetic type &6{type} &cis not supported!"),

    COSMETIC_NOT_FOUND("messages.yml", "cosmetics.manage.set", "&cA cosmetic named &6{input} &cdoes not exist!"),

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
        this.configPath = "command-messages." + configPath;
        this.defaultValue = defaultValue;
    }
}