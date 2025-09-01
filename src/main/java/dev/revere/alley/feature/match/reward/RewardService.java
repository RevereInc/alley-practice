package dev.revere.alley.feature.match.reward;

import dev.revere.alley.bootstrap.lifecycle.Service;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @author Emmy
 * @project alley-practice
 * @since 01/09/2025
 */
public interface RewardService extends Service {
    /**
     * Issues a reward to a player based on the specified reward type.
     *
     * @param player The player receiving the reward.
     * @param type   The type of reward to calculate.
     */
    void issueReward(Player player, RewardType type);

    /**
     * Gets the map of reward calculators.
     *
     * @return The map of reward calculators.
     */
    Map<RewardType, RewardCalculator> getCalculators();
}