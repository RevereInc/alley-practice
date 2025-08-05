package dev.revere.alley.feature.spawn.task;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.common.text.Symbol;
import dev.revere.alley.feature.spawn.SpawnService;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

/**
 * @author Emmy
 * @project alley-practice
 * @since 05/08/2025
 */
public class SpawnReminderTask extends BukkitRunnable {
    @Override
    public void run() {
        if (AlleyPlugin.getInstance().getService(SpawnService.class).getLocation() != null) {
            this.cancel();
            return;
        }

        Arrays.asList(
                "",
                "&fServer Spawn: &c" + Symbol.CROSS,
                "&fPlease use the &6/setspawn &fto set your spawn location.",
                ""
        ).forEach(line -> Bukkit.broadcastMessage(CC.translate(line)));
    }
}
