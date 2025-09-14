package dev.revere.alley.feature.match.task.other;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.reflect.ReflectionService;
import dev.revere.alley.common.reflect.internal.types.TitleReflectionServiceImpl;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.locale.internal.impl.GameLocaleImpl;
import dev.revere.alley.core.locale.internal.impl.VisualLocaleImpl;
import dev.revere.alley.feature.match.Match;
import dev.revere.alley.feature.match.MatchState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

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

        LocaleService localeService = AlleyPlugin.getInstance().getService(LocaleService.class);

        String header = localeService.getMessage(VisualLocaleImpl.TITLE_MATCH_RESPAWNING_HEADER).replace("{seconds}", String.valueOf(this.count));
        String footer = localeService.getMessage(VisualLocaleImpl.TITLE_MATCH_RESPAWNING_FOOTER).replace("{seconds}", String.valueOf(this.count));
        int fadeIn = localeService.getInt(VisualLocaleImpl.TITLE_MATCH_RESPAWNING_FADE_IN);
        int stay = localeService.getInt(VisualLocaleImpl.TITLE_MATCH_RESPAWNING_STAY);
        int fadeOut = localeService.getInt(VisualLocaleImpl.TITLE_MATCH_RESPAWNING_FADEOUT);

        AlleyPlugin.getInstance().getService(ReflectionService.class).getReflectionService(TitleReflectionServiceImpl.class).sendTitle(
                player,
                header,
                footer,
                fadeIn, stay, fadeOut
        );

        boolean messageEnabled = localeService.getBoolean(GameLocaleImpl.MATCH_RESPAWNING_MESSAGE_ENABLED_BOOLEAN);
        List<String> messageFormat = localeService.getMessageList(GameLocaleImpl.MATCH_RESPAWNING_MESSAGE_FORMAT);
        if (messageEnabled) {
            messageFormat.forEach(message -> this.player.sendMessage(CC.translate(message.replace("{seconds}", String.valueOf(this.count)))));
        }

        this.count--;
    }
}