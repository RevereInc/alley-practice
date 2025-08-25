package dev.revere.alley.common;

import dev.revere.alley.AlleyPlugin;
import lombok.experimental.UtilityClass;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author Emmy
 * @project Alley
 * @date 27/03/2024 - 14:56
 */
@UtilityClass
public class TaskUtil {
    private final AlleyPlugin plugin = AlleyPlugin.getInstance();

    public void runTaskAsync(Runnable runnable) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public void runTaskLater(Runnable runnable, long delay) {
        plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delay);
    }

    public void runTaskTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer(plugin, delay, timer);
    }

    public void runTaskTimer(Runnable runnable, long delay, long timer) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, delay, timer);
    }

    public void runTask(Runnable runnable) {
        plugin.getServer().getScheduler().runTask(plugin, runnable);
    }

    public BukkitTask run(Runnable runnable) {
        return plugin.getServer().getScheduler().runTask(plugin, runnable);
    }

    public BukkitTask runAsync(Runnable runnable) {
        return plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public BukkitTask runLater(Runnable runnable, long delay) {
        return plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delay);
    }

    public BukkitTask runLaterAsync(Runnable runnable, long delay) {
        return plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
    }

    public BukkitTask runTimer(Runnable runnable, long delay, long interval) {
        return plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, delay, interval);
    }

    public BukkitTask runTimerAsync(Runnable runnable, long delay, long interval) {
        return plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, interval);
    }
}