package dev.revere.alley.feature.match.task.other;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.reflect.ReflectionService;
import dev.revere.alley.common.reflect.internal.types.TitleReflectionServiceImpl;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.locale.internal.impl.VisualsLocaleImpl;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.feature.arena.internal.types.StandAloneArena;
import dev.revere.alley.feature.match.Match;
import dev.revere.alley.feature.match.MatchState;
import dev.revere.alley.feature.match.model.internal.MatchGamePlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Remi
 * @project alley-practice
 * @date 22/07/2025
 */
public class MatchCampProtectionTask extends BukkitRunnable {
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

        Profile profile = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(this.player.getUniqueId());
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

        MatchGamePlayer gamePlayer = match.getGamePlayer(player);
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

        TitleReflectionServiceImpl titleReflectionService = AlleyPlugin.getInstance().getService(ReflectionService.class).getReflectionService(TitleReflectionServiceImpl.class);
        LocaleService localeService = AlleyPlugin.getInstance().getService(LocaleService.class);


        if (ticks <= damageStartPeriod) {
            int countdownValue = damageStartPeriod - ticks + 1;

            String header = localeService.getMessage(VisualsLocaleImpl.TITLE_CAMP_PROTECTION_HEADER);
            String footer = localeService.getMessage(VisualsLocaleImpl.TITLE_CAMP_PROTECTION_FOOTER).replace("{seconds}", String.valueOf(countdownValue));

            int fadeIn = localeService.getInt(VisualsLocaleImpl.TITLE_CAMP_PROTECTION_FADE_IN);
            int stay = localeService.getInt(VisualsLocaleImpl.TITLE_CAMP_PROTECTION_STAY);
            int fadeOut = localeService.getInt(VisualsLocaleImpl.TITLE_CAMP_PROTECTION_FADEOUT);

            titleReflectionService.sendTitle(this.player, header, footer, fadeIn, stay, fadeOut);
        } else {
            this.player.damage(4.0);

            String header = localeService.getMessage(VisualsLocaleImpl.TITLE_CAMP_PROTECTION_TAKING_DAMAGE_HEADER);
            String footer = localeService.getMessage(VisualsLocaleImpl.TITLE_CAMP_PROTECTION_TAKING_DAMAGE_FOOTER);

            int fadeIn = localeService.getInt(VisualsLocaleImpl.TITLE_CAMP_PROTECTION_TAKING_DAMAGE_FADE_IN);
            int stay = localeService.getInt(VisualsLocaleImpl.TITLE_CAMP_PROTECTION_TAKING_DAMAGE_STAY);
            int fadeOut = localeService.getInt(VisualsLocaleImpl.TITLE_CAMP_PROTECTION_TAKING_DAMAGE_FADEOUT);

            titleReflectionService.sendTitle(this.player, header, footer, fadeIn, stay, fadeOut);
        }
    }
}
