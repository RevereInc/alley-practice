package dev.revere.alley.base.arena.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.arena.ArenaService;
import dev.revere.alley.base.arena.enums.ArenaType;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.tool.serializer.Serializer;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Emmy
 * @project Alley
 * @date 20/05/2024 - 19:14
 */
public class SharedArena extends Arena {
    protected final Alley plugin = Alley.getInstance();

    /**
     * Constructor for the SharedArena class.
     *
     * @param name    The name of the arena.
     * @param minimum The minimum location of the arena.
     * @param maximum The maximum location of the arena.
     */
    public SharedArena(String name, Location minimum, Location maximum) {
        super(name, minimum, maximum);
    }

    @Override
    public ArenaType getType() {
        return ArenaType.SHARED;
    }

    @Override
    public void createArena() {
        ArenaService arenaService = this.plugin.getService(ArenaService.class);
        arenaService.registerNewArena(this);
        this.saveArena();
    }

    @Override
    public void saveArena() {
        String name = "arenas." + this.getName();
        FileConfiguration config = this.plugin.getService(ConfigService.class).getArenasConfig();

        config.set(name, null);
        config.set(name + ".type", this.getType().name());
        config.set(name + ".minimum", Serializer.serializeLocation(this.getMinimum()));
        config.set(name + ".maximum", Serializer.serializeLocation(this.getMaximum()));
        config.set(name + ".center", Serializer.serializeLocation(this.getCenter()));
        config.set(name + ".pos1", Serializer.serializeLocation(this.getPos1()));
        config.set(name + ".pos2", Serializer.serializeLocation(this.getPos2()));
        config.set(name + ".kits", this.getKits());
        config.set(name + ".enabled", this.isEnabled());
        config.set(name + ".display-name", this.getDisplayName());

        this.plugin.getService(ConfigService.class).saveConfig(this.plugin.getService(ConfigService.class).getConfigFile("storage/arenas.yml"), config);
    }

    @Override
    public void deleteArena() {
        FileConfiguration config = this.plugin.getService(ConfigService.class).getArenasConfig();
        config.set("arenas." + this.getName(), null);

        this.plugin.getService(ConfigService.class).saveConfig(this.plugin.getService(ConfigService.class).getConfigFile("storage/arenas.yml"), config);
    }
}