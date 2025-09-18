package dev.revere.alley.feature.command.impl.other;

import dev.revere.alley.common.logger.Logger;
import dev.revere.alley.common.reflect.ReflectionService;
import dev.revere.alley.common.reflect.internal.types.VirtualStackReflectionServiceImpl;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 14/07/2025
 */
public class VirtualStackCommand extends BaseCommand {
    @CommandData(
            name = "virtualstack",
            isAdminOnly = true,
            description = "Bypass stack size limits for items and set a virtual stack amount (max 127)",
            usage = "virtualstack <amount> [bypassLimit]"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/virtualstack &6<amount> &7[bypassLimit]"));
            player.sendMessage(CC.translate("&7Example: /virtualstack 127"));
            player.sendMessage(CC.translate("&7To bypass stack size limits, use: '/virtualstack 130 true'"));
            return;
        }

        if (player.getInventory().getItemInHand() == null || player.getInventory().getItemInHand().getType() == Material.AIR) {
            player.sendMessage(CC.translate("&cYou must be holding an item to set its virtual stack amount."));
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException exception) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ERROR_INVALID_NUMBER).replace("{input}", args[0]));
            return;
        }

        boolean bypassLimit = args.length > 1 && args[1].equalsIgnoreCase("true");

        if (!bypassLimit && (amount < 1 || amount > 127)) {
            player.sendMessage(CC.translate("&cAmount must be between 1 and 127."));
            return;
        }

        try {
            this.plugin.getService(ReflectionService.class).getReflectionService(VirtualStackReflectionServiceImpl.class).setVirtualStackAmount(player, amount);
            player.sendMessage(CC.translate("&aSuccessfully set the virtual stack amount to &6" + amount + "&a."));
        } catch (Exception exception) {
            Logger.logException("Failed to set virtual stack amount", exception);
        }
    }
}
