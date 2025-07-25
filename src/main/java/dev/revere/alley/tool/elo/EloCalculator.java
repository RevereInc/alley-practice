package dev.revere.alley.tool.elo;

import dev.revere.alley.plugin.lifecycle.Service;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface EloCalculator extends Service {
    /**
     * Calculates the new Elo rating for a player based on a simple win/loss result.
     *
     * @param playerElo   The player's current Elo rating.
     * @param opponentElo The opponent's Elo rating.
     * @param playerWon   True if the player won the match, false otherwise.
     * @return The player's updated Elo rating.
     */
    int determineNewElo(int playerElo, int opponentElo, boolean playerWon);

    /**
     * Calculates the new Elo rating for a player based on a given match score.
     *
     * @param playerElo   The player's current Elo rating.
     * @param opponentElo The opponent's Elo rating.
     * @param score       The score of the player (typically 1 for a win, 0 for a loss).
     * @return The updated Elo rating.
     */
    int calculateElo(int playerElo, int opponentElo, int score);
}