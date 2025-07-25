package dev.revere.alley.config.locale.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.config.locale.Locale;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 03/03/2025
 */
@Getter
public enum KitLocale implements Locale {
    KIT_NOT_FOUND("messages.yml", "kit.error.not-found"),
    KIT_ALREADY_EXISTS("messages.yml", "kit.error.already-exists"),
    KIT_CANNOT_SET_IN_CREATIVE("messages.yml", "kit.error.cannot-set-in-creative"),
    SLOT_MUST_BE_NUMBER("messages.yml", "kit.error.slot-must-be-number"),

    KIT_INVENTORY_GIVEN("messages.yml", "kit.data.inventory-given"),
    KIT_INVENTORY_SET("messages.yml", "kit.data.inventory-set"),
    KIT_UNRANKEDSLOT_SET("messages.yml", "kit.data.unrankedslot-set"),
    KIT_RANKEDSLOT_SET("messages.yml", "kit.data.rankedslot-set"),
    KIT_EDITORSLOT_SET("messages.yml", "kit.data.editorslot-set"),
    KIT_FFASLOT_SET("messages.yml", "kit.data.ffaslot-set"),
    KIT_SLOTS_SET("messages.yml", "kit.data.slots-set"),
    KIT_DESCRIPTION_SET("messages.yml", "kit.data.description-set"),
    KIT_DESCRIPTION_CLEARED("messages.yml", "kit.data.description-cleared"),
    KIT_DISCLAIMER_SET("messages.yml", "kit.data.disclaimer-set"),
    KIT_DISPLAYNAME_SET("messages.yml", "kit.data.displayname-set"),
    KIT_ICON_SET("messages.yml", "kit.data.icon-set"),
    KIT_SET_CATEGORY("messages.yml", "kit.data.set-category"),
    KIT_MENU_TITLE_SET("messages.yml", "kit.data.menu-title-set"),
    KIT_SET_EDITABLE("messages.yml", "kit.data.set-editable"),

    KIT_POTION_EFFECTS_SET("messages.yml", "kit.data.potion-effects-set"),
    KIT_POTION_EFFECTS_CLEARED("messages.yml", "kit.data.potion-effects-cleared"),

    KIT_CREATED("messages.yml", "kit.manage.created"),
    KIT_DELETED("messages.yml", "kit.manage.deleted"),

    KIT_SETTING_SET("messages.yml", "kit.settings.setting-set"),

    KIT_SAVED("messages.yml", "kit.storage.saved"),
    KIT_SAVED_ALL("messages.yml", "kit.storage.saved-all"),

    ;

    private final String configName;
    private final String configString;

    /**
     * Constructor for the KitLocale enum.
     *
     * @param configName   The name of the config.
     * @param configString The string of the config.
     */
    KitLocale(String configName, String configString) {
        this.configName = configName;
        this.configString = configString;
    }

    /**
     * Gets the String from the config.
     *
     * @return The message from the config.
     */
    @Override
    public String getMessage() {
        return CC.translate(Alley.getInstance().getService(ConfigService.class).getConfig(this.configName).getString(this.configString));
    }
}