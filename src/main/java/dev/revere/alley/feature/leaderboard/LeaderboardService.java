package dev.revere.alley.feature.leaderboard;

import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.plugin.lifecycle.Service;
import dev.revere.alley.feature.leaderboard.data.LeaderboardPlayerData;
import dev.revere.alley.feature.leaderboard.enums.LeaderboardType;

import java.util.List;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface LeaderboardService extends Service {
    /**
     * Gets the sorted leaderboard data for a specific kit and type.
     * <p>
     * This method performs a live, in-memory refresh for all online players
     * before returning the data, ensuring it is always up-to-date.
     *
     * @param kit  The kit to get the leaderboard for.
     * @param type The type of leaderboard to retrieve.
     * @return A sorted list of LeaderboardPlayerData.
     */
    List<LeaderboardPlayerData> getLeaderboardEntries(Kit kit, LeaderboardType type);

    /**
     * Triggers a full, deep recalculation of all leaderboards from the database.
     * This is a heavy operation and should only be used for a manual refresh command.
     */
    void forceRecalculateAll();
}