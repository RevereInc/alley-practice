package dev.revere.alley.feature.troll;

import dev.revere.alley.bootstrap.lifecycle.Service;
import dev.revere.alley.feature.troll.internal.TrollServiceImpl;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Emmy
 * @project alley-practice
 * @since 01/09/2025
 */
public interface TrollService extends Service {
    /**
     * Opens a demo menu for the specified player.
     *
     * @param target The player for whom the demo menu will be opened.
     */
    void openDemoMenu(Player target);

    /**
     * Spawns a donut of fake boats around the target player.
     *
     * @param target The player around whom the donut will be spawned.
     */
    void spawnDonut(Player target);

    /**
     * Generates the positions for a hollow cube trap around the given location.
     *
     * @param location The center location.
     * @param radius   The horizontal radius of the cube from the center (e.g., radius 2 for a 5x5 cube).
     * @param height   The vertical height of the cube (e.g., height 4 for a 4-block high trap).
     * @return A list of locations representing the cube's walls.
     */
    List<Location> generateCube(Location location, int radius, int height);

    /**
     * Traps the specified player in a confined space for a given duration.
     *
     * @param trapper        The player initiating the trap.
     * @param target         The player to be trapped.
     * @param durationMillis The duration in milliseconds for which the player will be trapped. If negative, the trap is permanent.
     */
    void trapPlayer(Player trapper, Player target, long durationMillis);

    /**
     * Traps a player identified as a "monkey" in a cage, because thats where they belong.
     *
     * @param trapper The player initiating the trap.
     * @param target  The player to be trapped.
     */
    void trapMonkey(Player trapper, Player target);

    /**
     * Generates and returns the next unique fake entity ID.
     *
     * @return The next fake entity ID.
     */
    int getNextFakeEntityId();

    /**
     * Provides access to the MonkeyRegistry for managing monkey players.
     *
     * @return The MonkeyRegistry instance.
     */
    TrollServiceImpl.MonkeyRegistry getMonkeyRegistry();
}