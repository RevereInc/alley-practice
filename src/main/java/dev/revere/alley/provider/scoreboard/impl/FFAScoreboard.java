package dev.revere.alley.provider.scoreboard.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.base.combat.CombatService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.provider.scoreboard.IScoreboard;
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
public class FFAScoreboard implements IScoreboard {
    protected final Alley plugin;

    /**
     * Constructor for the FFAScoreboard class.
     *
     * @param plugin The Alley plugin instance.
     */
    public FFAScoreboard(Alley plugin) {
        this.plugin = plugin;
    }

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
        List<String> scoreboardLines = new ArrayList<>();

        CombatService combatService = this.plugin.getCombatService();
        List<String> ffaLines = this.plugin.getConfigService().getScoreboardConfig().getStringList("scoreboard.lines.ffa");
        List<String> combatTagLines = this.plugin.getConfigService().getScoreboardConfig().getStringList("ffa-combat-tag");

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
                        .replaceAll("\\{zone}", this.plugin.getFfaSpawnService().getCuboid().isIn(player) ? "Spawn" : "Warzone")
                        .replaceAll("\\{kills}", String.valueOf(profile.getProfileData().getFfaData().get(profile.getFfaMatch().getKit().getName()).getKills()))
                        .replaceAll("\\{deaths}", String.valueOf(profile.getProfileData().getFfaData().get(profile.getFfaMatch().getKit().getName()).getDeaths()))
                        .replaceAll("\\{ping}", String.valueOf(this.getPing(player))));
            }
        }

        return scoreboardLines;
    }
}