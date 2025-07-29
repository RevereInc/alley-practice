package dev.revere.alley.game.event.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.game.event.enums.EventType;
import dev.revere.alley.game.event.impl.sumo.SumoEvent;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.enums.ProfileState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
public class EventListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        if (profile.getEvent() == null) {
            return;
        }

        if (profile.getState() == ProfileState.PLAYING_EVENT) {
            profile.getEvent().handleLeave(player, true);
        }
    }

    @EventHandler
    private void onKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        if (profile.getEvent() == null) {
            return;
        }

        if (profile.getState() == ProfileState.PLAYING_EVENT) {
            profile.getEvent().handleLeave(player, true);
        }
    }

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        if (profile.getEvent() == null) return;
        if (!(profile.getEvent() instanceof SumoEvent)) return;
        if (!(profile.getEvent().getEventType() == EventType.SUMO)) return;

        Block block = player.getLocation().getBlock();
        if (block.getType() == Material.WATER || block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_WATER || block.getType() == Material.STATIONARY_LAVA) {
            profile.getEvent().handleDeath(player);
        }
    }
}