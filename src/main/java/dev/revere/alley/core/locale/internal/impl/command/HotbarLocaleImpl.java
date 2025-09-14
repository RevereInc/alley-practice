package dev.revere.alley.core.locale.internal.impl.command;

import dev.revere.alley.core.locale.LocaleEntry;
import lombok.Getter;

/**
 * An enum class that contains all locale entries for hotbar commands.
 *
 * @author Emmy
 * @project alley-practice
 * @since 09/09/2025
 */
@Getter
public enum HotbarLocaleImpl implements LocaleEntry {
    NOT_FOUND("messages.yml", "hotbar.error.not-found", "&cThe hotbar item named &e{hotbar-name} &cdoes not exist."),
    NO_HOTBAR_ITEMS_CREATED("messages.yml", "hotbar.error.no-hotbar-items-created", "&cNo hotbar items have been created yet."),

    CREATED_ITEM("messages.yml", "hotbar.manage.created", "&aYou have created a new hotbar item named &e{hotbar-name}&a."),
    DELETED_ITEM("messages.yml", "hotbar.manage.deleted", "&aYou have deleted the hotbar item named &e{hotbar-name}&a.");

    private final String configName;
    private final String configPath;
    private final String defaultValue;

    /**
     * Constructor for the HotbarLocaleImpl enum.
     *
     * @param configName   The name of the configuration file.
     * @param configPath   The path to the specific string within the configuration file.
     * @param defaultValue The default value for the locale entry.
     */
    HotbarLocaleImpl(String configName, String configPath, String defaultValue) {
        this.configName = configName;
        this.configPath = "command-messages." + configPath;
        this.defaultValue = defaultValue;
    }
}