package dev.revere.alley.visual.tab.task;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.visual.tab.TabAdapter;
import dev.revere.alley.visual.tab.internal.TabAdapterImpl;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Emmy
 * @project Alley
 * @date 07/09/2024 - 15:23
 */
public class TabUpdateTask extends BukkitRunnable {
    private final AlleyPlugin plugin;
    protected final TabAdapter tabAdapter;

    /**
     * Constructor for the TabUpdateTask class.
     *
     * @param plugin The main plugin instance.
     */
    public TabUpdateTask(AlleyPlugin plugin) {
        this.plugin = plugin;
        this.tabAdapter = new TabAdapterImpl(AlleyPlugin.getInstance());
    }

    @Override
    public void run() {
        this.plugin.getServer().getOnlinePlayers().forEach(this.tabAdapter::update);
    }
}