package dev.revere.alley.feature.match.task.other;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.reflect.ReflectionService;
import dev.revere.alley.common.reflect.internal.types.TitleReflectionServiceImpl;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.match.Match;
import dev.revere.alley.feature.match.MatchState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Emmy
 * @project Alley
 * @since 23/05/2025
 */
public class MatchRespawnTask extends BukkitRunnable {
    protected final Player player;
    protected final Match match;
    private int count;

    /**
     * Constructor for the MatchRespawnTask class.
     *
     * @param player The player to respawn.
     * @param match  The match instance.
     * @param count  The countdown time in seconds.
     */
    public MatchRespawnTask(Player player, Match match, int count) {
        this.player = player;
        this.match = match;
        this.count = count;
    }

    @Override
    public void run() {
        if (this.count == 0) {
            this.cancel();
            this.match.handleRespawn(this.player);
            return;
        }

        if (this.match.getState() == MatchState.ENDING_MATCH || this.match.getState() == MatchState.ENDING_ROUND) {
            this.cancel();
            return;
        }

        AlleyPlugin.getInstance().getService(ReflectionService.class).getReflectionService(TitleReflectionServiceImpl.class).sendTitle(
                player,
                "&6&lRespawn",
                "&fRespawning in &6" + this.count + "s",
                0, 23, 20
        );

        this.player.sendMessage(CC.translate("&a" + this.count + "..."));
        this.count--;
    }
}