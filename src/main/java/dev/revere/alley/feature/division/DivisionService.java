package dev.revere.alley.feature.division;

import dev.revere.alley.plugin.lifecycle.Service;

import java.util.List;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface DivisionService extends Service {
    /**
     * Gets a sorted list of all loaded divisions.
     * @return An unmodifiable list of all divisions.
     */
    List<Division> getDivisions();

    /**
     * Gets a specific division by its unique name (case-insensitive).
     * @param name The name of the division.
     * @return The Division object, or null if not found.
     */
    Division getDivision(String name);

    /**
     * Creates a new division with default tiers and saves it to the configuration.
     * @param name The unique name for the new division.
     * @param requiredWins The number of wins required for the first tier of this division.
     */
    void createDivision(String name, int requiredWins);

    /**
     * Deletes a division from the service and the configuration file.
     * @param name The name of the division to delete.
     */
    void deleteDivision(String name);

    /**
     * Saves a single division object to the configuration file.
     * @param division The division to save.
     */
    void saveDivision(Division division);

    /**
     * Finds the highest-ranked division based on the required wins.
     * @return The highest division, or null if no divisions are loaded.
     */
    Division getHighestDivision();
}