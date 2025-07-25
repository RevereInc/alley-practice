package dev.revere.alley.tool.elo;

import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.tool.elo.range.EloRangeFactor;
import lombok.Getter;

/**
 * @author Remi
 * @project Alley
 * @date 6/2/2024
 */
@Getter
@Service(provides = EloCalculator.class, priority = 290)
public class EloCalculatorImpl implements EloCalculator {
    private final EloRangeFactor[] ELO_RANGES = {
            new EloRangeFactor(0, 1100, 25),
            new EloRangeFactor(1001, 1400, 20),
            new EloRangeFactor(1401, 1800, 15),
            new EloRangeFactor(1801, 2200, 10)
    };

    private final int DEFAULT_RANGE_FACTOR = 25;

    @Override
    public int determineNewElo(int playerElo, int opponentElo, boolean playerWon) {
        int score = playerWon ? 1 : 0;
        return this.calculateElo(playerElo, opponentElo, score);
    }

    @Override
    public int calculateElo(int playerElo, int opponentElo, int score) {
        double range = this.determineRange(playerElo);
        double expectedScore = this.calculateExpectedScore(playerElo, opponentElo);
        int updatedElo = (int) (playerElo + range * (score - expectedScore));

        if (score == 1 && updatedElo == playerElo) {
            updatedElo++;
        }
        return updatedElo;
    }

    /**
     * Determines the expected outcome for a player against an opponent based on Elo ratings.
     * An expected score of 0.5 means the players are evenly matched.
     *
     * @param playerElo   The player's current Elo rating.
     * @param opponentElo The opponent's Elo rating.
     * @return The expected score for the player (a value between 0 and 1).
     */
    private double calculateExpectedScore(int playerElo, int opponentElo) {
        return 1.0 / (1.0 + Math.pow(10, (opponentElo - playerElo) / 400.0));
    }

    /**
     * Determines the K-factor (range factor) for the player's Elo rating.
     *
     * @param elo The player's Elo rating.
     * @return The K-factor (range).
     */
    private double determineRange(int elo) {
        for (EloRangeFactor range : this.ELO_RANGES) {
            if (range.isInRange(elo)) {
                return range.getFactor();
            }
        }
        return this.DEFAULT_RANGE_FACTOR;
    }
}