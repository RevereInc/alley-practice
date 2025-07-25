package dev.revere.alley.provider.scoreboard.impl.match.impl.type;

import dev.revere.alley.base.kit.setting.impl.mode.KitSettingStickFight;
import dev.revere.alley.game.match.impl.RoundsMatch;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.provider.scoreboard.impl.match.BaseMatchScoreboard;
import dev.revere.alley.provider.scoreboard.impl.match.annotation.ScoreboardData;
import dev.revere.alley.util.visual.ScoreboardUtil;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @since 26/06/2025
 */
@ScoreboardData(kit = KitSettingStickFight.class)
public class MatchScoreboardStickFight extends BaseMatchScoreboard {
    @Override
    protected String getSoloConfigPath() {
        return "scoreboard.lines.playing.solo.stickfight-match";
    }

    @Override
    protected String getTeamConfigPath() {
        return "scoreboard.lines.playing.team.stickfight-match";
    }

    @Override
    protected String replacePlaceholders(String line, Profile profile, Player player, GameParticipant<MatchGamePlayerImpl> you, GameParticipant<MatchGamePlayerImpl> opponent) {
        String baseLine = super.replacePlaceholders(line, profile, player, you, opponent);
        RoundsMatch roundsMatch = (RoundsMatch) profile.getMatch();

        return baseLine
                .replace("{goals}", ScoreboardUtil.visualizeGoals(roundsMatch.getParticipantA().getLeader().getData().getScore(), 5))
                .replace("{opponent-goals}", ScoreboardUtil.visualizeGoals(roundsMatch.getParticipantB().getLeader().getData().getScore(), 5))
                .replace("{current-round}", String.valueOf(roundsMatch.getCurrentRound()))
                .replace("{color}", String.valueOf(roundsMatch.getTeamAColor()))
                .replace("{opponent-color}", String.valueOf(roundsMatch.getTeamBColor()));
    }
}