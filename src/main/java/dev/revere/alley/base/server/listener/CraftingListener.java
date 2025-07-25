package dev.revere.alley.base.server.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.base.server.ServerService;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.ProfileState;
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

        Profile profile = Alley.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.PLAYING && profile.getState() != ProfileState.FFA && profile.getState() != ProfileState.SPECTATING) {
            return;
        }

        ItemStack result = event.getInventory().getResult();
        if (result == null || result.getType() == Material.AIR) {
            return;
        }

        if (Alley.getInstance().getService(ServerService.class).getBlockedCraftingItems().contains(result.getType())) {
            event.getInventory().setResult(null);
        }
    }
}