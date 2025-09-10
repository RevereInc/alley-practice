package dev.revere.alley.core.locale.internal.types;

import dev.revere.alley.core.locale.LocaleEntry;
import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 09/09/2025
 */
@Getter
public enum DivisionLocaleImpl implements LocaleEntry {
    NOT_FOUND(
            "messages.yml",
            "division.error.not-found",
            "&cA division named &6{division-name} &cdoes not exist!"
    ),
    TIER_NOT_FOUND(
            "messages.yml",
            "division.error.tier-not-found",
            "&cA tier named &6{tier-name} &cdoes not exist in the &6{division-name} &cdivision!"
    ),
    ALREADY_EXISTS(
            "messages.yml",
            "division.error.already-exists",
            "&cThe name &6{division-name} &cis already taken by another division!"
    ),

    DESCRIPTION_SET(
            "messages.yml",
            "division.data.description-set",
            "&aSuccessfully set the description of the &6{division-name} &adivision to &r{description}&a!"
    ),
    DISPLAY_NAME_SET(
            "messages.yml",
            "division.data.display-name-set",
            "&aSuccessfully set the display name of the &6{division-name} &adivision to &r{display-name}&a!"
    ),
    ICON_SET(
            "messages.yml",
            "division.data.icon-set",
            "&aSuccessfully set the icon for the division &6{division-name} &ato &6{item-type}:{item-durability}&a!"
    ),
    WINS_SET(
            "messages.yml",
            "division.data.wins-set",
            "&aSuccessfully set the required wins for the &6{division-name} &adivision's &6{tier-name} &atier to &6{required-wins}&a!"
    ),

    CREATED(
            "messages.yml",
            "division.manage.created",
            "&aSuccessfully created a new division named &6{division-name} &awith &6{required-wins} &awins!"
    ),
    DELETED(
            "messages.yml",
            "division.manage.deleted",
            "&cSuccessfully deleted the division named &6{division-name}&c!"
    ),

    ;

    private final String configName;
    private final String configPath;
    private final Object defaultValue;

    /**
     * Constructor for the DivisionLocale enum.
     *
     * @param configName   The name of the configuration file.
     * @param configPath   The path to the specific string within the configuration file.
     * @param defaultValue The default value for the locale entry.
     */
    DivisionLocaleImpl(String configName, String configPath, Object defaultValue) {
        this.configName = configName;
        this.configPath = configPath;
        this.defaultValue = defaultValue;
    }
}
