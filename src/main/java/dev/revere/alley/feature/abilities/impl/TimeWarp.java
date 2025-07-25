package dev.revere.alley.feature.abilities.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import dev.revere.alley.Alley;
import dev.revere.alley.feature.abilities.Ability;
import dev.revere.alley.feature.abilities.AbilityService;
import dev.revere.alley.feature.abilities.utils.DurationFormatter;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.GlobalCooldown;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;
import java.util.UUID;

public class TimeWarp extends Ability {

    private final Map<UUID, Map<Location, Long>> lastPearl;

    public TimeWarp() {
        super("TIME_WARP");

        this.lastPearl = Maps.newConcurrentMap();

        Bukkit.getScheduler().runTaskTimerAsynchronously(Alley.getInstance(), () -> {

            for (Map.Entry<UUID, Map<Location, Long>> lastPearlEntry : this.lastPearl.entrySet()) {
                UUID uuid = lastPearlEntry.getKey();
                Map<Location, Long> map = lastPearlEntry.getValue();

                if (System.currentTimeMillis() > map.values().iterator().next()) {
                    this.lastPearl.remove(uuid);
                }
            }

        }, 20L, 20L);
    }

    @EventHandler
    public void checkCooldown(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        Profile profile = Alley.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
            if (!isAbility(player.getItemInHand())) {
                return;
            }
            if (isAbility(player.getItemInHand())) {
                if (this.hasCooldown(player)) {
                    player.sendMessage(CC.translate("&fYou are on cooldown for &4" + DurationFormatter.getRemaining(profile.getCooldown(TimeWarp.class).getRemainingMillis(player), true)));
                    event.setCancelled(true);
                    player.updateInventory();
                }
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getEntityType() != EntityType.ENDER_PEARL) {
            return;
        }

        EnderPearl enderpearl = (EnderPearl) event.getEntity();

        if (!(enderpearl.getShooter() instanceof Player)) {
            return;
        }

        Player shooter = (Player) enderpearl.getShooter();
        long PEARL_EXPIRE = 15 * 1000;
        Map<Location, Long> map = ImmutableMap.of(shooter.getLocation().clone(),
                System.currentTimeMillis() + PEARL_EXPIRE);

        this.lastPearl.put(shooter.getUniqueId(), map);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        AbilityService abilityService = Alley.getInstance().getService(AbilityService.class);

        Profile profile = profileService.getProfile(player.getUniqueId());

        if (!event.getAction().name().contains("RIGHT_CLICK")) {
            return;
        }

        if (!isAbility(event.getItem())) {
            return;
        }

        if (profile.getCooldown(TimeWarp.class).onCooldown(player)) {
            event.setCancelled(true);
            player.sendMessage(CC.translate("&fYou are on &6&lTime Warp &7cooldown for &4" + DurationFormatter.getRemaining(profile.getCooldown(TimeWarp.class).getRemainingMillis(player), true, true)));
            player.updateInventory();
            event.setCancelled(true);
            return;
        }

        if (profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).onCooldown(player)) {
            player.sendMessage(CC.translate("&fYou are on &6&lPartner Item &fcooldown for &6" + DurationFormatter.getRemaining(profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).getRemainingMillis(player), true, true)));
            player.updateInventory();
            event.setCancelled(true);
            return;
        }

        if (!this.lastPearl.containsKey(player.getUniqueId())) {
            player.sendMessage(CC.translate("&cYour last enderpearl location has expired!"));
            return;
        }

        Location location = this.lastPearl.get(player.getUniqueId()).keySet().iterator().next().clone();

        PlayerUtil.decrement(player);

        profile.getCooldown(TimeWarp.class).applyCooldown(player, 60 * 1000);
        profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).applyCooldown(player, 10 * 1000);

        player.sendMessage(CC.translate(
                "&7You &a4ctivated &7a Time Warp, so you will be teleported to your last thrown enderpearl's location in &43 &7seconds!"));

        Bukkit.getScheduler().runTaskLater(Alley.getInstance(), () -> {
            player.teleport(location);
            player.sendMessage(
                    CC.translate("&7You have been &4teleported &7to your last thrown enderpearl's location!"));

            this.lastPearl.remove(player.getUniqueId());
        }, 60L);

        abilityService.sendCooldownExpiredMessage(player, this.getName(), this.getAbility());
        abilityService.sendPlayerMessage(player, this.getAbility());
    }
}