package dev.revere.alley.provider.scoreboard.impl.match.impl.state;

import dev.revere.alley.Alley;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.game.match.Match;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.provider.scoreboard.impl.match.MatchScoreboard;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
public class MatchScoreboardEndingImpl implements MatchScoreboard {
    @Override
    public List<String> getLines(Profile profile, Player player, GameParticipant<MatchGamePlayerImpl> you, GameParticipant<MatchGamePlayerImpl> opponent) {
        ConfigService configService = Alley.getInstance().getService(ConfigService.class);

        List<String> scoreboardLines = new ArrayList<>();
        Match match = profile.getMatch();
        if (match == null) return scoreboardLines;

        for (String line : configService.getScoreboardConfig().getStringList("scoreboard.lines.ending")) {
            scoreboardLines.add(CC.translate(line)
                    .replace("{opponent}", opponent.getLeader().getUsername())
                    .replace("{duration}", match.getDuration())
                    .replace("{winner}", opponent.getLeader().isDead() ? you.getLeader().getUsername() : opponent.getLeader().getUsername())
                    .replace("{end-result}", opponent.getLeader().isDead() ? "&a&lVICTORY!" : "&c&lDEFEAT!"));
        }

        return scoreboardLines;
    }
}