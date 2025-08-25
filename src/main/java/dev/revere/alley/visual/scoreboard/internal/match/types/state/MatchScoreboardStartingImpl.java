package dev.revere.alley.visual.scoreboard.internal.match.types.state;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.feature.match.model.GameParticipant;
import dev.revere.alley.feature.match.model.internal.MatchGamePlayer;
import dev.revere.alley.visual.scoreboard.internal.match.MatchScoreboard;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
public class MatchScoreboardStartingImpl implements MatchScoreboard {
    @Override
    public List<String> getLines(Profile profile, Player player, GameParticipant<MatchGamePlayer> you, GameParticipant<MatchGamePlayer> opponent) {
        ConfigService configService = AlleyPlugin.getInstance().getService(ConfigService.class);
        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);

        List<String> scoreboardLines = new ArrayList<>();
        List<String> template = configService.getScoreboardConfig().getStringList("scoreboard.lines.starting");

        for (String line : template) {
            scoreboardLines.add(CC.translate(line)
                    .replace("{opponent}", this.getColoredName(profileService.getProfile(opponent.getLeader().getUuid())))
                    .replace("{opponent-ping}", String.valueOf(this.getPing(opponent.getLeader().getTeamPlayer())))
                    .replace("{player-ping}", String.valueOf(this.getPing(player)))
                    .replace("{duration}", profile.getMatch().getDuration())
                    .replace("{arena}", profile.getMatch().getArena().getDisplayName() == null ? "&c&lNULL" : profile.getMatch().getArena().getDisplayName())
                    .replace("{dot-animation}", this.getDotAnimation().getCurrentFrame())
                    .replace("{countdown}", String.valueOf(profile.getMatch().getRunnable().getStage()))
                    .replace("{kit}", profile.getMatch().getKit().getDisplayName()));
        }

        return scoreboardLines;
    }
}
