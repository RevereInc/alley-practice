package dev.revere.alley.provider.scoreboard.impl.match.impl.type;

import dev.revere.alley.game.match.impl.RoundsMatch;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.provider.scoreboard.impl.match.BaseMatchScoreboard;
import dev.revere.alley.provider.scoreboard.impl.match.annotation.ScoreboardData;
import dev.revere.alley.util.TimeUtil;
import dev.revere.alley.util.visual.ScoreboardUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Remi
 * @project Alley
 * @since 26/06/2025
 */
@ScoreboardData(match = RoundsMatch.class)
public class MatchScoreboardRounds extends BaseMatchScoreboard {
    @Override
    protected String getSoloConfigPath() {
        return "scoreboard.lines.playing.solo.rounds-match";
    }

    @Override
    protected String getTeamConfigPath() {
        return "scoreboard.lines.playing.team.rounds-match";
    }

    @Override
    protected String replacePlaceholders(String line, Profile profile, Player player, GameParticipant<MatchGamePlayerImpl> you, GameParticipant<MatchGamePlayerImpl> opponent) {
        String baseLine = super.replacePlaceholders(line, profile, player, you, opponent);
        RoundsMatch roundsMatch = (RoundsMatch) profile.getMatch();

        return baseLine
                .replace("{time-left}", getFormattedTime(profile))
                .replace("{goals}", ScoreboardUtil.visualizeGoals(roundsMatch.getParticipantA().getLeader().getData().getScore(), 3))
                .replace("{opponent-goals}", ScoreboardUtil.visualizeGoals(roundsMatch.getParticipantB().getLeader().getData().getScore(), 3))
                .replace("{kills}", String.valueOf(profile.getMatch().getGamePlayer(player).getData().getKills()))
                .replace("{current-round}", String.valueOf(roundsMatch.getCurrentRound()))
                .replace("{color}", String.valueOf(roundsMatch.getTeamAColor()))
                .replace("{opponent-color}", String.valueOf(roundsMatch.getTeamBColor()));
    }

    private @NotNull String getFormattedTime(Profile profile) {
        long elapsedTime = System.currentTimeMillis() - profile.getMatch().getStartTime();
        long remainingTime = Math.max(900_000 - elapsedTime, 0);
        return TimeUtil.millisToFourDigitSecondsTimer(remainingTime);
    }
}