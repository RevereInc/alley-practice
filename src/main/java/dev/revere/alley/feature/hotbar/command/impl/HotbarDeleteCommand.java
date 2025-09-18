package dev.revere.alley.feature.hotbar.command.impl;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.hotbar.HotbarItem;
import dev.revere.alley.feature.hotbar.HotbarService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 26/07/2025
 */
public class HotbarDeleteCommand extends BaseCommand {
    @CommandData(
            name = "hotbar.delete",
            isAdminOnly = true,
            usage = "hotbar delete <name>",
            description = "Delete a saved hotbar item."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/hotbar delete &6<name>"));
            return;
        }

        HotbarService hotbarService = this.plugin.getService(HotbarService.class);

        String name = args[0];

        HotbarItem hotbarItem = hotbarService.getHotbarItem(name);
        if (hotbarItem == null) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.HOTBAR_NOT_FOUND).replace("{hotbar-name}", name));
            return;
        }

        hotbarService.deleteHotbarItem(hotbarItem);
        player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.HOTBAR_DELETED_ITEM).replace("{hotbar-name}", name));
    }
}
