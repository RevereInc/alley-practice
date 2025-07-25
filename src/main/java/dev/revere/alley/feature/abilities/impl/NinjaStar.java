package dev.revere.alley.feature.abilities.impl;

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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public class NinjaStar extends Ability {
    private final Alley plugin = Alley.getInstance();
    private final Map<UUID, UUID> TAGGED = Maps.newHashMap();

    public NinjaStar() {
        super("NINJA_STAR");
    }

    @EventHandler
    private void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();

            //AbilityCooldowns.addCooldown("TELEPORT", victim, 15);
            TAGGED.put(victim.getUniqueId(), damager.getUniqueId());
        }
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (!isAbility(event.getItem())) return;

        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            event.setCancelled(true);

            Player player = event.getPlayer();

            ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
            AbilityService abilityService = Alley.getInstance().getService(AbilityService.class);

            Profile profile = profileService.getProfile(player.getUniqueId());

            if (profile.getCooldown(NinjaStar.class).onCooldown(player)) {
                player.sendMessage(CC.translate("&fYou are on &6&lNinja Star &7cooldown for &4" + DurationFormatter.getRemaining(profile.getCooldown(NinjaStar.class).getRemainingMillis(player), true, true)));
                player.updateInventory();
                return;
            }

            if (profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).onCooldown(player)) {
                player.sendMessage(CC.translate("&fYou are on &6&lPartner Item &fcooldown for &6" + DurationFormatter.getRemaining(profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).getRemainingMillis(player), true, true)));
                player.updateInventory();
                return;
            }

            //if (!AbilityCooldowns.isOnCooldown("TELEPORT", player)) return;

            PlayerUtil.decrement(player);

            Player target = Bukkit.getPlayer(TAGGED.get(player.getUniqueId()));

            profile.getCooldown(NinjaStar.class).applyCooldown(player, 60 * 1000);
            profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).applyCooldown(player, 10 * 1000);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (target == null) {
                        return;
                    }
                    player.teleport(target.getLocation());
                    player.sendMessage(CC.translate("&7You have been successfully teleported")); // you just got teleported back
                }
            }.runTaskLaterAsynchronously(this.plugin, (5 * 10));

            abilityService.sendCooldownExpiredMessage(player, this.getName(), this.getAbility());
            abilityService.sendPlayerMessage(player, this.getAbility());
            abilityService.sendTargetMessage(target, player, this.getAbility());

            TAGGED.remove(player.getUniqueId());
        }
    }
}