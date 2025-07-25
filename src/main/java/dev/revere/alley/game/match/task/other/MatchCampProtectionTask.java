package dev.revere.alley.game.match.task.other;

import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.impl.StandAloneArena;
import dev.revere.alley.game.match.Match;
import dev.revere.alley.game.match.enums.MatchState;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.reflection.ReflectionRepository;
import dev.revere.alley.tool.reflection.impl.TitleReflectionServiceImpl;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Remi
 * @project alley-practice
 * @date 22/07/2025
 */
public class MatchCampProtectionTask extends BukkitRunnable {
    private final TitleReflectionServiceImpl titleReflectionServiceImpl = Alley.getInstance().getService(ReflectionRepository.class).getReflectionService(TitleReflectionServiceImpl.class);

    private final Player player;
    private int ticks;

    private static final int INITIAL_GRACE_PERIOD_SECONDS = 3;
    private static final int COUNTDOWN_DURATION_SECONDS = 3;

    /**
     * Constructor for the MatchCampProtectionTask class.
     *
     * @param player The player to apply camp protection to.
     */
    public MatchCampProtectionTask(Player player) {
        this.player = player;
        this.ticks = 0;
    }

    @Override
    public void run() {
        if (this.player == null || !this.player.isOnline()) {
            this.cancel();
            return;
        }

        Profile profile = Alley.getInstance().getService(ProfileService.class).getProfile(this.player.getUniqueId());
        Match match = profile.getMatch();
        if (match == null) {
            this.cancel();
            return;
        }

        if (match.getState() == MatchState.ENDING_MATCH) {
            this.cancel();
            return;
        }

        StandAloneArena matchArena = (StandAloneArena) match.getArena();
        int CAMP_Y_LEVEL = matchArena.getHeightLimit();

        MatchGamePlayerImpl gamePlayer = match.getGamePlayer(player);
        if (this.player.getLocation().getY() <= CAMP_Y_LEVEL + 3
                || gamePlayer.isDead()
                || gamePlayer.isEliminated()
                || this.player.getGameMode() == GameMode.CREATIVE
                || this.player.getGameMode() == GameMode.SPECTATOR) {
            ticks = 0;
            return;
        }

        this.ticks++;
        int damageStartPeriod = INITIAL_GRACE_PERIOD_SECONDS + COUNTDOWN_DURATION_SECONDS;

        if (ticks <= damageStartPeriod) {
            int countdownValue = damageStartPeriod - ticks + 1;

            this.titleReflectionServiceImpl.sendTitle(
                    this.player,
                    "&cCAMP PROTECTION",
                    "&fYou will take damage in " + countdownValue + " seconds!"
            );
        } else {
            this.player.damage(4.0);
            this.titleReflectionServiceImpl.sendTitle(
                    this.player,
                    "&cTAKING DAMAGE!",
                    "&fMove down!"
            );
        }
    }
}
