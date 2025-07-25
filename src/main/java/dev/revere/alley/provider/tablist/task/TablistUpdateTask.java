package dev.revere.alley.provider.tablist.task;

import dev.revere.alley.Alley;
import dev.revere.alley.provider.tablist.Tablist;
import dev.revere.alley.provider.tablist.TablistImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Emmy
 * @project Alley
 * @date 07/09/2024 - 15:23
 */
public class TablistUpdateTask extends BukkitRunnable {
    protected final Tablist tablistVisualizer;

    public TablistUpdateTask() {
        this.tablistVisualizer = new TablistImpl(Alley.getInstance());
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.tablistVisualizer.update(player);
        }
    }
}