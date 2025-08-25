package dev.revere.alley.feature.abilities.types;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.common.time.DurationFormatter;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.enums.GlobalCooldown;
import dev.revere.alley.feature.abilities.Ability;
import dev.revere.alley.feature.abilities.AbilityService;
import org.bukkit.Location;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class Switcher extends Ability {
    private final AlleyPlugin plugin = AlleyPlugin.getInstance();

    public Switcher() {
        super("SWITCHER");
    }

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player shooter = (Player) event.getEntity().getShooter();
            if (isAbility(shooter.getItemInHand())) {
                event.getEntity().setMetadata(this.getAbility(), new FixedMetadataValue(this.plugin, true));
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!isAbility(event.getItem())) return;

        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player shooter = event.getPlayer();

            ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
            AbilityService abilityService = AlleyPlugin.getInstance().getService(AbilityService.class);

            Profile profile = profileService.getProfile(shooter.getUniqueId());

            if (profile.getCooldown(Switcher.class).onCooldown(shooter)) {
                shooter.sendMessage(CC.translate("&fYou are on &6&lSwitcher &7cooldown for &4" + DurationFormatter.getRemaining(profile.getCooldown(Switcher.class).getRemainingMillis(shooter), true, true)));
                shooter.updateInventory();
                event.setCancelled(true);
                return;
            }

            if (profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).onCooldown(shooter)) {
                shooter.sendMessage(CC.translate("&fYou are on &6&lPartner Item &fcooldown for &6" + DurationFormatter.getRemaining(profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).getRemainingMillis(shooter), true, true)));
                shooter.updateInventory();
                event.setCancelled(true);
                return;
            }

            profile.getCooldown(Switcher.class).applyCooldown(shooter, 8 * 1000);
            profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).applyCooldown(shooter, 10 * 1000);

            abilityService.sendCooldownExpiredMessage(shooter, this.getName(), this.getAbility());
            abilityService.sendPlayerMessage(shooter, this.getAbility());
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
                    player.sendMessage(CC.translate("&fYou are on cooldown for &4" + DurationFormatter.getRemaining(profile.getCooldown(Switcher.class).getRemainingMillis(player), true)));
                    event.setCancelled(true);
                    player.updateInventory();
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();

            if (projectile instanceof Egg && projectile.hasMetadata(this.getAbility())) {
                Player player = (Player) event.getEntity();
                Player shooter = (Player) projectile.getShooter();

                Location playerLocation = player.getLocation().clone();
                Location shooterLocation = shooter.getLocation().clone();

                player.teleport(shooterLocation);
                shooter.teleport(playerLocation);

                AlleyPlugin.getInstance().getService(AbilityService.class).sendTargetMessage(player, shooter, this.getAbility());
            } else if (projectile instanceof Snowball && projectile.hasMetadata(this.getAbility())) {
                Player player = (Player) event.getEntity();
                Player shooter = (Player) projectile.getShooter();

                Location playerLocation = player.getLocation().clone();
                Location shooterLocation = shooter.getLocation().clone();

                player.teleport(shooterLocation);
                shooter.teleport(playerLocation);

                AlleyPlugin.getInstance().getService(AbilityService.class).sendTargetMessage(player, shooter, this.getAbility());
            }
        }
    }
}