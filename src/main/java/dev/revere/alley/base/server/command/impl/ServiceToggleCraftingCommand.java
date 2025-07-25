package dev.revere.alley.base.server.command.impl;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.server.ServerService;
import dev.revere.alley.config.locale.impl.ServerLocale;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project alley-practice
 * @since 14/07/2025
 */
public class ServiceToggleCraftingCommand extends BaseCommand {
    @CommandData(
            name = "service.togglecrafting",
            description = "Command to manage service crafting operations.",
            usage = "/service togglecrafting",
            isAdminOnly = true
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        ItemStack heldItem = player.getInventory().getItemInHand();
        Material itemType = (heldItem != null) ? heldItem.getType() : Material.AIR;

        ServerService serverService = this.plugin.getService(ServerService.class);
        if (itemType == null || itemType == Material.AIR || !serverService.isCraftable(itemType)) {
            player.sendMessage(ServerLocale.MUST_HOLD_CRAFTABLE_ITEM.getMessage());
            return;
        }

        if (serverService.getBlockedCraftingItems().contains(itemType)) {
            serverService.removeFromBlockedCraftingList(itemType);
        } else {
            serverService.addToBlockedCraftingList(itemType);
        }

        serverService.saveBlockedItems(itemType);
        player.sendMessage(ServerLocale.CRAFTING_TOGGLED.getMessage().replace("{item}", itemType.name()).replace("{status}", serverService.getBlockedCraftingItems().contains(itemType) ? CC.translate("&cDisabled") : CC.translate("&aEnabled")));
    }
}