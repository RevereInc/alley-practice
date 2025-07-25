package dev.revere.alley.feature.level;

import dev.revere.alley.plugin.lifecycle.Service;
import dev.revere.alley.feature.level.data.LevelData;

import java.util.List;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface LevelService extends Service {
    /**
     * Gets a list of all loaded level tiers.
     * @return An unmodifiable list of all LevelData objects.
     */
    List<LevelData> getLevels();

    /**
     * Creates a new level tier and saves it to the configuration.
     *
     * @param name   The unique name of the level.
     * @param minElo The minimum Elo rating for this level.
     * @param maxElo The maximum Elo rating for this level.
     */
    void createLevel(String name, int minElo, int maxElo);

    /**
     * Deletes an existing level from the service and the configuration.
     *
     * @param level The LevelData object to delete.
     */
    void deleteLevel(LevelData level);

    /**
     * Saves a level's data to the configuration file.
     *
     * @param level The LevelData object to save.
     */
    void saveLevel(LevelData level);

    /**
     * Gets the level tier that corresponds to a given Elo rating.
     *
     * @param elo The Elo rating to check.
     * @return The matching LevelData, or null if no level contains the Elo.
     */
    LevelData getLevel(int elo);

    /**
     * Gets a level tier by its unique name (case-insensitive).
     *
     * @param name The name of the level.
     * @return The matching LevelData, or null if not found.
     */
    LevelData getLevel(String name);

    /**
     * Generates a visual progress bar for the given elo.
     *
     * @param elo the elo to check.
     * @return The formatted progress bar string.
     */
    String getProgressBar(int elo);

    /**
     * Generates the numerical details for level progression (e.g., 500/1000).
     *
     * @param elo the elo to check.
     * @return The formatted progress details string.
     */
    String getProgressDetails(int elo);
}