package dev.revere.alley.feature.hotbar.command.impl.data.lore;

import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.feature.hotbar.HotbarItem;
import dev.revere.alley.feature.hotbar.HotbarService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Hamza
 * @since 02/11/2025
 */
public class HotbarRemoveLoreCommand extends BaseCommand {
    @CommandData(
            name = "hotbar.removelore",
            isAdminOnly = true,
            usage = "hotbar removelore <name> <lineNumber>",
            description = "Remove a lore line from a hotbar item."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            command.sendUsage();
            return;
        }

        String name = args[0];
        HotbarService hotbarService = this.plugin.getService(HotbarService.class);
        HotbarItem hotbarItem = hotbarService.getHotbarItem(name);
        if (hotbarItem == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.HOTBAR_NOT_FOUND).replace("{hotbar-name}", name));
            return;
        }

        int lineNumber;
        try {
            lineNumber = Integer.parseInt(args[1]) - 1;
        } catch (NumberFormatException e) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_NUMBER).replace("{input}", args[1]));
            return;
        }

        if (lineNumber < 0 || lineNumber >= hotbarItem.getLore().size()) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.HOTBAR_INVALID_LORE_LINE).replace("{hotbar-name}", name));
            return;
        }

        String removedLore = hotbarItem.getLore().remove(lineNumber);
        hotbarService.saveToConfig(hotbarItem);
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.HOTBAR_REMOVED_LORE).replace("{hotbar-name}", name).replace("{lore-line}", removedLore));
    }
}