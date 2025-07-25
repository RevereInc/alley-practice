package dev.revere.alley.config;

import dev.revere.alley.Alley;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
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
@Service(provides = ConfigService.class, priority = 20)
public class ConfigServiceImpl implements ConfigService {
    private final Alley plugin;

    private final Map<String, FileConfiguration> fileConfigurations = new HashMap<>();
    private final Map<String, File> configFiles = new HashMap<>();

    private FileConfiguration settingsConfig, messagesConfig, databaseConfig, kitsConfig, arenasConfig,
            scoreboardConfig, tabListConfig, divisionsConfig, menusConfig, titlesConfig, levelsConfig,
            pearlConfig, abilityConfig, visualsConfig, saltyMessagesConfig, yeetMessagesConfig,
            nerdMessagesConfig, spigotCommunityMessagesConfig, texturesConfig, hotbarConfig;

    private final String[] configFileNames = {
            "settings.yml", "messages.yml", "menus.yml", "pearls.yml", "abilities.yml", "visuals.yml",

            "database/database.yml",

            "storage/kits.yml", "storage/arenas.yml", "storage/divisions.yml", "storage/titles.yml", "storage/levels.yml",

            "providers/scoreboard.yml", "providers/tablist.yml", "providers/textures.yml", "providers/hotbar.yml",

            "cosmetics/messages/salty_messages.yml", "cosmetics/messages/yeet_messages.yml",
            "cosmetics/messages/nerd_messages.yml", "cosmetics/messages/spigot_community_messages.yml",
    };

    /**
     * Constructor for DI. Receives the main plugin instance from the AlleyContext.
     */
    public ConfigServiceImpl(Alley plugin) {
        this.plugin = plugin;
    }

    @Override
    public void setup(AlleyContext context) {
        for (String fileName : this.configFileNames) {
            this.loadConfig(fileName);
        }

        this.assignConfigs();
    }

    @Override
    public void reloadConfigs() {
        this.fileConfigurations.clear();
        this.configFiles.clear();
        for (String fileName : this.configFileNames) {
            this.loadConfig(fileName);
        }

        this.assignConfigs();
    }

    @Override
    public void saveConfig(File configFile, FileConfiguration fileConfiguration) {
        try {
            fileConfiguration.save(configFile);
        } catch (Exception e) {
            Logger.logException("Error occurred while saving config: " + configFile.getName(), e);
        }
    }

    @Override
    public FileConfiguration getConfig(String configName) {
        return this.fileConfigurations.get(configName);
    }

    @Override
    public File getConfigFile(String fileName) {
        return this.configFiles.get(fileName);
    }

    private void assignConfigs() {
        this.menusConfig = this.getConfig("menus.yml");
        this.pearlConfig = this.getConfig("pearls.yml");
        this.abilityConfig = this.getConfig("abilities.yml");
        this.visualsConfig = this.getConfig("visuals.yml");
        this.settingsConfig = this.getConfig("settings.yml");
        this.messagesConfig = this.getConfig("messages.yml");
        this.databaseConfig = this.getConfig("database/database.yml");
        this.kitsConfig = this.getConfig("storage/kits.yml");
        this.arenasConfig = this.getConfig("storage/arenas.yml");
        this.titlesConfig = this.getConfig("storage/titles.yml");
        this.levelsConfig = this.getConfig("storage/levels.yml");
        this.divisionsConfig = this.getConfig("storage/divisions.yml");
        this.tabListConfig = this.getConfig("providers/tablist.yml");
        this.texturesConfig = this.getConfig("providers/textures.yml");
        this.scoreboardConfig = this.getConfig("providers/scoreboard.yml");
        this.hotbarConfig = this.getConfig("providers/hotbar.yml");
        this.saltyMessagesConfig = this.getConfig("cosmetics/messages/salty_messages.yml");
        this.yeetMessagesConfig = this.getConfig("cosmetics/messages/yeet_messages.yml");
        this.nerdMessagesConfig = this.getConfig("cosmetics/messages/nerd_messages.yml");
        this.spigotCommunityMessagesConfig = this.getConfig("cosmetics/messages/spigot_community_messages.yml");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void loadConfig(String fileName) {
        File configFile = new File(this.plugin.getDataFolder(), fileName);
        this.configFiles.put(fileName, configFile);

        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            this.plugin.saveResource(fileName, false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        this.fileConfigurations.put(fileName, config);
    }
}