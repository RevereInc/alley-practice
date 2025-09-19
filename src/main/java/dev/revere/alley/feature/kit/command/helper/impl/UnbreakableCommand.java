package dev.revere.alley.feature.kit.command.helper.impl;

import dev.revere.alley.common.reflect.utility.ReflectionUtility;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @date 14/07/2025
 */
public class UnbreakableCommand extends BaseCommand {
    @CommandData(
            name = "unbreakable",
            description = "Set the unbreakable state of the item in your hand",
            usage = "unbreakable <true|false>",
            isAdminOnly = true
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/unbreakable &6<true|false>"));
            return;
        }

        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ERROR_YOU_MUST_HOLD_ITEM));
            return;
        }

        boolean unbreakable = Boolean.parseBoolean(args[0]);

        ItemStack unbreakAbleItem = ReflectionUtility.setUnbreakable(item, unbreakable);
        player.setItemInHand(unbreakAbleItem);

        player.sendMessage(CC.translate("&aSuccessfully set the unbreakable state of the item to &6" + unbreakable + "&a."));
    }
}