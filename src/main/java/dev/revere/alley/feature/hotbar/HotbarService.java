package dev.revere.alley.feature.hotbar;

import dev.revere.alley.bootstrap.lifecycle.Service;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.library.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Emmy
 * @project alley-practice
 * @since 21/07/2025
 */
public interface HotbarService extends Service {
    /**
     * Method to retrieve the list of hotbar items.
     *
     * @return A list of HotbarItem objects representing the items in the hotbar.
     */
    List<HotbarItem> getHotbarItems();

    /**
     * Applies a specific type of hotbar layout to a player's inventory.
     *
     * @param player The player to apply the hotbar to.
     * @param type   The type of hotbar to apply.
     */
    void applyHotbarItems(Player player, HotbarType type);

    /**
     * Determines the correct hotbar type based on the player's current profile state
     * and applies it to their inventory.
     *
     * @param player The player to apply the hotbar to.
     */
    void applyHotbarItems(Player player);

    /**
     * Creates a new hotbar item with the specified name and type.
     *
     * @param name The name of the hotbar item to create.
     * @param type The type of the hotbar item to create.
     */
    void createHotbarItem(String name, HotbarType type);

    /**
     * Deletes a hotbar item by its object reference.
     *
     * @param hotbarItem The HotbarItem object to delete.
     */
    void deleteHotbarItem(HotbarItem hotbarItem);

    /**
     * Deletes a hotbar item by its object.
     *
     * @param hotbarItem The name of the hotbar item to delete.
     */
    void saveToConfig(HotbarItem hotbarItem);

    /**
     * Builds an ItemStack representation of a hotbar item that can be received by the player.
     *
     * @param hotbarItem The HotbarItem to build the ItemStack for.
     * @return An ItemStack representing the hotbar item, ready to be given to the player.
     */
    ItemStack buildReceivableItem(HotbarItem hotbarItem);

    /**
     * Retrieves the list of hotbar items for a specific hotbar type.
     *
     * @param type The type of hotbar to retrieve items for.
     * @return A list of HotbarItem objects corresponding to the specified type.
     */
    List<HotbarItem> getItemsForType(HotbarType type);

    /**
     * Get the corresponding hotbar type for the given profile.
     *
     * @param profile the profile
     * @return the corresponding hotbar type
     */
    HotbarType getCorrespondingType(Profile profile);

    /**
     * Retrieves the HotbarItem associated with the given ItemStack and hotbar type.
     *
     * @param itemStack The item stack to check.
     * @param type      The type of hotbar to retrieve the item for.
     * @return The HotbarItem associated with the item stack and type, or null if not found.
     */
    HotbarItem getHotbarItem(ItemStack itemStack, HotbarType type);

    /**
     * Method to retrieve the given hotbar item object.
     *
     * @param name the name of the item.
     * @return the hotbar item.
     */
    HotbarItem getHotbarItem(String name);

    /**
     * Gets a menu instance by a given name.
     *
     * @param name the name of the menu
     * @return the menu instance
     */
    Menu getMenuInstanceFromName(String name, Player player);
}
