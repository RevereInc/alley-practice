package dev.revere.alley.feature.ffa.command.impl.admin.data;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 21/05/2024 - 00:25
 */
public class FFASetSlotCommand extends BaseCommand {
    @CommandData(
            name = "ffa.setslot",
            isAdminOnly = true,
            usage = "ffa setslot <kitName> <slot>",
            description = "Set the FFA slot for a kit."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 2) {
            command.sendUsage();
            return;
        }

        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            player.sendMessage(CC.translate(this.getMessage(GlobalMessagesLocaleImpl.KIT_NOT_FOUND)));
            return;
        }

        int slot;
        try {
            slot = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate(this.getMessage(GlobalMessagesLocaleImpl.KIT_SLOT_MUST_BE_NUMBER)));
            return;
        }

        if (!kit.isFfaEnabled()) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.FFA_DISABLED).replace("{kit-name}", kit.getName()));
            return;
        }

        kit.setFfaSlot(slot);
        kitService.saveKit(kit);
        player.sendMessage(CC.translate(this.getMessage(GlobalMessagesLocaleImpl.KIT_FFA_SLOT_SET))
                .replace("{kit-name}", kit.getName())
                .replace("{slot}", String.valueOf(slot))
        );
    }
}