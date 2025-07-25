package dev.revere.alley.api.server.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;

/**
 * @author Emmy
 * @project Alley
 * @date 15/09/2024 - 19:23
 */
public class ServerEnvironmentListener implements Listener {

    @EventHandler
    private void onUnlockChunk(ChunkUnloadEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onPrime(ExplosionPrimeEvent event) {
        // event.setCancelled(true);
    }

    @EventHandler
    private void onBlockBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onBlockSpread(BlockSpreadEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onLeavesDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getCause() == BlockIgniteEvent.IgniteCause.LIGHTNING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onCreatureSpawn(CreatureSpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onWorldLoad(WorldLoadEvent event) {
        event.getWorld().getEntities().clear();
        event.getWorld().setDifficulty(Difficulty.HARD);
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        if (event.getChangedType() == Material.GRASS && event.getBlock().getType() == Material.DIRT) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWorldTeleport(PlayerTeleportEvent event) {
        if (event.getFrom().getWorld() != event.getTo().getWorld()) {
            Player player = event.getPlayer();
            Profile profile = Alley.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
            profile.getProfileData().getSettingData().setTimeBasedOnProfileSetting(player);
        }
    }
}