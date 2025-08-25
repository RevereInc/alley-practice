package dev.revere.alley.feature.abilities.types;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.PlayerUtil;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.common.time.DurationFormatter;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.enums.GlobalCooldown;
import dev.revere.alley.feature.abilities.Ability;
import dev.revere.alley.feature.abilities.AbilityService;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class Rocket extends Ability {

    public Rocket() {
        super("ROCKET");
    }

    @EventHandler
    public void onItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
        AbilityService abilityService = AlleyPlugin.getInstance().getService(AbilityService.class);

        Profile profile = profileService.getProfile(player.getUniqueId());

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!isAbility(player.getItemInHand())) {
                return;
            }

            if (profile.getCooldown(Rocket.class).onCooldown(player)) {
                player.sendMessage(CC.translate("&fYou are on &6&lRocket &7cooldown for &4" + DurationFormatter.getRemaining(profile.getCooldown(Rocket.class).getRemainingMillis(player), true, true)));
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

            if (isAbility(player.getItemInHand())) {
                player.setVelocity(new Vector(0.1D, 2.0D, 0.0D));

                PlayerUtil.decrement(player);

                profile.getCooldown(Rocket.class).applyCooldown(player, 60 * 1000);
                profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).applyCooldown(player, 10 * 1000);

                player.setMetadata("rocket", new FixedMetadataValue(AlleyPlugin.getInstance(), true));
            }
        }
    }

    @EventHandler
    public void checkCooldown(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        Profile profile = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
            if (!isAbility(player.getItemInHand())) {
                return;
            }
            if (isAbility(player.getItemInHand())) {
                if (this.hasCooldown(player)) {
                    player.sendMessage(CC.translate("&fYou are on cooldown for &4" + DurationFormatter.getRemaining(profile.getCooldown(Rocket.class).getRemainingMillis(player), true)));
                    event.setCancelled(true);
                    player.updateInventory();
                }
            }
        }
    }

    @EventHandler
    public void fallDamage(final EntityDamageEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            final Player player = (Player) event.getEntity();
            Profile profile = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
            if (profile.getCooldown(Rocket.class).onCooldown(player)) {
                event.setCancelled(true);
            }
        }
    }
}
