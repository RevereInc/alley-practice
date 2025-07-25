package dev.revere.alley.provider.scoreboard.impl.match.impl.type;

import dev.revere.alley.Alley;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.game.match.impl.HideAndSeekMatch;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.provider.scoreboard.impl.match.BaseMatchScoreboard;
import dev.revere.alley.provider.scoreboard.impl.match.annotation.ScoreboardData;
import dev.revere.alley.util.TimeUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project alley-practice
 * @date 22/07/2025
 */
@ScoreboardData(match = HideAndSeekMatch.class)
public class MatchScoreboardHideAndSeek extends BaseMatchScoreboard {
    @Override
    protected String getSoloConfigPath() {
        return "scoreboard.lines.playing.solo.hideandseek-match";
    }

    @Override
    protected String getTeamConfigPath() {
        return "scoreboard.lines.playing.team.hideandseek-match";
    }

    @Override
    public List<String> getLines(Profile profile, Player player, GameParticipant<MatchGamePlayerImpl> you, GameParticipant<MatchGamePlayerImpl> opponent) {
        ConfigService configService = Alley.getInstance().getService(ConfigService.class);
        HideAndSeekMatch match = (HideAndSeekMatch) profile.getMatch();

        String configPath = match.isTeamMatch() ? getTeamConfigPath() : getSoloConfigPath();
        boolean isHidingPhase = match.getGameEndTask() == null;
        if (isHidingPhase) {
            configPath = configPath + ".hiding";
        } else {
            configPath = configPath + ".seeking";
        }

        List<String> template = configService.getScoreboardConfig().getStringList(configPath);
        List<String> scoreboardLines = new ArrayList<>();

        for (String line : template) {
            scoreboardLines.add(replacePlaceholders(line, profile, player, you, opponent));
        }

        return scoreboardLines;
    }

    @Override
    protected String replacePlaceholders(String line, Profile profile, Player player, GameParticipant<MatchGamePlayerImpl> you, GameParticipant<MatchGamePlayerImpl> opponent) {
        String baseLine = super.replacePlaceholders(line, profile, player, you, opponent);

        HideAndSeekMatch match = (HideAndSeekMatch) profile.getMatch();
        GameParticipant<MatchGamePlayerImpl> seekers = match.getParticipantA();
        GameParticipant<MatchGamePlayerImpl> hiders = match.getParticipantB();

        long hidersAlive = hiders.getPlayers().stream().filter(p -> !p.isDead()).count();
        long seekersAlive = seekers.getPlayers().stream().filter(p -> !p.isDead()).count();
        String playerRole = seekers.containsPlayer(player.getUniqueId()) ? "&cSeeker" : "&aHider";

        String timeLeft;
        long totalElapsedSeconds = match.getElapsedTime() / 1000;
        boolean isHidingPhase = match.getGameEndTask() == null;

        if (isHidingPhase) {
            long remaining = Math.max(0, match.getHidingTimeSeconds() - totalElapsedSeconds);
            timeLeft = TimeUtil.formatTimeFromSeconds((int) remaining);
        } else {
            long elapsedInSeekingPhase = totalElapsedSeconds - match.getHidingTimeSeconds();
            long remaining = Math.max(0, match.getGameTimeSeconds() - elapsedInSeekingPhase);
            timeLeft = TimeUtil.formatTimeFromSeconds((int) remaining);
        }

        return baseLine
                .replace("{player-role}", playerRole)
                .replace("{time-left}", timeLeft)
                .replace("{hiders-alive}", String.valueOf(hidersAlive))
                .replace("{hiders-total}", String.valueOf(hiders.getPlayerSize()))
                .replace("{seekers-alive}", String.valueOf(seekersAlive))
                .replace("{seekers-total}", String.valueOf(seekers.getPlayerSize()));
    }
}
