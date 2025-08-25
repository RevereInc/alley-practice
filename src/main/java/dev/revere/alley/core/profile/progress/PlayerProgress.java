package dev.revere.alley.core.profile.progress;

import dev.revere.alley.common.text.ProgressBarUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
@Getter
@RequiredArgsConstructor
public class PlayerProgress {
    private final int currentWins;
    private final int winsForNextTier;
    private final String nextRankName;
    private final boolean isMaxRank;

    /**
     * Generates a visual progress bar string.
     *
     * @param length The desired length of the bar.
     * @param symbol The character to use for the bar.
     * @return A formatted progress bar.
     */
    public String getProgressBar(int length, String symbol) {
        return ProgressBarUtil.generate(this.currentWins, this.winsForNextTier, length, symbol);
    }

    /**
     * Calculates the progress towards the next tier as a percentage string.
     *
     * @return The progress as a formatted percentage string (e.g., "75%").
     */
    public String getProgressPercentage() {
        if (this.isMaxRank) return "100%";
        return this.winsForNextTier > 0 ? Math.round((float) this.currentWins / this.winsForNextTier * 100) + "%" : "100%";
    }

    /**
     * Calculates the number of additional wins required to reach the next tier.
     *
     * @return The number of additional wins required to reach the next tier.
     */
    public int getWinsRequired() {
        if (isMaxRank) return 0;
        return Math.max(0, this.winsForNextTier - this.currentWins);
    }

    /**
     * Method to determine whether to use "win" or "wins" based on the number of wins required.
     *
     * @return the appropriate string ("win" or "wins").
     */
    public String getWinOrWins() {
        return this.getWinsRequired() == 1 ? "win" : "wins";
    }
}