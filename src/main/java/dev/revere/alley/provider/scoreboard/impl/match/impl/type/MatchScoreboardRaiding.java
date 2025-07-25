package dev.revere.alley.provider.scoreboard.impl.match.impl.type;

import dev.revere.alley.base.kit.setting.impl.mode.KitSettingRaiding;
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
@ScoreboardData(kit = KitSettingRaiding.class)
public class MatchScoreboardRaiding extends BaseMatchScoreboard {
    @Override
    protected String getSoloConfigPath() {
        return "scoreboard.lines.playing.solo.raiding-match";
    }

    @Override
    protected String getTeamConfigPath() {
        return "scoreboard.lines.playing.team.raiding-match";
    }

    @Override
    protected String replacePlaceholders(String line, Profile profile, Player player, GameParticipant<MatchGamePlayerImpl> you, GameParticipant<MatchGamePlayerImpl> opponent) {
        String baseLine = super.replacePlaceholders(line, profile, player, you, opponent);

        return baseLine
                .replace("{role}", you.getLeader().getData().getRole().getDisplayName());
    }
}
