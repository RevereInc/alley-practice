package dev.revere.alley.base.nametag;

import dev.revere.alley.plugin.lifecycle.Service;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface NametagService extends Service {
    /**
     * This is the main method to call when a player's state changes (e.g., joining/leaving a match).
     * It triggers a full, two-way re-evaluation of nametags between the specified player
     * and all other online players.
     *
     * @param player The player whose state has changed.
     */
    void updatePlayerState(Player player);
}