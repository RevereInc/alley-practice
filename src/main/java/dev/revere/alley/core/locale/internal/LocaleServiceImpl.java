package dev.revere.alley.core.locale.internal;

import dev.revere.alley.bootstrap.AlleyContext;
import dev.revere.alley.bootstrap.annotation.Service;
import dev.revere.alley.common.logger.Logger;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.core.locale.LocaleEntry;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.locale.internal.types.*;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @since 09/09/2025
 */
@Service(provides = LocaleService.class, priority = 25)
public class LocaleServiceImpl implements LocaleService {
    private final ConfigService configService;

    /**
     * DI Constructor for the LocaleServiceImpl class.
     *
     * @param configService the ConfigService instance.
     */
    public LocaleServiceImpl(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public void initialize(AlleyContext context) {
        List<Class<? extends LocaleEntry>> localeEntries = Arrays.asList(
                ArenaLocaleImpl.class, CosmeticLocaleImpl.class, DivisionLocaleImpl.class, FFALocaleImpl.class,
                HotbarLocaleImpl.class, KitLocaleImpl.class, PartyLocaleImpl.class,

                ErrorLocaleImpl.class,ProfileLocaleImpl.class, ServerLocaleImpl.class
        );

        Logger.infoNoPrefix("");
        Logger.info("Checking locale entries...");
        Logger.infoNoPrefix("");

        int missingCount = 0;

        for (Class<? extends LocaleEntry> localeClass : localeEntries) {
            Map<String, Boolean> filesToSave = new HashMap<>();

            for (LocaleEntry entry : localeClass.getEnumConstants()) {
                FileConfiguration config = this.configService.getConfig(entry.getConfigName());
                if (!config.contains(entry.getConfigPath())) {
                    config.set(entry.getConfigPath(), entry.getDefaultValue());
                    Logger.infoNoPrefix("&8'&6" + entry.getConfigPath() + "&8' &fnot found in &6" + entry.getConfigName() + "&f.");
                    filesToSave.put(entry.getConfigName(), true);
                    missingCount++;
                }
            }

            for (String fileName : filesToSave.keySet()) {
                File file = this.configService.getConfigFile(fileName);
                FileConfiguration config = this.configService.getConfig(fileName);
                this.configService.saveConfig(file, config);
            }
        }

        Logger.infoNoPrefix("");
        Logger.info("Locale entries check complete.");
        if (missingCount > 0) {
            Logger.info("Total of " + missingCount + " missing locale entr" + (missingCount == 1 ? "y" : "ies") + (missingCount == 1 ? " was" : " were") + " added.");
        } else {
            Logger.info("No missing locale entries found.");
        }
        Logger.infoNoPrefix("");
    }

    @Override
    public String getMessage(LocaleEntry entry) {
        FileConfiguration config = this.configService.getConfig(entry.getConfigName());
        if (config.contains(entry.getConfigPath())) {
            return CC.translate(config.getString(entry.getConfigPath()));
        } else {
            Logger.error("'" + entry.getConfigPath() + "' doesn't exist in " + entry.getConfigName() + ". Using default value. Restarting your server will fix this.");
            return (String) entry.getDefaultValue();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getMessageList(LocaleEntry entry) {
        FileConfiguration config = this.configService.getConfig(entry.getConfigName());
        if (config.contains(entry.getConfigPath())) {
            return CC.translateList(config.getStringList(entry.getConfigPath()));
        } else {
            Logger.error("'" + entry.getConfigPath() + "' doesn't exist in " + entry.getConfigName() + ". Using default value. Restarting your server will fix this.");
            return (List<String>) entry.getDefaultValue();
        }
    }

    @Override
    public int getInt(LocaleEntry entry) {
        FileConfiguration config = this.configService.getConfig(entry.getConfigName());
        if (config.contains(entry.getConfigPath())) {
            return config.getInt(entry.getConfigPath());
        } else {
            Logger.error("'" + entry.getConfigPath() + "' doesn't exist in " + entry.getConfigName() + ". Using default value. Restarting your server will fix this.");
            return (int) entry.getDefaultValue();
        }
    }

    @Override
    public double getDouble(LocaleEntry entry) {
        FileConfiguration config = this.configService.getConfig(entry.getConfigName());
        if (config.contains(entry.getConfigPath())) {
            return config.getDouble(entry.getConfigPath());
        } else {
            Logger.error("'" + entry.getConfigPath() + "' doesn't exist in " + entry.getConfigName() + ". Using default value. Restarting your server will fix this.");
            return (double) entry.getDefaultValue();
        }
    }

    @Override
    public boolean getBoolean(LocaleEntry entry) {
        FileConfiguration config = this.configService.getConfig(entry.getConfigName());
        if (config.contains(entry.getConfigPath())) {
            return config.getBoolean(entry.getConfigPath());
        } else {
            Logger.error("'" + entry.getConfigPath() + "' doesn't exist in " + entry.getConfigName() + ". Using default value. Restarting your server will fix this.");
            return (boolean) entry.getDefaultValue();
        }
    }
}