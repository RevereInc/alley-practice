package dev.revere.alley.core.profile.progress;

import dev.revere.alley.common.text.ProgressBarUtil;
import dev.revere.alley.feature.division.Division;
import dev.revere.alley.feature.division.model.DivisionTier;
import lombok.Getter;

/**
 * @author Emmy
 * @project alley-practice
 * @date 29/09/2025
 */
@Getter
public class PlayerProgress {
    private final Division currentDivision;
    private final Division nextDivision;

    private final DivisionTier currentTier;
    private final DivisionTier nextTier;

    private final int currentWins;
    private final int remainingWins;

    /**
     * Constructor for the PlayerProgress class.
     *
     * @param currentDivision The player's current division.
     * @param nextDivision    The player's next division (if applicable).
     * @param currentTier     The player's current tier within their division.
     * @param nextTier        The player's next tier within their division (if applicable).
     * @param currentWins     The player's current number of wins.
     * @param remainingWins   The number of wins remaining to reach the next tier or division.
     */
    public PlayerProgress(Division currentDivision, Division nextDivision, DivisionTier currentTier, DivisionTier nextTier, int currentWins, int remainingWins) {
        this.currentDivision = currentDivision;
        this.nextDivision = nextDivision;
        this.currentTier = currentTier;
        this.nextTier = nextTier;
        this.currentWins = currentWins;
        this.remainingWins = remainingWins;
    }

    /**
     * Generates a visual progress bar string that starts from current wins to wins required (next tier).
     *
     * @param length The desired length of the bar.
     * @param symbol The character to use for the bar.
     * @return A formatted progress bar.
     */
    public String getProgressBar(int length, String symbol) {
        int startWins = 0;
        int requiredWins = 0;

        if (this.nextTier != null && this.currentTier != null) {
            startWins = this.currentTier.getRequiredWins();
            requiredWins = this.nextTier.getRequiredWins();
        } else if (this.nextDivision != null && this.currentTier != null) {
            startWins = this.currentTier.getRequiredWins();
            DivisionTier firstTier = this.nextDivision.getTiers().isEmpty() ? null : this.nextDivision.getTiers().get(0);
            if (firstTier != null) {
                requiredWins = firstTier.getRequiredWins();
            }
        }

        if (requiredWins <= startWins) {
            return ProgressBarUtil.generate(1, 1, length, symbol);
        }

        int progress = this.currentWins - startWins;
        int totalNeeded = requiredWins - startWins;
        return ProgressBarUtil.generate(progress, totalNeeded, length, symbol);
    }

    /**
     * Calculates and returns the progress as a percentage string.
     * Progress is from current wins to wins required (next tier).
     *
     * @return The progress as a formatted percentage string (e.g., "75%").
     */
    public String getProgressPercentage() {
        int startWins = 0;
        int requiredWins = 0;

        if (this.nextTier != null && this.currentTier != null) {
            startWins = this.currentTier.getRequiredWins();
            requiredWins = this.nextTier.getRequiredWins();
        } else if (this.nextDivision != null && this.currentTier != null) {
            startWins = this.currentTier.getRequiredWins();
            DivisionTier firstTier = this.nextDivision.getTiers().isEmpty() ? null : this.nextDivision.getTiers().get(0);
            if (firstTier != null) {
                requiredWins = firstTier.getRequiredWins();
            }
        }

        if (requiredWins <= startWins) {
            return "100%";
        }

        double progress = ((double) (this.currentWins - startWins) / (requiredWins - startWins)) * 100;
        return String.format("%.0f%%", Math.min(progress, 100.0));
    }
}