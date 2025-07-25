package dev.revere.alley.base.kit.setting;

import dev.revere.alley.api.constant.PluginConstant;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.kit.setting.annotation.KitSettingData;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;
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
@Service(provides = KitSettingService.class, priority = 70)
public class KitSettingServiceImpl implements KitSettingService {
    private final PluginConstant pluginConstant;

    private final List<KitSetting> settings = new ArrayList<>();
    private final Map<String, Class<? extends KitSetting>> settingClasses = new HashMap<>();

    /**
     * Constructor for DI.
     */
    public KitSettingServiceImpl(PluginConstant pluginConstant) {
        this.pluginConstant = pluginConstant;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.registerSettings();
    }

    private void registerSettings() {
        Reflections reflections = this.pluginConstant.getReflections();

        for (Class<? extends KitSetting> clazz : reflections.getSubTypesOf(KitSetting.class)) {
            if (clazz.isInterface() || java.lang.reflect.Modifier.isAbstract(clazz.getModifiers()) || !clazz.isAnnotationPresent(KitSettingData.class)) {
                continue;
            }

            try {
                KitSetting instance = clazz.getDeclaredConstructor().newInstance();
                this.settings.add(instance);
                this.settingClasses.put(instance.getName(), clazz);
            } catch (Exception exception) {
                Logger.logException("Failed to register setting class " + clazz.getSimpleName() + "!", exception);
            }
        }
    }

    @Override
    public KitSetting createSettingByName(String name) {
        Class<? extends KitSetting> clazz = this.settingClasses.get(name);
        if (clazz != null) {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                Logger.logException("Failed to create setting instance for " + name + "!", e);
            }
        }
        return null;
    }

    @Override
    public KitSetting getSettingByName(String name) {
        return this.settings.stream()
                .filter(setting -> setting.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends KitSetting> T getSettingByClass(Class<T> clazz) {
        return (T) this.settings.stream()
                .filter(clazz::isInstance)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void applyAllSettingsToKit(Kit kit) {
        for (KitSetting setting : this.settings) {
            kit.addKitSetting(setting);
        }
    }
}