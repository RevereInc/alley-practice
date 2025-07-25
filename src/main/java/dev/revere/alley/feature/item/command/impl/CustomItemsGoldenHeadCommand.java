package dev.revere.alley.feature.item.command.impl;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.item.ItemService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project alley-practice
 * @since 18/07/2025
 */
public class CustomItemsGoldenHeadCommand extends BaseCommand {
    @CommandData(
            name = "customitems.goldenhead",
            aliases = {"alleyitems.goldenhead", "specialitems.goldenhead"},
            usage = "/customitems goldenhead <amount>",
            description = "Gives the player specific amount of custom golden heads",
            isAdminOnly = true
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/customitems goldenhead &6<amount>"));
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cInvalid amount specified. Please enter a valid number."));
            return;
        }

        if (amount <= 0) {
            player.sendMessage(CC.translate("&cAmount must be greater than zero."));
            return;
        }

        ItemService itemService = this.plugin.getService(ItemService.class);
        ItemStack goldenHead = itemService.getGoldenHead();
        if (goldenHead == null) {
            player.sendMessage(CC.translate("&cCustom golden head item is not configured."));
            return;
        }

        goldenHead.setAmount(amount);
        player.getInventory().addItem(goldenHead);
        player.sendMessage(CC.translate("&aYou have been given " + amount + " custom golden head(s)."));
    }
}