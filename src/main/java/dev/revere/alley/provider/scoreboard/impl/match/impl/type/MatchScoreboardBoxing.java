package dev.revere.alley.provider.scoreboard.impl.match.impl.type;

import dev.revere.alley.Alley;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingBoxing;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.provider.scoreboard.impl.match.BaseMatchScoreboard;
import dev.revere.alley.provider.scoreboard.impl.match.annotation.ScoreboardData;
import dev.revere.alley.util.chat.CC;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @since 26/06/2025
 */
@ScoreboardData(kit = KitSettingBoxing.class)
public class MatchScoreboardBoxing extends BaseMatchScoreboard {
    @Override
    protected String getSoloConfigPath() {
        return "scoreboard.lines.playing.solo.boxing-match";
    }

    @Override
    protected String getTeamConfigPath() {
        return "scoreboard.lines.playing.team.boxing-match";
    }

    @Override
    protected String replacePlaceholders(String line, Profile profile, Player player, GameParticipant<MatchGamePlayerImpl> you, GameParticipant<MatchGamePlayerImpl> opponent) {
        String baseLine = super.replacePlaceholders(line, profile, player, you, opponent);
        FileConfiguration config = Alley.getInstance().getService(ConfigService.class).getScoreboardConfig();

        int playerHits = profile.getMatch().isTeamMatch() ? you.getTeamHits() : you.getLeader().getData().getHits();
        int opponentHits = profile.getMatch().isTeamMatch() ? opponent.getTeamHits() : opponent.getLeader().getData().getHits();

        int playerCombo = you.getLeader().getData().getCombo();
        int opponentCombo = opponent.getLeader().getData().getCombo();

        String hitDifference = formatHitDifference(playerHits, opponentHits, config);
        String combo = formatCombo(playerCombo, opponentCombo, config);

        return baseLine
                .replace("{player-hits}", String.valueOf(playerHits))
                .replace("{opponent-hits}", String.valueOf(opponentHits))
                .replace("{difference}", hitDifference)
                .replace("{combo}", combo);
    }

    private String formatHitDifference(int playerHits, int opponentHits, FileConfiguration config) {
        int difference = playerHits - opponentHits;
        String format;
        if (difference > 0) {
            format = config.getString("boxing-hit-difference.positive-difference", "&a(+{difference})").replace("{difference}", String.valueOf(difference));
        } else if (difference < 0) {
            format = config.getString("boxing-hit-difference.negative-difference", "&c(-{difference})").replace("{difference}", String.valueOf(Math.abs(difference)));
        } else {
            format = config.getString("boxing-hit-difference.no-difference", "&a(+0)");
        }
        return CC.translate(format);
    }

    private String formatCombo(int playerCombo, int opponentCombo, FileConfiguration config) {
        String format;
        if (playerCombo > 1) {
            format = config.getString("boxing-combo-display.positive-combo", "&a{combo} Combo").replace("{combo}", String.valueOf(playerCombo));
        } else if (opponentCombo > 1) {
            format = config.getString("boxing-combo-display.negative-combo", "&c{combo} Combo").replace("{combo}", String.valueOf(opponentCombo));
        } else {
            format = config.getString("boxing-combo-display.no-combo", "&fNo Combo");
        }
        return CC.translate(format);
    }
}