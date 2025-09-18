package dev.revere.alley.feature.hotbar.command.impl;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.common.text.EnumFormatter;
import dev.revere.alley.feature.hotbar.HotbarService;
import dev.revere.alley.feature.hotbar.HotbarType;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 21/07/2025
 */
public class HotbarCreateCommand extends BaseCommand {
    @CommandData(
            name = "hotbar.create",
            isAdminOnly = true,
            usage = "hotbar create <name> <type>",
            description = "Create a new hotbar item."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/hotbar create &6<name> <type>"));
            return;
        }

        String name = args[0];

        HotbarType type;
        try {
            type = HotbarType.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException exception) {
            player.sendMessage(EnumFormatter.outputAvailableValues(HotbarType.class));
            return;
        }

        this.plugin.getService(HotbarService.class).createHotbarItem(name, type);
        player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.HOTBAR_CREATED_ITEM)
                .replace("{name}", name)
                .replace("{type}", type.name())
        );
    }
}