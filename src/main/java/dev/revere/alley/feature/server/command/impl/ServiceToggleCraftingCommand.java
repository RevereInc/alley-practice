package dev.revere.alley.feature.server.command.impl;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.server.ServerService;
import dev.revere.alley.core.locale.internal.types.ServerLocaleImpl;
import dev.revere.alley.common.text.CC;
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
            usage = "service togglecrafting",
            isAdminOnly = true
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        ItemStack heldItem = player.getInventory().getItemInHand();
        Material itemType = (heldItem != null) ? heldItem.getType() : Material.AIR;

        ServerService serverService = this.plugin.getService(ServerService.class);
        if (itemType == null || itemType == Material.AIR || !serverService.isCraftable(itemType)) {
            player.sendMessage(this.getMessage(ServerLocaleImpl.MUST_HOLD_CRAFTABLE_ITEM));
            return;
        }

        if (serverService.getBlockedCraftingItems().contains(itemType)) {
            serverService.removeFromBlockedCraftingList(itemType);
        } else {
            serverService.addToBlockedCraftingList(itemType);
        }

        serverService.saveBlockedItems(itemType);
        player.sendMessage(this.getMessage(ServerLocaleImpl.CRAFTING_TOGGLED)
                .replace("{item}", itemType.name())
                .replace("{status}", serverService.getBlockedCraftingItems().contains(itemType) ? CC.translate("&cDisabled") : CC.translate("&aEnabled"))
        );
    }
}