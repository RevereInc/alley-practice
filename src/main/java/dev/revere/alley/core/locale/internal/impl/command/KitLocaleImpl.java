package dev.revere.alley.core.locale.internal.impl.command;

import dev.revere.alley.core.locale.LocaleEntry;
import lombok.Getter;

/**
 * An enum class that contains all locale entries for kit commands.
 *
 * @author Emmy
 * @project Alley
 * @since 09/09/2025
 */
@Getter
public enum KitLocaleImpl implements LocaleEntry {
    NOT_FOUND("messages.yml", "kit.error.not-found", "&cThere is no kit with that name!"),
    ALREADY_EXISTS("messages.yml", "kit.error.already-exists", "&cA kit with that name already exists!"),
    SLOT_MUST_BE_NUMBER("messages.yml", "kit.error.slot-must-be-number", "&cThe slot must be a number!"),

    INVENTORY_GIVEN("messages.yml", "kit.data.inventory-given", "&aSuccessfully retrieved the inventory of the &6{kit-name} &akit!"),
    INVENTORY_SET("messages.yml", "kit.data.inventory-set", "&aSuccessfully set the inventory of the &6{kit-name} &akit!"),
    FFA_SLOT_SET("messages.yml", "kit.data.ffaslot-set", "&aSuccessfully set the FFA slot of the &6{kit-name} &akit to &6{slot}&a!"),
    DESCRIPTION_SET("messages.yml", "kit.data.description-set", "&aSuccessfully set the description of the &6{kit-name} &akit: &r{description}"),
    DESCRIPTION_CLEARED("messages.yml", "kit.data.description-cleared", "&aSuccessfully cleared the description of the &6{kit-name} &akit!"),
    DISCLAIMER_SET("messages.yml", "kit.data.disclaimer-set", "&aSuccessfully set the disclaimer of the &6{kit-name} &akit: &r{disclaimer}"),
    DISPLAYNAME_SET("messages.yml", "kit.data.displayname-set", "&aSuccessfully set the display name of the &6{kit-name} &akit: &r{display-name}"),
    ICON_SET("messages.yml", "kit.data.icon-set", "&aSuccessfully set the icon of the &6{kit-name} &akit to &6{icon}&a!"),
    SET_CATEGORY("messages.yml", "kit.data.set-category", "&aSuccessfully set the category of the &6{kit-name} &akit to &6{category}&a!"),
    MENU_TITLE_SET("messages.yml", "kit.data.menu-title-set", "&aSuccessfully set the menu title of the &6{kit-name} &akit: &r{title}"),
    SET_EDITABLE("messages.yml", "kit.data.set-editable", "&aSuccessfully set the editable status of the &6{kit-name} &akit to &6{editable}&a!"),

    POTION_EFFECTS_SET("messages.yml", "kit.data.potion-effects-set", "&aSuccessfully set the potion effects of the &6{kit-name} &akit!"),
    POTION_EFFECTS_CLEARED("messages.yml", "kit.data.potion-effects-cleared", "&aSuccessfully cleared the potion effects of the &6{kit-name} &akit!"),

    CREATED("messages.yml", "kit.manage.created", "&aSuccessfully created a new kit named &6{kit-name}&a!"),
    DELETED("messages.yml", "kit.manage.deleted", "&cSuccessfully deleted the kit named &6{kit-name}&c!"),

    SETTING_SET("messages.yml", "kit.settings.setting-set", "&aSuccessfully set the setting &6{setting-name} &ato &6{enabled} &afor the kit &6{kit-name}&a."),

    SAVED("messages.yml", "kit.storage.saved", "&aSuccessfully saved the &6{kit-name} &akit!"),
    SAVED_ALL("messages.yml", "kit.storage.saved-all", "&aSuccessfully saved all kits!"),

    DEFAULT_DISPLAYNAME("messages.yml", "kit.default-values.display-name", "&6{kit-name}"),
    DEFAULT_DESCRIPTION("messages.yml", "kit.default-values.description", ""),
    DEFAULT_DISCLAIMER("messages.yml", "kit.default-values.disclaimer", "&7{kit-name} kit disclaimer."),
    DEFAULT_MENU_TITLE("messages.yml", "kit.default-values.menu-title", "&6&l{kit-name}");

    private final String configName;
    private final String configPath;
    private final Object defaultValue;

    /**
     * Constructor for the KitLocale enum.
     *
     * @param configName   The name of the configuration file.
     * @param configPath   The path to the specific string within the configuration file.
     * @param defaultValue The default value for the locale entry.
     */
    KitLocaleImpl(String configName, String configPath, Object defaultValue) {
        this.configName = configName;
        this.configPath = "command-messages." + configPath;
        this.defaultValue = defaultValue;
    }
}