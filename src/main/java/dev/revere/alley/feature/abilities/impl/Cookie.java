package dev.revere.alley.feature.abilities.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.abilities.Ability;
import dev.revere.alley.feature.abilities.AbilityService;
import dev.revere.alley.feature.abilities.utils.DurationFormatter;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.GlobalCooldown;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Cookie extends Ability {
    private final Alley plugin = Alley.getInstance();

    public Cookie() {
        super("COOKIE");
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (!isAbility(event.getItem())) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);

            Player player = event.getPlayer();

            ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
            AbilityService abilityService = Alley.getInstance().getService(AbilityService.class);

            Profile profile = profileService.getProfile(player.getUniqueId());

            if (profile.getCooldown(Cookie.class).onCooldown(player)) {
                player.sendMessage(CC.translate("&fYou are on &6&lCookie &7cooldown for &4" + DurationFormatter.getRemaining(profile.getCooldown(Cookie.class).getRemainingMillis(player), true, true)));
                player.updateInventory();
                return;
            }

            if(profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).onCooldown(player)){
                player.sendMessage(CC.translate("&fYou are on &6&lPartner Item &fcooldown for &6" + DurationFormatter.getRemaining(profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).getRemainingMillis(player), true, true)));
                player.updateInventory();
                return;
            }

            PlayerUtil.decrement(player);

            profile.getCooldown(Cookie.class).applyCooldown(player, 60 * 1000);
            profile.getGlobalCooldown(GlobalCooldown.PARTNER_ITEM).applyCooldown(player,  10 * 1000);

            player.removePotionEffect(PotionEffectType.REGENERATION);
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 7, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 7, 4));

            abilityService.sendCooldownExpiredMessage(player, this.getName(), this.getAbility());
            abilityService.sendPlayerMessage(player, this.getAbility());
        }
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
                    player.sendMessage(CC.translate("&fYou are on cooldown for &4" + DurationFormatter.getRemaining(profile.getCooldown(Cookie.class).getRemainingMillis(player), true)));
                    event.setCancelled(true);
                    player.updateInventory();
                }
            }
        }
    }
}
