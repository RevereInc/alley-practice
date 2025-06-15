package dev.revere.alley.config;

import dev.revere.alley.Alley;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 19/04/2024 - 17:39
 */
@Getter
public class ConfigService {
    private final Map<String, FileConfiguration> fileConfigurations;
    private final Map<String, File> configFiles;

    private final FileConfiguration settingsConfig, messagesConfig,
            databaseConfig, kitsConfig, arenasConfig,
            scoreboardConfig, tabListConfig, divisionsConfig,
            menusConfig, titlesConfig, levelsConfig, abilitiesConfig;

    private final String[] configFileNames = {
            "settings.yml", "messages.yml", "menus.yml",
            "database/database.yml",
            "storage/kits.yml", "storage/arenas.yml", "storage/divisions.yml", "storage/titles.yml", "storage/levels.yml",
            "providers/scoreboard.yml", "providers/tablist.yml", "providers/abilities.yml"
    };

    public ConfigService() {
        this.configFiles = new HashMap<>();
        this.fileConfigurations = new HashMap<>();

        for (String fileName : this.configFileNames) {
            this.loadConfig(fileName);
        }

        this.settingsConfig = this.getConfig("settings.yml");
        this.messagesConfig = this.getConfig("messages.yml");
        this.databaseConfig = this.getConfig("database/database.yml");
        this.kitsConfig = this.getConfig("storage/kits.yml");
        this.arenasConfig = this.getConfig("storage/arenas.yml");
        this.scoreboardConfig = this.getConfig("providers/scoreboard.yml");
        this.tabListConfig = this.getConfig("providers/tablist.yml");
        this.abilitiesConfig = this.getConfig("providers/abilities.yml");
        this.divisionsConfig = this.getConfig("storage/divisions.yml");
        this.menusConfig = this.getConfig("menus.yml");
        this.titlesConfig = this.getConfig("storage/titles.yml");
        this.levelsConfig = this.getConfig("storage/levels.yml");
    }

    /**
     * Load a configuration file.
     *
     * @param fileName The name of the file.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void loadConfig(String fileName) {
        File configFile = new File(Alley.getInstance().getDataFolder(), fileName);
        this.configFiles.put(fileName, configFile);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            Alley.getInstance().saveResource(fileName, false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        this.fileConfigurations.put(fileName, config);
    }

    /**
     * Reload all configurations.
     */
    public void reloadConfigs() {
        for (String fileName : this.configFileNames) {
            this.loadConfig(fileName);
        }
    }

    /**
     * Save a configuration file.
     *
     * @param configFile        The file to save.
     * @param fileConfiguration The configuration to save.
     */
    public void saveConfig(File configFile, FileConfiguration fileConfiguration) {
        try {
            fileConfiguration.save(configFile);
            fileConfiguration.load(configFile);
        } catch (Exception e) {
            Logger.logError("Error occurred while saving config: " + configFile.getName());
        }
    }

    /**
     * Get a file configuration by its name.
     *
     * @param configName The name of the config file.
     * @return The file configuration.
     */
    public FileConfiguration getConfig(String configName) {
        return this.fileConfigurations.get(configName);
    }

    /**
     * Get a file by its name.
     *
     * @param fileName The name of the file.
     * @return The file.
     */
    public File getConfigFile(String fileName) {
        return this.configFiles.get(fileName);
    }
}