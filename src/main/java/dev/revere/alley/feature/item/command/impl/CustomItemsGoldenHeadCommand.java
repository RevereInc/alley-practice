package dev.revere.alley.feature.item.command.impl;

import com.boydti.fawe.util.chat.Message;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.feature.item.ItemService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
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
            usage = "customitems goldenhead <amount>",
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
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ERROR_INVALID_NUMBER).replace("{input}", args[0]));
            return;
        }

        if (amount <= 0) {
            //TODO: Locale
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ERROR_AMOUNT_MUST_BE_GREATER_THAN_ZERO));
            return;
        }

        ItemService itemService = this.plugin.getService(ItemService.class);
        ItemStack goldenHead = itemService.getGoldenHead();
        if (goldenHead == null) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ITEM_NOT_CONFIGURED).replace("{item-name}", "Custom Golden Head"));
            return;
        }

        goldenHead.setAmount(amount);
        player.getInventory().addItem(goldenHead);
        player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ITEM_GIVEN)
                .replace("{item-name}", "Custom Golden Head" + (amount > 1 ? "s" : ""))
                .replace("{amount}", String.valueOf(amount))
        );
    }
}