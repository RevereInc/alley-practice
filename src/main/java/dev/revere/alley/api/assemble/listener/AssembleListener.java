package dev.revere.alley.api.assemble.listener;

import dev.revere.alley.api.assemble.AssembleService;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@Getter
public class AssembleListener implements Listener {
    private final AssembleService assembleService;

    public AssembleListener(AssembleService assembleService) {
        this.assembleService = assembleService;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        this.assembleService.createBoard(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        this.assembleService.removeBoard(player);
    }
}