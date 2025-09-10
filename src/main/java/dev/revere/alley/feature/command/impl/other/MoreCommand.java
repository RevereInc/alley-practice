package dev.revere.alley.feature.command.impl.other;

import dev.revere.alley.core.locale.internal.types.ErrorLocaleImpl;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.common.text.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @date 28/10/2024 - 08:50
 */
public class MoreCommand extends BaseCommand {
    @CommandData(
            name = "more",
            isAdminOnly = true,
            usage = "more <soup/potion> <amount>",
            description = "Gives you more soup or potions."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/more &6<soup/potion> <amount>"));
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException exception) {
            player.sendMessage(this.getMessage(ErrorLocaleImpl.INVALID_NUMBER).replace("{input}", args[1]));
            return;
        }

        if (args[0].equalsIgnoreCase("soup")) {
            for (int i = 0; i < amount; i++) {
                player.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));
            }
            player.sendMessage(CC.translate("&aYou've received &6" + amount + " &asoups."));
        } else if (args[0].equalsIgnoreCase("potion")) {
            for (int i = 0; i < amount; i++) {
                player.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 16421));
            }
            player.sendMessage(CC.translate("&aYou've received &6" + amount + " &apotions."));
        } else {
            player.sendMessage(CC.translate("&cInvalid item."));
        }
    }
}