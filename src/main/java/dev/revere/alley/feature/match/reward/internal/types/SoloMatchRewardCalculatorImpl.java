package dev.revere.alley.feature.match.reward.internal.types;

import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.feature.match.model.MatchGamePlayerData;
import dev.revere.alley.feature.match.reward.RewardCalculator;
import dev.revere.alley.feature.match.reward.RewardType;
import dev.revere.alley.feature.match.reward.annotation.RewardTypeProvider;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 01/09/2025
 */
@RewardTypeProvider(type = RewardType.SOLO_MATCH)
public class SoloMatchRewardCalculatorImpl implements RewardCalculator {
    @Override
    public int calculateReward(Player player, Profile profile) {

        //TODO: Revise reward calculation logic later on to include more factors

        MatchGamePlayerData data = profile.getMatch().getGamePlayer(player).getData();
        int kills = data.getKills();
        int deaths = data.getDeaths();
        int missedPotions = data.getMissedPotions();

        double score = kills * 15 - deaths * 10;
        int excessPotions = Math.max(missedPotions - 20, 0);
        score -= excessPotions * 1.5;

        return (int) Math.max(0, Math.min(100, score));
    }
}