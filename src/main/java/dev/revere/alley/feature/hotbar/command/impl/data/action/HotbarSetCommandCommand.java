package dev.revere.alley.feature.hotbar.command.impl.data.action;

import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.feature.hotbar.HotbarItem;
import dev.revere.alley.feature.hotbar.HotbarService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Hamza
 * @since 02/11/2025
 */
public class HotbarSetCommandCommand extends BaseCommand {
    @CommandData(
            name = "hotbar.setcommand",
            isAdminOnly = true,
            usage = "hotbar setcommand <name> <slot>",
            aliases = {"hotbar.command"},
            description = "Set the command of a hotbar item."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            command.sendUsage();
            return;
        }

        String hotbarName = args[0];
        HotbarService hotbarService = this.plugin.getService(HotbarService.class);
        HotbarItem hotbarItem = hotbarService.getHotbarItem(hotbarName);
        if (hotbarItem == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.HOTBAR_NOT_FOUND).replace("{hotbar-name}", hotbarName));
            return;
        }

        String commandToSet = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        hotbarItem.getActionData().setCommand(commandToSet);
        hotbarService.saveToConfig(hotbarItem);
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.HOTBAR_SET_COMMAND).replace("{hotbar-name}", hotbarName).replace("{command}", commandToSet));
    }
}