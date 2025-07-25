package dev.revere.alley.provider.scoreboard.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.base.combat.CombatService;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.game.ffa.cuboid.FFASpawnService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.data.impl.ProfileFFAData;
import dev.revere.alley.provider.scoreboard.Scoreboard;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
public class FFAScoreboardImpl implements Scoreboard {
    @Override
    public List<String> getLines(Profile profile) {
        return Collections.emptyList();
    }

    /**
     * Get the title of the scoreboard.
     *
     * @param profile The profile to get the title for.
     * @param player  The player to get the title for.
     * @return The title of the scoreboard.
     */
    @Override
    public List<String> getLines(Profile profile, Player player) {
        ProfileFFAData profileFFAData = profile.getProfileData().getFfaData().get(profile.getFfaMatch().getKit().getName());

        FFASpawnService ffaSpawnService = Alley.getInstance().getService(FFASpawnService.class);
        ConfigService configService = Alley.getInstance().getService(ConfigService.class);
        CombatService combatService = Alley.getInstance().getService(CombatService.class);

        List<String> scoreboardLines = new ArrayList<>();

        List<String> ffaLines = configService.getScoreboardConfig().getStringList("scoreboard.lines.ffa");
        List<String> combatTagLines = configService.getScoreboardConfig().getStringList("ffa-combat-tag");

        for (String line : ffaLines) {
            if (line.contains("{player-combat}")) {
                if (combatService.isPlayerInCombat(player.getUniqueId())) {
                    for (String combatLine : combatTagLines) {
                        scoreboardLines.add(CC.translate(combatLine
                                .replaceAll("\\{combat-tag}", combatService.getRemainingTimeFormatted(player))));
                    }
                }
            } else {
                scoreboardLines.add(CC.translate(line)
                        .replaceAll("\\{kit}", profile.getFfaMatch().getKit().getDisplayName())
                        .replaceAll("\\{players}", String.valueOf(profile.getFfaMatch().getPlayers().size()))
                        .replaceAll("\\{zone}", ffaSpawnService.getCuboid().isIn(player) ? "Spawn" : "Warzone")
                        .replaceAll("\\{ks}", String.valueOf(profileFFAData.getKillstreak()))
                        .replaceAll("\\{kills}", String.valueOf(profileFFAData.getKills()))
                        .replaceAll("\\{deaths}", String.valueOf(profileFFAData.getDeaths()))
                        .replaceAll("\\{ping}", String.valueOf(this.getPing(player))));
            }
        }

        return scoreboardLines;
    }
}