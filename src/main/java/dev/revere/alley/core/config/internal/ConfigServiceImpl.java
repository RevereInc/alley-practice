package dev.revere.alley.core.config.internal;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.bootstrap.AlleyContext;
import dev.revere.alley.bootstrap.annotation.Service;
import dev.revere.alley.common.TaskUtil;
import dev.revere.alley.common.logger.Logger;
import dev.revere.alley.core.config.ConfigService;
import lombok.Getter;
import org.bukkit.command.CommandSender;
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
    private final AlleyPlugin plugin;

    private final Map<String, FileConfiguration> fileConfigurations = new HashMap<>();
    private final Map<String, File> configFiles = new HashMap<>();

    private FileConfiguration settingsConfig, globalMessagesConfig, gameMessagesConfig, databaseConfig, kitsConfig, arenasConfig,
            scoreboardConfig, tabListConfig, divisionsConfig, menusConfig, titlesConfig, levelsConfig,
            pearlConfig, abilityConfig, visualsConfig, saltyMessagesConfig, yeetMessagesConfig,
            nerdMessagesConfig, spigotCommunityMessagesConfig, texturesConfig, hotbarConfig;

    private final String[] configFileNames = {
            "database/database.yml",

            "messages/global-messages.yml", "messages/game-messages.yml",
            "settings.yml", "menus.yml",

            "storage/kits.yml", "storage/arenas.yml", "storage/divisions.yml", "storage/titles.yml", "storage/levels.yml",

            "providers/scoreboard.yml", "providers/tab-list.yml", "providers/textures.yml", "storage/hotbar.yml",
            "providers/pearls.yml", "providers/abilities.yml", "providers/visuals.yml",

            "cosmetics/messages/salty_messages.yml", "cosmetics/messages/yeet_messages.yml",
            "cosmetics/messages/nerd_messages.yml", "cosmetics/messages/spigot_community_messages.yml",
    };

    /**
     * Constructor for DI. Receives the main bootstrap instance from the AlleyContext.
     */
    public ConfigServiceImpl(AlleyPlugin plugin) {
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

    /**
     * Reloads all configuration files with detailed debug output to the sender and console.
     *
     * @param sender The command sender who initiated the reload.
     */
    private void reloadConfigsAndDebug(CommandSender sender) {
        long timeTaken = System.nanoTime();

        try {
            Logger.sendMessageAndLog(sender, "");
            Logger.sendMessageAndLog(sender, "&fReload process &a&lSTARTING&f.");
            Logger.sendMessageAndLog(sender, "&fPlease wait...");
            Logger.sendMessageAndLog(sender, "");

            TaskUtil.runLaterAsync(() -> {
                long totalReloadTime = 0L;
                long delay = 0L;
                long delayIncrement = 5L;

                for (String fileName : this.configFileNames) {
                    long startTime = System.nanoTime();

                    this.loadConfig(fileName);

                    long endTime = System.nanoTime();
                    long timeTakenForFile = (endTime - startTime) / 1_000_000;

                    totalReloadTime += timeTakenForFile;

                    TaskUtil.runLaterAsync(() -> {
                        Logger.sendMessageAndLog(sender, "&a✔  &6" + fileName);
                    }, delay);

                    delay += delayIncrement;
                }

                long finalTotalReloadTime = totalReloadTime;

                TaskUtil.runLaterAsync(() -> {
                    this.assignConfigs();

                    Logger.sendMessageAndLog(sender, "");
                    Logger.sendMessageAndLog(sender, "&fConfig files &a&lRE&r-&a&lASSIGNED&f.");

                    long overallEndTime = System.nanoTime();
                    long overallTimeTaken = (overallEndTime - timeTaken) / 1_000_000;

                    Logger.sendMessageAndLog(sender, "&fReload process &a&lCOMPLETED&r in &6" + overallTimeTaken + "&f ms.");
                    Logger.sendMessageAndLog(sender, "&fIndividual file reloads took a total of &6" + finalTotalReloadTime + "&f ms.");
                }, delay + delayIncrement);
            }, 20L);
        } catch (Exception exception) {
            Logger.sendMessageAndLog(sender, "&c✘  &c&lFAILURE: " + exception.getMessage());
            Logger.logException("Error occurred while reloading configs.", exception);
        }
    }

    @Override
    public void saveConfig(File configFile, FileConfiguration fileConfiguration) {
        try {
            fileConfiguration.save(configFile);
        } catch (Exception exception) {
            Logger.logException("Error occurred while saving config: " + configFile.getName(), exception);
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
        this.pearlConfig = this.getConfig("providers/pearls.yml");
        this.abilityConfig = this.getConfig("providers/abilities.yml");
        this.visualsConfig = this.getConfig("providers/visuals.yml");
        this.settingsConfig = this.getConfig("settings.yml");
        this.globalMessagesConfig = this.getConfig("messages/global-messages.yml");
        this.gameMessagesConfig = this.getConfig("messages/game-messages.yml");
        this.databaseConfig = this.getConfig("database/database.yml");
        this.kitsConfig = this.getConfig("storage/kits.yml");
        this.arenasConfig = this.getConfig("storage/arenas.yml");
        this.titlesConfig = this.getConfig("storage/titles.yml");
        this.levelsConfig = this.getConfig("storage/levels.yml");
        this.divisionsConfig = this.getConfig("storage/divisions.yml");
        this.tabListConfig = this.getConfig("providers/tab-list.yml");
        this.texturesConfig = this.getConfig("providers/textures.yml");
        this.scoreboardConfig = this.getConfig("providers/scoreboard.yml");
        this.hotbarConfig = this.getConfig("storage/hotbar.yml");
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