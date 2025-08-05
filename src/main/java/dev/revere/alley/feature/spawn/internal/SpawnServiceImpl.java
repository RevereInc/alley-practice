package dev.revere.alley.feature.spawn.internal;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.bootstrap.AlleyContext;
import dev.revere.alley.bootstrap.annotation.Service;
import dev.revere.alley.common.logger.Logger;
import dev.revere.alley.common.serializer.Serializer;
import dev.revere.alley.common.PlayerUtil;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.spawn.SpawnService;
import dev.revere.alley.feature.spawn.task.SpawnReminderTask;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 17/05/2024 - 17:47
 */
@Getter
@Service(provides = SpawnService.class, priority = 240)
public class SpawnServiceImpl implements SpawnService {
    private final ConfigService configService;
    private Location location;

    /**
     * Constructor for DI.
     */
    public SpawnServiceImpl(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.loadSpawnLocation();
    }

    private void loadSpawnLocation() {
        FileConfiguration config = this.configService.getSettingsConfig();
        Location location = Serializer.deserializeLocation(config.getString("spawn.join-location"));
        if (location == null) {
            new SpawnReminderTask().runTaskTimer(AlleyPlugin.getInstance(), 0L, 50L);
            Logger.error("Spawn location is null.");
            return;
        }

        this.location = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    @Override
    public void updateSpawnLocation(Location location) {
        if (location == null) return;

        this.location = location;

        FileConfiguration config = this.configService.getSettingsConfig();
        config.set("spawn.join-location", Serializer.serializeLocation(location));
        this.configService.saveConfig(this.configService.getConfigFile("settings.yml"), config);
    }

    @Override
    public void teleportToSpawn(Player player) {
        if (this.location == null) {
            Logger.error("Cannot teleport " + player.getName() + " to spawn: Spawn location is not set.");
            player.sendMessage(CC.translate("&cThe server spawn is not set. Please notify an administrator."));
            return;
        }

        player.teleport(this.location);
        PlayerUtil.reset(player, false, true);
    }
}