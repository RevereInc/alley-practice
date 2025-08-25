package dev.revere.alley.feature.server.listener;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.feature.server.ServerService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project alley-practice
 * @since 15/07/2025
 */
public class CraftingListener implements Listener {
    @EventHandler
    private void onPrepareItemCraft(PrepareItemCraftEvent event) {
        Player player = (Player) event.getView().getPlayer();
        if (player == null) {
            return;
        }

        Profile profile = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.PLAYING && profile.getState() != ProfileState.FFA && profile.getState() != ProfileState.SPECTATING) {
            return;
        }

        ItemStack result = event.getInventory().getResult();
        if (result == null || result.getType() == Material.AIR) {
            return;
        }

        if (AlleyPlugin.getInstance().getService(ServerService.class).getBlockedCraftingItems().contains(result.getType())) {
            event.getInventory().setResult(null);
        }
    }
}