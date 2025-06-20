package dev.revere.alley.base.kit.setting;

import dev.revere.alley.Alley;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.kit.setting.annotation.KitSettingData;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;
import lombok.Setter;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Getter
@Setter
public class KitSettingService {
    protected final Alley plugin;
    private final List<KitSetting> settings;
    private final Map<String, Class<? extends KitSetting>> settingClasses;

    /**
     * Constructor for the KitSettingService class.
     *
     * @param plugin The Alley plugin instance.
     */
    public KitSettingService(Alley plugin) {
        this.plugin = plugin;
        this.settings = new ArrayList<>();
        this.settingClasses = new HashMap<>();
        this.registerSettings();
    }

    private void registerSettings() {
        Reflections reflections = this.plugin.getPluginConstant().getReflections();

        for (Class<? extends KitSetting> clazz : reflections.getSubTypesOf(KitSetting.class)) {
            KitSettingData annotation = clazz.getAnnotation(KitSettingData.class);
            if (annotation != null) {
                try {
                    KitSetting instance = clazz.getDeclaredConstructor().newInstance();
                    this.settings.add(instance);
                    this.settingClasses.put(instance.getName(), clazz);
                } catch (Exception exception) {
                    Logger.logError("Failed to register setting class " + clazz.getSimpleName() + "!");
                }
            }
        }
    }

    /**
     * Method to create a new setting instance by its name.
     *
     * @param name The name of the setting.
     * @return A new instance of the setting.
     */
    public KitSetting createSettingByName(String name) {
        Class<? extends KitSetting> clazz = this.settingClasses.get(name);
        if (clazz != null) {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                Logger.logError("Failed to create setting instance for " + name + "!");
            }
        }
        return null;
    }

    /**
     * Method to get a setting by its name.
     *
     * @param name The name of the setting.
     * @return The setting.
     */
    public KitSetting getSettingByName(String name) {
        return this.settings.stream().filter(setting -> setting.getName().equals(name)).findFirst().orElse(null);
    }

    /**
     * Method to get a setting by its class.
     *
     * @param clazz The class of the setting.
     * @return The setting.
     */
    public KitSetting getSettingByClass(Class<? extends KitSetting> clazz) {
        return this.settings.stream().filter(setting -> setting.getClass().equals(clazz)).findFirst().orElse(null);
    }

    /**
     * Method to apply all settings to a kit.
     *
     * @param kit The kit to apply the settings to.
     */
    public void applyAllSettingsToKit(Kit kit) {
        for (KitSetting setting : this.settings) {
            kit.addKitSetting(setting);
        }
    }
}