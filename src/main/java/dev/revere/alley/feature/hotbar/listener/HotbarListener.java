package dev.revere.alley.feature.hotbar.listener;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.logger.Logger;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.feature.hotbar.HotbarAction;
import dev.revere.alley.feature.hotbar.HotbarItem;
import dev.revere.alley.feature.hotbar.HotbarService;
import dev.revere.alley.feature.hotbar.HotbarType;
import dev.revere.alley.library.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public class HotbarListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if ((action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
        HotbarService hotbarService = AlleyPlugin.getInstance().getService(HotbarService.class);

        Player player = event.getPlayer();
        ItemStack clickedItem = player.getItemInHand();

        if (clickedItem == null || !clickedItem.hasItemMeta() || !clickedItem.getItemMeta().hasDisplayName()) {
            return;
        }

        Profile profile = profileService.getProfile(player.getUniqueId());
        HotbarType currentHotbarType = hotbarService.getCorrespondingType(profile);
        if (currentHotbarType == null) return;

        HotbarItem hotbarItem = hotbarService.getHotbarItem(clickedItem, currentHotbarType);
        if (hotbarItem == null) {
            return;
        }

        HotbarAction actionType = hotbarItem.getActionData().getAction();
        if (actionType == null) {
            Logger.error("Hotbar item action type is null for item: " + hotbarItem.getName());
            return;
        }

        switch (actionType) {
            case RUN_COMMAND:
                player.performCommand(hotbarItem.getActionData().getCommand());
                break;
            case OPEN_MENU:
                Menu menu = hotbarService.getMenuInstanceFromName(hotbarItem.getActionData().getMenuName(), player);
                menu.openMenu(player);
                break;
        }

        event.setCancelled(true);
    }
}