package dev.revere.alley.feature.hotbar.command.impl.data.action;

import dev.revere.alley.common.text.CC;
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
public class HotbarSetMenuCommand extends BaseCommand {
    @CommandData(
            name = "hotbar.setmenu",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "hotbar setmenu <hotbar-name> <menu-name>",
            aliases = {"hotbar.menu"},
            description = "Set the menu action for a hotbar item."
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

        String menuName = args[1];
        String[] availableMenus = hotbarService.getMenuNames();
        if (!Arrays.asList(availableMenus).contains(menuName)) {
            player.sendMessage(CC.translate("&cMenu &e" + menuName + " &cdoes not exist. Available menus: &e" + String.join(", ", availableMenus)));
            return;
        }

        hotbarItem.getActionData().setMenuName(menuName);
        hotbarService.saveToConfig(hotbarItem);
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.HOTBAR_SET_MENU).replace("{hotbar-name}", hotbarName).replace("{menu-name}", menuName));
    }
}