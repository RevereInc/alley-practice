package dev.revere.alley.feature.layout;

import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.plugin.lifecycle.Service;
import dev.revere.alley.feature.layout.data.LayoutData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface LayoutService extends Service {
    /**
     * Gets the menu instance used for the kit layout editor.
     *
     * @return The layout editor Menu.
     */
    Menu getLayoutMenu();

    /**
     * Creates the specific ItemStack (a book) that represents a single kit layout.
     *
     * @param layout The layout data to represent.
     * @return The ItemStack representing the layout book.
     */
    ItemStack getLayoutBook(LayoutData layout);

    /**
     * Gives a player all the layout selection books for a specific kit.
     *
     * @param player  The player to give the books to.
     * @param kitName The name of the kit.
     */
    void giveBooks(Player player, String kitName);
}