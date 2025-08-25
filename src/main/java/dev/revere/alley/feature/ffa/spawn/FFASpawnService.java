package dev.revere.alley.feature.ffa.spawn;

import dev.revere.alley.bootstrap.lifecycle.Service;
import dev.revere.alley.common.Cuboid;
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
     * Gets the minimum boundary point of the FFA safe zone geom.
     *
     * @return The minimum Location.
     */
    Location getMinimum();

    /**
     * Gets the maximum boundary point of the FFA safe zone geom.
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