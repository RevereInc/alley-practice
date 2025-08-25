package dev.revere.alley.visual.tab;

import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 07/09/2024 - 15:17
 */
public interface TabAdapter {
    /**
     * Retrieves the header lines for the tab.
     *
     * @param player The player for whom to retrieve the header.
     * @return A list of strings representing the header.
     */
    List<String> getHeader(Player player);

    /**
     * Retrieves the footer lines for the tab.
     *
     * @param player The player for whom to retrieve the footer.
     * @return A list of strings representing the footer.
     */
    List<String> getFooter(Player player);

    /**
     * Updates the tab for a specific player.
     *
     * @param player The player whose tab will be updated.
     */
    void update(Player player);
}