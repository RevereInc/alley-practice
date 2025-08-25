package dev.revere.alley.feature.match.task.other;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.feature.cooldown.Cooldown;
import dev.revere.alley.feature.cooldown.CooldownService;
import dev.revere.alley.feature.cooldown.CooldownType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

/**
 * @author Remi
 * @project alley-practice
 * @date 26/06/2025
 */
public class MatchPearlCooldownTask extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Profile profile = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());

            if (profile.getState() == ProfileState.PLAYING || profile.getState() == ProfileState.FFA) {
                CooldownService cooldownService = AlleyPlugin.getInstance().getService(CooldownService.class);
                Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownService.getCooldown(player.getUniqueId(), CooldownType.ENDER_PEARL));

                if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {

                    Cooldown cooldown = optionalCooldown.get();

                    long remainingMillis = cooldown.remainingTimeMillis();
                    long totalDuration = cooldown.getType().getCooldownDuration();
                    int remainingTime = optionalCooldown.get().remainingTime();

                    player.setLevel(remainingTime);

                    if (totalDuration > 0) {
                        player.setExp((float) remainingMillis / totalDuration);
                    }
                }
            } else {
                if (player.getLevel() > 0) {
                    player.setLevel(0);
                }

                if (player.getExp() > 0.0F) {
                    player.setExp(0.0F);
                }
            }
        }
    }
}
