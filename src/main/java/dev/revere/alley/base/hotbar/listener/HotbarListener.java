package dev.revere.alley.base.hotbar.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.base.hotbar.HotbarItem;
import dev.revere.alley.base.hotbar.HotbarService;
import dev.revere.alley.base.hotbar.enums.HotbarAction;
import dev.revere.alley.base.hotbar.enums.HotbarType;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.logger.Logger;
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

        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        HotbarService hotbarService = Alley.getInstance().getService(HotbarService.class);

        Player player = event.getPlayer();
        ItemStack clickedItem = player.getItemInHand();

        if (clickedItem == null || !clickedItem.hasItemMeta() || !clickedItem.getItemMeta().hasDisplayName()) {
            return;
        }

        Profile profile = profileService.getProfile(player.getUniqueId());
        HotbarType currentHotbarType = hotbarService.getCorrespondingType(profile);
        if (currentHotbarType == null) return;

        if (!hotbarService.isHotbarItem(clickedItem, currentHotbarType)) {
            return;
        }

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
                hotbarItem.getActionData().getMenu().openMenu(player);
                break;
        }

        event.setCancelled(true);
    }
}