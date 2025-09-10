package dev.revere.alley.core.locale.internal.types;

import dev.revere.alley.core.locale.LocaleEntry;
import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 09/09/2025
 */
@Getter
public enum KitLocaleImpl implements LocaleEntry {
    KIT_NOT_FOUND(
            "messages.yml",
            "kit.error.not-found",
            "&cThere is no kit with that name!"
    ),
    KIT_ALREADY_EXISTS(
            "messages.yml",
            "kit.error.already-exists",
            "&cA kit with that name already exists!"
    ),
    KIT_CANNOT_SET_IN_CREATIVE(
            "messages.yml",
            "kit.error.cannot-set-in-creative",
            "&cYou can't set a kit's inventory in creative mode!"
    ),
    SLOT_MUST_BE_NUMBER(
            "messages.yml",
            "kit.error.slot-must-be-number",
            "&cThe slot must be a number!"
    ),

    KIT_INVENTORY_GIVEN(
            "messages.yml",
            "kit.data.inventory-given",
            "&aSuccessfully retrieved the inventory of the &6{kit-name} &akit!"
    ),
    KIT_INVENTORY_SET(
            "messages.yml",
            "kit.data.inventory-set",
            "&aSuccessfully set the inventory of the &6{kit-name} &akit!"
    ),
    KIT_UNRANKEDSLOT_SET(
            "messages.yml",
            "kit.data.unrankedslot-set",
            "&aSuccessfully set the unranked slot of the &6{kit-name} &akit to &6{slot}&a!"
    ),
    KIT_RANKEDSLOT_SET(
            "messages.yml",
            "kit.data.rankedslot-set",
            "&aSuccessfully set the ranked slot of the &6{kit-name} &akit to &6{slot}&a!"
    ),
    KIT_EDITORSLOT_SET(
            "messages.yml",
            "kit.data.editorslot-set",
            "&aSuccessfully set the editor slot of the &6{kit-name} &akit to &6{slot}&a!"
    ),
    KIT_FFASLOT_SET(
            "messages.yml",
            "kit.data.ffaslot-set",
            "&aSuccessfully set the FFA slot of the &6{kit-name} &akit to &6{slot}&a!"
    ),
    KIT_SLOTS_SET(
            "messages.yml",
            "kit.data.slots-set",
            "&aSuccessfully set all slots of the &6{kit-name} &akit to &6{slot}&a!"
    ),
    KIT_DESCRIPTION_SET(
            "messages.yml",
            "kit.data.description-set",
            "&aSuccessfully set the description of the &6{kit-name} &akit: &r{description}"
    ),
    KIT_DESCRIPTION_CLEARED(
            "messages.yml",
            "kit.data.description-cleared",
            "&aSuccessfully cleared the description of the &6{kit-name} &akit!"
    ),
    KIT_DISCLAIMER_SET(
            "messages.yml",
            "kit.data.disclaimer-set",
            "&aSuccessfully set the disclaimer of the &6{kit-name} &akit: &r{disclaimer}"
    ),
    KIT_DISPLAYNAME_SET(
            "messages.yml",
            "kit.data.displayname-set",
            "&aSuccessfully set the display name of the &6{kit-name} &akit: &r{display-name}"
    ),
    KIT_ICON_SET(
            "messages.yml",
            "kit.data.icon-set",
            "&aSuccessfully set the icon of the &6{kit-name} &akit to &6{icon}&a!"
    ),
    KIT_SET_CATEGORY(
            "messages.yml",
            "kit.data.set-category",
            "&aSuccessfully set the category of the &6{kit-name} &akit to &6{category}&a!"
    ),
    KIT_MENU_TITLE_SET(
            "messages.yml",
            "kit.data.menu-title-set",
            "&aSuccessfully set the menu title of the &6{kit-name} &akit: &r{title}"
    ),
    KIT_SET_EDITABLE(
            "messages.yml",
            "kit.data.set-editable",
            "&aSuccessfully set the editable status of the &6{kit-name} &akit to &6{editable}&a!"
    ),

    KIT_POTION_EFFECTS_SET(
            "messages.yml",
            "kit.data.potion-effects-set",
            "&aSuccessfully set the potion effects of the &6{kit-name} &akit!"
    ),
    KIT_POTION_EFFECTS_CLEARED(
            "messages.yml",
            "kit.data.potion-effects-cleared",
            "&aSuccessfully cleared the potion effects of the &6{kit-name} &akit!"
    ),

    KIT_CREATED(
            "messages.yml",
            "kit.manage.created",
            "&aSuccessfully created a new kit named &6{kit-name}&a!"
    ),
    KIT_DELETED(
            "messages.yml",
            "kit.manage.deleted",
            "&cSuccessfully deleted the kit named &6{kit-name}&c!"
    ),

    KIT_SETTING_SET(
            "messages.yml",
            "kit.settings.setting-set",
            "&aSuccessfully set the setting &6{setting-name} &ato &6{enabled} &afor the kit &6{kit-name}&a."
    ),

    KIT_SAVED(
            "messages.yml",
            "kit.storage.saved",
            "&aSuccessfully saved the &6{kit-name} &akit!"
    ),
    KIT_SAVED_ALL(
            "messages.yml",
            "kit.storage.saved-all",
            "&aSuccessfully saved all kits!"
    ),

    ;

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
        this.configPath = configPath;
        this.defaultValue = defaultValue;
    }
}