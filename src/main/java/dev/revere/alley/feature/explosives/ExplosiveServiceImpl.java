package dev.revere.alley.feature.explosives;

import dev.revere.alley.Alley;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.feature.explosives.listener.ExplosiveListener;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

/**
 * @author Emmy
 * @project Alley
 * @since 11/06/2025
 */
@Setter
@Getter
@Service(provides = ExplosiveService.class, priority = 370)
public class ExplosiveServiceImpl implements ExplosiveService {
    private final Alley plugin;
    private final ConfigService configService;

    private double explosionRange;
    private double horizontal;
    private double vertical;
    private double range;
    private double speed;
    private int tntFuseTicks;
    private boolean enabled;

    /**
     * Constructor for DI.
     */
    public ExplosiveServiceImpl(Alley plugin, ConfigService configService) {
        this.plugin = plugin;
        this.configService = configService;
    }

    @Override
    public void initialize(AlleyContext context) {
        FileConfiguration settingsConfig = this.configService.getSettingsConfig();
        this.enabled = settingsConfig.getBoolean("explosive.enabled", false);

        if (this.enabled) {
            this.assignValues(settingsConfig);
            this.registerListener();
        }
    }

    /**
     * Assigns the values for the explosive knockback settings from the configuration.
     *
     * @param settingsConfig The configuration file containing the settings.
     */
    private void assignValues(FileConfiguration settingsConfig) {
        this.horizontal = settingsConfig.getDouble("explosive.values.horizontal");
        this.vertical = settingsConfig.getDouble("explosive.values.vertical");
        this.range = settingsConfig.getDouble("explosive.values.range");
        this.speed = settingsConfig.getDouble("explosive.values.speed");
        this.tntFuseTicks = settingsConfig.getInt("explosive.values.tnt-fuse-ticks");
        this.explosionRange = settingsConfig.getDouble("explosive.values.explosion-range");
    }


    @Override
    public void save() {
        FileConfiguration settingsConfig = this.configService.getSettingsConfig();
        File settingsFile = this.configService.getConfigFile("settings.yml");

        settingsConfig.set("explosive.values.horizontal", this.horizontal);
        settingsConfig.set("explosive.values.vertical", this.vertical);
        settingsConfig.set("explosive.values.range", this.range);
        settingsConfig.set("explosive.values.speed", this.speed);
        settingsConfig.set("explosive.values.tnt-fuse-ticks", this.tntFuseTicks);
        settingsConfig.set("explosive.values.explosion-range", this.explosionRange);

        this.configService.saveConfig(settingsFile, settingsConfig);
    }

    private void registerListener() {
        this.plugin.getServer().getPluginManager().registerEvents(new ExplosiveListener(), this.plugin);
    }
}
