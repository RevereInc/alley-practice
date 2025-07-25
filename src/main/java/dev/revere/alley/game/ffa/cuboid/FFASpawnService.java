package dev.revere.alley.game.ffa.cuboid;

import dev.revere.alley.plugin.lifecycle.Service;
import dev.revere.alley.tool.cuboid.Cuboid;
import org.bukkit.Location;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface FFASpawnService extends Service {
    /**
     * Gets the designated spawn point within the FFA arena.
     *
     * @return The spawn Location.
     */
    Location getSpawn();

    /**
     * Gets the minimum boundary point of the FFA safe zone cuboid.
     *
     * @return The minimum Location.
     */
    Location getMinimum();

    /**
     * Gets the maximum boundary point of the FFA safe zone cuboid.
     *
     * @return The maximum Location.
     */
    Location getMaximum();

    /**
     * Gets the Cuboid object representing the FFA safe zone.
     *
     * @return The Cuboid object, or null if not properly loaded.
     */
    Cuboid getCuboid();
}