package dev.revere.alley.base.server;

import dev.revere.alley.plugin.lifecycle.Service;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface ServerService extends Service {
    /**
     * Checks if players are currently allowed to join matchmaking queues.
     *
     * @return true if queueing is allowed, false otherwise.
     */
    boolean isQueueingAllowed();

    /**
     * Sets whether players are allowed to join matchmaking queues.
     *
     * @param allowed The new queueing status.
     */
    void setQueueingAllowed(boolean allowed);

    /**
     * Forcefully ends all active matches on the server.
     *
     * @param issuer The staff member who initiated this action (can be null for console).
     */
    void endAllMatches(Player issuer);

    /**
     * Disbands all active parties on the server.
     *
     * @param issuer The staff member who initiated this action (can be null for console).
     */
    void disbandAllParties(Player issuer);

    /**
     * Removes all players from all matchmaking queues.
     *
     * @param issuer The staff member who initiated this action (can be null for console).
     */
    void clearAllQueues(Player issuer);

    /**
     * Loads the list of materials that are blocked from crafting.
     * This method should be called during server startup to initialize the blocked crafting materials.
     */
    void loadBlockedCraftingItems();

    /**
     * Retrieves a list of materials that are blocked from crafting.
     *
     * @return A list of blocked crafting materials.
     */
    Set<Material> getBlockedCraftingItems();

    /**
     * Adds a material to the list of blocked crafting materials.
     *
     * @param material The material to block from crafting.
     */
    void removeFromBlockedCraftingList(Material material);

    /**
     * Removes a material from the list of blocked crafting materials.
     *
     * @param material The material to unblock from crafting.
     */
    void addToBlockedCraftingList(Material material);

    /**
     * Checks if an item is craftable.
     *
     * @param material The material to check.
     * @return true if the crafting recipe is valid, false otherwise.
     */
    boolean isCraftable(Material material);

    /**
     * Saves the current crafting conditions to the configuration file.
     * This method should be called when the server is shutting down or when
     * the crafting conditions are modified.
     */
    void saveBlockedItems(Material material);
}