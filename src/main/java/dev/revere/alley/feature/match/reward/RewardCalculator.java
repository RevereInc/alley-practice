package dev.revere.alley.feature.match.reward;

import dev.revere.alley.core.profile.Profile;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 01/09/2025
 */
public interface RewardCalculator {
    /**
     * Calculates the reward for a player.
     *
     * @param player  The player receiving the reward.
     * @param profile The profile of the player.
     * @return The number of coins or reward points earned.
     */
    int calculateReward(Player player, Profile profile);
}