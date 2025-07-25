package dev.revere.alley.base.kit;

import dev.revere.alley.plugin.lifecycle.Service;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface KitService extends Service {
    /**
     * Gets a list of all loaded kits.
     *
     * @return An unmodifiable list of kits.
     */
    List<Kit> getKits();

    /**
     * Gets a specific kit by its unique name (case-insensitive).
     *
     * @param name The name of the kit.
     * @return The Kit object, or null if not found.
     */
    Kit getKit(String name);

    /**
     * Saves a single kit to the configuration file.
     *
     * @param kit The kit to save.
     */
    void saveKit(Kit kit);

    /**
     * Creates and saves a new kit with default values.
     *
     * @param kitName The unique name for the new kit.
     * @param inventory The default inventory contents.
     * @param armor The default armor contents.
     * @param icon The material for the kit's menu icon.
     */
    void createKit(String kitName, ItemStack[] inventory, ItemStack[] armor, Material icon);

    /**
     * Deletes a kit from the service and the configuration file.
     *
     * @param kit The kit to delete.
     */
    void deleteKit(Kit kit);

    void saveKits();
}