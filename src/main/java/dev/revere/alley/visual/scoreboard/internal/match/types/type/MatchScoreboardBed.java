package dev.revere.alley.visual.scoreboard.internal.match.types.type;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.feature.match.internal.types.BedMatch;
import dev.revere.alley.feature.match.internal.types.DefaultMatch;
import dev.revere.alley.feature.match.model.GameParticipant;
import dev.revere.alley.feature.match.model.internal.MatchGamePlayer;
import dev.revere.alley.visual.scoreboard.internal.match.BaseMatchScoreboard;
import dev.revere.alley.visual.scoreboard.internal.match.annotation.ScoreboardData;
import dev.revere.alley.visual.scoreboard.utility.ScoreboardUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @since 26/06/2025
 */
@ScoreboardData(match = BedMatch.class)
public class MatchScoreboardBed extends BaseMatchScoreboard {
    @Override
    protected String getSoloConfigPath() {
        return "scoreboard.lines.playing.solo.bed-match";
    }

    @Override
    protected String getTeamConfigPath() {
        return "scoreboard.lines.playing.team.bed-match";
    }

    @Override
    protected String replacePlaceholders(String line, Profile profile, Player player, GameParticipant<MatchGamePlayer> you, GameParticipant<MatchGamePlayer> opponent) {
        String baseLine = super.replacePlaceholders(line, profile, player, you, opponent);
        DefaultMatch match = (DefaultMatch) profile.getMatch();

        String yourColorDisplay = getTeamDisplay(match.getTeamColor(you));
        String opponentColorDisplay = getTeamDisplay(match.getTeamColor(opponent));

        return baseLine
                .replace("{your-bed}", ScoreboardUtil.visualizeBed(you.isBedBroken()))
                .replace("{opponent-bed}", ScoreboardUtil.visualizeBed(opponent.isBedBroken()))
                .replace("{your-color-display}", yourColorDisplay)
                .replace("{opponent-color-display}", opponentColorDisplay);
    }

    /**
     * Gets the team display name based on the team color.
     *
     * @param teamColor The team color.
     * @return The team display name.
     */
    private String getTeamDisplay(ChatColor teamColor) {
        if (teamColor == null) return "";
        String colorName = teamColor.name().toLowerCase();
        return AlleyPlugin.getInstance().getService(ConfigService.class).getScoreboardConfig().getString("scoreboard.team-displays." + colorName, "");
    }
}
