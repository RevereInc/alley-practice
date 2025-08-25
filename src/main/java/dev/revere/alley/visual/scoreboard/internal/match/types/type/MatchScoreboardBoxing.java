package dev.revere.alley.visual.scoreboard.internal.match.types.type;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.feature.kit.setting.types.mode.KitSettingBoxing;
import dev.revere.alley.feature.match.model.GameParticipant;
import dev.revere.alley.feature.match.model.internal.MatchGamePlayer;
import dev.revere.alley.visual.scoreboard.internal.match.BaseMatchScoreboard;
import dev.revere.alley.visual.scoreboard.internal.match.annotation.ScoreboardData;
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
    protected String replacePlaceholders(String line, Profile profile, Player player, GameParticipant<MatchGamePlayer> you, GameParticipant<MatchGamePlayer> opponent) {
        String baseLine = super.replacePlaceholders(line, profile, player, you, opponent);
        FileConfiguration config = AlleyPlugin.getInstance().getService(ConfigService.class).getScoreboardConfig();

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

    /**
     * Formats the hit difference between the player and the opponent.
     *
     * @param playerHits   The number of hits by the player.
     * @param opponentHits The number of hits by the opponent.
     * @param config       The configuration file to get formatting options.
     * @return The formatted hit difference string.
     */
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

    /**
     * Formats the combo display based on the player's and opponent's combos.
     *
     * @param playerCombo   The player's current combo count.
     * @param opponentCombo The opponent's current combo count.
     * @param config        The configuration file to get formatting options.
     * @return The formatted combo string.
     */
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