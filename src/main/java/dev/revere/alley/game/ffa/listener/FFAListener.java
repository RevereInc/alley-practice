package dev.revere.alley.game.ffa.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.base.combat.CombatService;
import dev.revere.alley.base.cooldown.Cooldown;
import dev.revere.alley.base.cooldown.CooldownRepository;
import dev.revere.alley.base.cooldown.enums.CooldownType;
import dev.revere.alley.base.kit.setting.impl.mechanic.KitSettingNoHungerImpl;
import dev.revere.alley.game.ffa.cuboid.FFASpawnService;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.util.InventoryUtil;
import dev.revere.alley.util.ListenerUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 14:24
 */
public class FFAListener implements Listener {
    @EventHandler
    private void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.FFA) return;

        if (ListenerUtil.isSword(event.getItemDrop().getItemStack().getType())) {
            event.setCancelled(true);
            player.sendMessage(CC.translate("&cYou cannot drop your sword during this match."));
            return;
        }

        ListenerUtil.clearDroppedItemsOnRegularItemDrop(event.getItemDrop());
    }

    @EventHandler
    private void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.FFA) {
            return;
        }

        ItemStack item = event.getItem();
        if (item.getType() == Material.POTION) {
            Alley.getInstance().getServer().getScheduler().runTaskLater(Alley.getInstance(), () -> {
                player.getInventory().removeItem(new ItemStack(Material.GLASS_BOTTLE, 1));
                player.updateInventory();
            }, 1L);
        }
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.FFA) return;
        event.setDeathMessage(null);

        ListenerUtil.clearDroppedItemsOnDeath(event, player);

        Player killer = Alley.getInstance().getService(CombatService.class).getLastAttacker(player);

        Alley.getInstance().getServer().getScheduler().runTaskLater(Alley.getInstance(), () -> player.spigot().respawn(), 1L);
        Bukkit.getScheduler().runTaskLater(Alley.getInstance(), () -> profile.getFfaMatch().handleDeath(player, killer), 1L);
    }

    @EventHandler
    private void onPearlLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof EnderPearl)) return;
        if (!(event.getEntity().getShooter() instanceof Player)) return;

        Player player = (Player) event.getEntity().getShooter();
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() != ProfileState.FFA) return;

        if (Alley.getInstance().getService(FFASpawnService.class).getCuboid().isIn(player)) {
            event.setCancelled(true);
            InventoryUtil.giveItem(player, Material.ENDER_PEARL, 1);
            player.updateInventory();
            player.sendMessage(CC.translate("&cYou cannot use ender pearls at spawn."));
            return;
        }

        CooldownRepository cooldownRepository = Alley.getInstance().getService(CooldownRepository.class);
        Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownRepository.getCooldown(player.getUniqueId(), CooldownType.ENDER_PEARL));

        if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {
            event.setCancelled(true);
            InventoryUtil.giveItem(player, Material.ENDER_PEARL, 1);
            player.updateInventory();
            player.sendMessage(CC.translate("&cYou must wait " + optionalCooldown.get().remainingTime() + " seconds before using another ender pearl."));
            return;
        }

        Cooldown cooldown = optionalCooldown.orElseGet(() -> {
            Cooldown newCooldown = new Cooldown(CooldownType.ENDER_PEARL, () -> player.sendMessage(CC.translate("&aYou can now use pearls again!")));
            cooldownRepository.addCooldown(player.getUniqueId(), CooldownType.ENDER_PEARL, newCooldown);
            return newCooldown;
        });

        cooldown.resetCooldown();
    }

    @EventHandler
    private void onHunger(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
            Profile profile = profileService.getProfile(player.getUniqueId());
            if (profile.getState() != ProfileState.FFA) return;

            if (profile.getFfaMatch().getKit().isSettingEnabled(KitSettingNoHungerImpl.class)) {
                event.setCancelled(true);
            }
        }
    }
}