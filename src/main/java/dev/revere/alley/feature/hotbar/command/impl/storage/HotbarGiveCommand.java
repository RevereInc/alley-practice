package dev.revere.alley.feature.hotbar.command.impl.storage;

import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.feature.hotbar.HotbarItem;
import dev.revere.alley.feature.hotbar.HotbarService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Hamza
 * @since 01/11/2025
 */
public class HotbarGiveCommand extends BaseCommand {
    @CommandData(
            name = "hotbar.give",
            isAdminOnly = true,
            usage = "hotbar give <name>",
            description = "Give a hotbar item to a player."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
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

        ItemStack itemStack = hotbarService.buildReceivableItem(hotbarItem);
        player.getInventory().addItem(itemStack);
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.HOTBAR_GIVEN_ITEM).replace("{hotbar-name}", name));
    }
}
