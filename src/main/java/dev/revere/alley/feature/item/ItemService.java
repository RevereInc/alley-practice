package dev.revere.alley.feature.item;

import dev.revere.alley.plugin.lifecycle.Service;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project alley-practice
 * @since 18/07/2025
 */
public interface ItemService extends Service {
    /**
     * Retrieves the golden head item stack.
     *
     * @return The ItemStack representing the golden head.
     */
    ItemStack getGoldenHead();

    /**
     * Performs the consume action for a golden head item when used by a player.
     *
     * @param player The player who is consuming the item.
     * @param item The ItemStack representing the golden head being consumed.
     */
    void performHeadConsume(Player player, ItemStack item);
}