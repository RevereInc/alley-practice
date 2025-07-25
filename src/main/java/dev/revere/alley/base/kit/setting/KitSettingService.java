package dev.revere.alley.base.kit.setting;

import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.plugin.lifecycle.Service;

import java.util.List;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface KitSettingService extends Service {

    /**
     * Gets a list of all discovered KitSetting template instances.
     * @return A list of all KitSettings.
     */
    List<KitSetting> getSettings();

    /**
     * Creates a new instance of a KitSetting by its registered name.
     * @param name The name of the setting (from @KitSettingData).
     * @return A new KitSetting instance, or null if not found.
     */
    KitSetting createSettingByName(String name);

    /**
     * Gets the template instance of a KitSetting by its name.
     * @param name The name of the setting.
     * @return The singleton KitSetting instance, or null if not found.
     */
    KitSetting getSettingByName(String name);

    /**
     * Gets the template instance of a KitSetting by its class.
     * @param clazz The class of the setting.
     * @return The singleton KitSetting instance, or null if not found.
     */
    <T extends KitSetting> T getSettingByClass(Class<T> clazz);

    /**
     * Applies all default settings to a given kit.
     * @param kit The kit to apply settings to.
     */
    void applyAllSettingsToKit(Kit kit);
}