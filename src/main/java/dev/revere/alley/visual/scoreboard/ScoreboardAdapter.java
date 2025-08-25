package dev.revere.alley.visual.scoreboard;

import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Emmy
 * @project alley-practice
 * @since 25/08/2025
 */
public interface ScoreboardAdapter {
    /**
     * Gets the title of the scoreboard for the specified player.
     *
     * @param player The player for whom to get the title.
     * @return The title string.
     */
    String getTitle(Player player);

    /**
     * Gets the lines of the scoreboard for the specified player.
     *
     * @param player The player for whom to get the lines.
     * @return A list of strings representing the scoreboard lines.
     */
    List<String> getLines(Player player);
}