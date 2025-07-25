package dev.revere.alley.provider.scoreboard.impl.match.impl.type;

import dev.revere.alley.game.match.impl.LivesMatch;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.provider.scoreboard.impl.match.BaseMatchScoreboard;
import dev.revere.alley.provider.scoreboard.impl.match.annotation.ScoreboardData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @since 26/06/2025
 */
@ScoreboardData(match = LivesMatch.class)
public class MatchScoreboardLives extends BaseMatchScoreboard {
    @Override
    protected String getSoloConfigPath() {
        return "scoreboard.lines.playing.solo.lives-match";
    }

    @Override
    protected String getTeamConfigPath() {
        return "scoreboard.lines.playing.team.lives-match";
    }

    @Override
    protected String replacePlaceholders(String line, Profile profile, Player player, GameParticipant<MatchGamePlayerImpl> you, GameParticipant<MatchGamePlayerImpl> opponent) {
        String baseLine = super.replacePlaceholders(line, profile, player, you, opponent);

        int yourTeamLives = you.getPlayers().stream()
                .mapToInt(p -> p.getData().getLives())
                .sum();

        int opponentTeamLives = opponent.getPlayers().stream()
                .mapToInt(p -> p.getData().getLives())
                .sum();

        return baseLine
                .replace("{player-lives}", String.valueOf(you.getLeader().getData().getLives()))
                .replace("{opponent-lives}", String.valueOf(opponent.getLeader().getData().getLives()))
                .replace("{your-team-lives}", String.valueOf(yourTeamLives))
                .replace("{opponent-team-lives}", String.valueOf(opponentTeamLives));
    }
}