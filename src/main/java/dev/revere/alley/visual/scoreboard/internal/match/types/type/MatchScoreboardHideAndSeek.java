package dev.revere.alley.visual.scoreboard.internal.match.types.type;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.time.TimeUtil;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.feature.match.internal.types.HideAndSeekMatch;
import dev.revere.alley.feature.match.model.GameParticipant;
import dev.revere.alley.feature.match.model.internal.MatchGamePlayer;
import dev.revere.alley.visual.scoreboard.internal.match.BaseMatchScoreboard;
import dev.revere.alley.visual.scoreboard.internal.match.annotation.ScoreboardData;
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
    public List<String> getLines(Profile profile, Player player, GameParticipant<MatchGamePlayer> you, GameParticipant<MatchGamePlayer> opponent) {
        ConfigService configService = AlleyPlugin.getInstance().getService(ConfigService.class);
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
    protected String replacePlaceholders(String line, Profile profile, Player player, GameParticipant<MatchGamePlayer> you, GameParticipant<MatchGamePlayer> opponent) {
        String baseLine = super.replacePlaceholders(line, profile, player, you, opponent);

        HideAndSeekMatch match = (HideAndSeekMatch) profile.getMatch();
        GameParticipant<MatchGamePlayer> seekers = match.getParticipantA();
        GameParticipant<MatchGamePlayer> hiders = match.getParticipantB();

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