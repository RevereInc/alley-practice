package dev.revere.alley.feature.item.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.base.cooldown.Cooldown;
import dev.revere.alley.base.cooldown.CooldownRepository;
import dev.revere.alley.base.cooldown.enums.CooldownType;
import dev.revere.alley.feature.item.ItemService;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

/**
 * @author Emmy
 * @project alley-practice
 * @since 18/07/2025
 */
public class ItemListener implements Listener {

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        if (profile.getMatch() == null && profile.getFfaMatch() == null) {
            return;
        }

        ItemStack item = event.getItem();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        ItemService itemService = Alley.getInstance().getService(ItemService.class);
        if (item.isSimilar(itemService.getGoldenHead())) {
            event.setCancelled(true);

            if (this.isOnHeadCooldown(player)) return;

            itemService.performHeadConsume(player, item);
        }
    }

    /**
     * Checks if the player is on cooldown for consuming a golden head.
     *
     * @param player The player to check the cooldown for.
     * @return true if the player is on cooldown, false otherwise.
     */
    private boolean isOnHeadCooldown(Player player) {
        CooldownType cooldownType = CooldownType.GOLDEN_HEAD_CONSUME;
        CooldownRepository cooldownRepository = Alley.getInstance().getService(CooldownRepository.class);
        Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownRepository.getCooldown(player.getUniqueId(), cooldownType));
        if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {
            player.sendMessage(CC.translate("&cYou must wait " + optionalCooldown.get().remainingTimeInMinutes() + " &cbefore consuming another &6&lGolden Head&c."));
            return true;
        }

        Cooldown cooldown = optionalCooldown.orElseGet(() -> {
            Cooldown newCooldown = new Cooldown(cooldownType, () -> player.sendMessage(CC.translate("&aYou can now use &6&lGolden Head&a's again.")));
            cooldownRepository.addCooldown(player.getUniqueId(), cooldownType, newCooldown);
            return newCooldown;
        });

        cooldown.resetCooldown();
        return false;
    }
}