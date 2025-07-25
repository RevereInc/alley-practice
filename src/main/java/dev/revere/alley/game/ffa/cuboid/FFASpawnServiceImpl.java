package dev.revere.alley.game.ffa.cuboid;

import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.arena.ArenaService;
import dev.revere.alley.base.arena.enums.ArenaType;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.tool.cuboid.Cuboid;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.tool.serializer.Serializer;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Emmy
 * @project Alley
 * @date 12/06/2024 - 22:14
 */
@Getter
@Service(provides = FFASpawnService.class, priority = 250)
public class FFASpawnServiceImpl implements FFASpawnService {
    private final ConfigService configService;
    private final ArenaService arenaService;

    private Location minimum;
    private Location maximum;
    private Location spawn;
    private Cuboid cuboid;

    /**
     * Constructor for DI.
     */
    public FFASpawnServiceImpl(ConfigService configService, ArenaService arenaService) {
        this.configService = configService;
        this.arenaService = arenaService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.loadCuboid();
    }

    /**
     * Load the FFA spawn location from the arenas.yml file
     */
    public void loadCuboid() {
        FileConfiguration config = this.configService.getArenasConfig();
        Arena arena = this.arenaService.getArenas().stream()
                .filter(a -> a.getType() == ArenaType.FFA)
                .findFirst()
                .orElse(null);

        if (arena == null) {
            Logger.error("FFA arena not found!");
            return;
        }

        String basePath = "arenas." + arena.getName();
        this.spawn = Serializer.deserializeLocation(config.getString(basePath + ".pos1"));
        this.minimum = Serializer.deserializeLocation(config.getString(basePath + ".safe-zone.pos1"));
        this.maximum = Serializer.deserializeLocation(config.getString(basePath + ".safe-zone.pos2"));

        if (this.minimum == null || this.maximum == null) {
            Logger.error("FFA safezone not found! Please set the ffa arena safezone and save it using the /arena save command.");
            return;
        }

        this.cuboid = new Cuboid(this.minimum, this.maximum);
    }

    public Cuboid getCuboid() {
        if (this.cuboid == null) {
            return null;
        }
        return cuboid;
    }
}