package dev.revere.alley.base.visibility;

import dev.revere.alley.plugin.lifecycle.Service;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface VisibilityService extends Service {
    /**
     * Updates the visibility for a specific player in relation to all other online players,
     * and also updates how all other players see this specific player.
     * <p>
     * This is the main method to call whenever a player's state changes.
     *
     * @param player The player whose visibility state needs a full update.
     */
    void updateVisibility(Player player);

}