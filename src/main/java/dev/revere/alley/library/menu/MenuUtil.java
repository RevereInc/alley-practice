package dev.revere.alley.library.menu;

import dev.revere.alley.library.menu.pagination.PaginatedMenu;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 24/01/2025
 */
@UtilityClass
public class MenuUtil {
    /**
     * Checks if the player has a next page.
     *
     * @param player the player viewing the menu
     * @param offset the delta to modify the page number by
     * @param menu   the menu
     * @return true if the player has a next page
     */
    public boolean hasNext(Player player, int offset, PaginatedMenu menu) {
        int pg = menu.getPage() + offset;
        return menu.getPages(player) >= pg;
    }

    /**
     * Checks if the player has a previous page.
     *
     * @param offset the delta to modify the page number by
     * @param menu   the menu
     * @return true if the player has a previous page
     */
    public boolean hasPrevious(int offset, PaginatedMenu menu) {
        int pg = menu.getPage() + offset;
        return pg > 0;
    }
}