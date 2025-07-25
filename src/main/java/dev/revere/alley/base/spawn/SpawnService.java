package dev.revere.alley.base.spawn;

import dev.revere.alley.plugin.lifecycle.Service;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface SpawnService extends Service {
    /**
     * Gets the currently loaded spawn location.
     *
     * @return The spawn Location, or null if not set.
     */
    Location getLocation();

    /**
     * Sets or updates the spawn location and saves it to the configuration file.
     *
     * @param location The new spawn location.
     */
    void updateSpawnLocation(Location location);

    /**
     * Teleports a player to the configured spawn location.
     *
     * @param player The player to teleport.
     */
    void teleportToSpawn(Player player);
}