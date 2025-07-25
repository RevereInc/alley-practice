package dev.revere.alley.config;

import dev.revere.alley.plugin.lifecycle.Service;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface ConfigService extends Service {
    /**
     * Reloads all configuration files from disk.
     */
    void reloadConfigs();

    /**
     * Saves a FileConfiguration to its corresponding file on disk.
     *
     * @param configFile        The file to save.
     * @param fileConfiguration The configuration object to save.
     */
    void saveConfig(File configFile, FileConfiguration fileConfiguration);

    /**
     * Gets a loaded configuration by its file name.
     *
     * @param configName The name of the config (e.g., "settings.yml").
     * @return The FileConfiguration object.
     */
    FileConfiguration getConfig(String configName);

    /**
     * Gets the File object for a configuration by its name.
     *
     * @param fileName The name of the file (e.g., "settings.yml").
     * @return The File object.
     */
    File getConfigFile(String fileName);

    FileConfiguration getSettingsConfig();

    FileConfiguration getHotbarConfig();

    FileConfiguration getMessagesConfig();

    FileConfiguration getDatabaseConfig();

    FileConfiguration getKitsConfig();

    FileConfiguration getArenasConfig();

    FileConfiguration getScoreboardConfig();

    FileConfiguration getTabListConfig();

    FileConfiguration getTexturesConfig();

    FileConfiguration getDivisionsConfig();

    FileConfiguration getMenusConfig();

    FileConfiguration getTitlesConfig();

    FileConfiguration getLevelsConfig();

    FileConfiguration getPearlConfig();

    FileConfiguration getAbilityConfig();

    FileConfiguration getVisualsConfig();

    FileConfiguration getSaltyMessagesConfig();

    FileConfiguration getYeetMessagesConfig();

    FileConfiguration getNerdMessagesConfig();

    FileConfiguration getSpigotCommunityMessagesConfig();

}