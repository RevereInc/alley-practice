package dev.revere.alley.feature.troll;

import dev.revere.alley.bootstrap.lifecycle.Service;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 25/08/2025
 */
public interface TrollService extends Service {
    /**
     * Spawns a donut made of fake boat entities around the target player.
     * The donut will last for 60 seconds before being removed.
     *
     * @param target The player around whom the donut will be spawned.
     */
    void spawnDonut(Player target);

    /**
     * Opens a demo menu for the specified player.
     *
     * @param target The player for whom the demo menu will be opened.
     */
    void openDemoMenu(Player target);
}