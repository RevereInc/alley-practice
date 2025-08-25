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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Strength extends Ability {
    private final AlleyPlugin plugin = AlleyPlugin.getInstance();

    public Strength() {
        super("STRENGTH");
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (!isAbility(event.getItem())) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);

            Player player = event.getPlayer();
            ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
            AbilityService abilityService = AlleyPlugin.getInstance().getService(AbilityService.class);

            Profile profile = profileService.getProfile(player.getUniqueId());

            if (profile.getCooldown(Strength.class).onCooldown(player)) {
                player.sendMessage(CC.translate("&fYou are on &6&lStrength &7cooldown for &4" + DurationFormatter.getRemaining(profile.getCooldown(Strength.class).getRemainingMillis(player), true, true)));
                player.updateInventory();
                return;
            }

            if (profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).onCooldown(player)) {
                player.sendMessage(CC.translate("&fYou are on &6&lPartner Item &fcooldown for &6" + DurationFormatter.getRemaining(profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).getRemainingMillis(player), true, true)));
                player.updateInventory();
                return;
            }

            PlayerUtil.decrement(player);

            profile.getCooldown(Strength.class).applyCooldown(player, 60 * 1000);
            profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).applyCooldown(player, 10 * 1000);

            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 6, 1));

            abilityService.sendCooldownExpiredMessage(player, this.getName(), this.getAbility());
            abilityService.sendPlayerMessage(player, this.getAbility());
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
                    player.sendMessage(CC.translate("&fYou are on cooldown for &4" + DurationFormatter.getRemaining(profile.getCooldown(Strength.class).getRemainingMillis(player), true)));
                    event.setCancelled(true);
                    player.updateInventory();
                }
            }
        }
    }
}
