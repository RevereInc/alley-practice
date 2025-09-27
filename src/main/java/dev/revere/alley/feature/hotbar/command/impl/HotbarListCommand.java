package dev.revere.alley.feature.hotbar.command.impl;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.feature.hotbar.HotbarItem;
import dev.revere.alley.feature.hotbar.HotbarService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * @author Emmy
 * @project alley-practice
 * @since 26/07/2025
 */
public class HotbarListCommand extends BaseCommand {
    @CommandData(
            name = "hotbar.list",
            isAdminOnly = true,
            usage = "hotbar list",
            description = "Sends a list of all hotbar items."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        HotbarService hotbarService = this.plugin.getService(HotbarService.class);

        Collection<HotbarItem> hotbarItems = hotbarService.getHotbarItems();

        if (hotbarItems.isEmpty()) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.HOTBAR_NO_HOTBAR_ITEMS_CREATED));
            return;
        }

        player.sendMessage(CC.translate("&6Hotbar Items:"));
        for (HotbarItem item : hotbarItems) {
            player.sendMessage(CC.translate(" &e• &f" + item.getName()));
        }
    }
}