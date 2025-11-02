package dev.revere.alley.feature.hotbar.command.impl.data.lore;

import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.feature.hotbar.HotbarItem;
import dev.revere.alley.feature.hotbar.HotbarService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * @author Hamza
 * @since 02/11/2025
 */
public class HotbarAddLoreCommand extends BaseCommand {
    @CommandData(
            name = "hotbar.addlore",
            isAdminOnly = true,
            usage = "hotbar addlore <name> <lore...>",
            description = "Add lore to a hotbar item."
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

        String loreLine = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        List<String> lore = hotbarItem.getLore();
        lore.add(loreLine);
        hotbarItem.setLore(lore);
        hotbarService.saveToConfig(hotbarItem);
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.HOTBAR_ADDED_LORE).replace("{hotbar-name}", name).replace("{lore-line}", loreLine));
    }
}