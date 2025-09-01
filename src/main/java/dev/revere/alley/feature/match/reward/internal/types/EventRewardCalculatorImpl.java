package dev.revere.alley.feature.match.reward.internal.types;

import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.feature.match.reward.RewardCalculator;
import dev.revere.alley.feature.match.reward.RewardType;
import dev.revere.alley.feature.match.reward.annotation.RewardTypeProvider;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 01/09/2025
 */
@RewardTypeProvider(type = RewardType.EVENT_WIN)
public class EventRewardCalculatorImpl implements RewardCalculator {
    @Override
    public int calculateReward(Player player, Profile profile) {

        //TODO: Implement event win reward logic

        return 0;
    }
}