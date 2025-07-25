package dev.revere.alley.base.spawn.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.ProfileState;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 16:20
 */
public class SpawnListener implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState().equals(ProfileState.LOBBY) || profile.getState().equals(ProfileState.EDITING) || profile.getState().equals(ProfileState.WAITING)) {
            if (player.getGameMode() == GameMode.CREATIVE) {
                return;
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (player.getGameMode() == GameMode.SURVIVAL
                && (profile.getState().equals(ProfileState.LOBBY)
                || profile.getState().equals(ProfileState.EDITING)
                || profile.getState().equals(ProfileState.WAITING))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemPickUp(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (player.getGameMode() == GameMode.SURVIVAL
                && (profile.getState().equals(ProfileState.LOBBY)
                || profile.getState().equals(ProfileState.EDITING)
                || profile.getState().equals(ProfileState.WAITING))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getState().equals(ProfileState.LOBBY) || profile.getState().equals(ProfileState.EDITING) || profile.getState().equals(ProfileState.WAITING)) {
            if (player.getGameMode() == GameMode.CREATIVE) {
                return;
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onHunger(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
            Profile profile = profileService.getProfile(player.getUniqueId());

            if (profile.getState().equals(ProfileState.LOBBY)
                    || profile.getState().equals(ProfileState.EDITING)
                    || profile.getState().equals(ProfileState.WAITING)
                    || profile.getState().equals(ProfileState.SPECTATING)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onMoveItem(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
            Profile profile = profileService.getProfile(player.getUniqueId());

            if (profile.getState() == ProfileState.EDITING) {
                return;
            }

            if (player.getGameMode() == GameMode.SURVIVAL
                    && (profile.getState().equals(ProfileState.LOBBY)
                    || profile.getState().equals(ProfileState.WAITING))) {
                if (event.getClickedInventory() != null && event.getClickedInventory().equals(player.getInventory())) {
                    event.setCancelled(true);
                }

                if (event.getSlotType() == InventoryType.SlotType.CRAFTING || event.isShiftClick() || event.getClick().isKeyboardClick()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemDamageEvent(PlayerItemDamageEvent event) {
        Profile profile = Alley.getInstance().getService(ProfileService.class).getProfile(event.getPlayer().getUniqueId());

        if (profile.getState().equals(ProfileState.LOBBY)
                || profile.getState().equals(ProfileState.EDITING)
                || profile.getState().equals(ProfileState.WAITING)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onHangingPlace(HangingPlaceEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState().equals(ProfileState.LOBBY) || profile.getState().equals(ProfileState.EDITING) || profile.getState().equals(ProfileState.WAITING)) {
            if (player.getGameMode() == GameMode.CREATIVE) {
                return;
            }
            event.setCancelled(true);
        }
    }
}
