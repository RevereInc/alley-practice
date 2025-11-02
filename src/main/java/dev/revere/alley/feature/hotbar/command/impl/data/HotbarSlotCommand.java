package dev.revere.alley.feature.hotbar.command.impl.data;

import dev.revere.alley.common.text.EnumFormatter;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.feature.hotbar.HotbarItem;
import dev.revere.alley.feature.hotbar.HotbarService;
import dev.revere.alley.feature.hotbar.HotbarType;
import dev.revere.alley.feature.hotbar.data.HotbarTypeData;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Hamza
 * @since 02/11/2025
 */
public class HotbarSlotCommand extends BaseCommand {
    @CommandData(
            name = "hotbar.slot",
            isAdminOnly = true,
            usage = "hotbar slot <name> <type> <slot>",
            description = "Set the action for a hotbar slot."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            command.sendUsage();
            return;
        }

        HotbarService hotbarService = this.plugin.getService(HotbarService.class);

        String name = args[0];

        HotbarItem hotbarItem = hotbarService.getHotbarItem(name);
        if (hotbarItem == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.HOTBAR_NOT_FOUND).replace("{hotbar-name}", name));
            return;
        }

        HotbarType type;
        try {
            type = HotbarType.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException exception) {
            player.sendMessage(EnumFormatter.outputAvailableValues(HotbarType.class));
            return;
        }

        int slot;
        try {
            slot = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_NUMBER).replace("{input}", args[2]));
            return;
        }

        HotbarTypeData typeData = hotbarItem.getTypeData().stream()
                .filter(data -> data.getType() == type)
                .findFirst()
                .orElse(null);

        typeData.setSlot(slot);
        hotbarService.saveToConfig(hotbarItem);
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.HOTBAR_SET_SLOT)
                .replace("{hotbar-name}", name)
                .replace("{type}", type.name())
                .replace("{slot}", String.valueOf(slot)));
    }
}
