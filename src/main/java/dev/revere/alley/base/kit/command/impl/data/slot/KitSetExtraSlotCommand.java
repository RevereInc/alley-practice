package dev.revere.alley.base.kit.command.impl.data.slot;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.kit.KitService;
import dev.revere.alley.config.locale.impl.KitLocale;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

public class KitSetExtraSlotCommand extends BaseCommand {

    @CommandData(name = "kit.setextraslot", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/kit setextraslot &b<kitName> <slot>"));
            return;
        }

        KitService kitService = this.plugin.getKitService();
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            player.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        int slot;
        try {
            slot = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate(KitLocale.SLOT_MUST_BE_NUMBER.getMessage()));
            return;
        }

        kit.setExtraSlot(slot);
        kitService.saveKit(kit);
        player.sendMessage(CC.translate(KitLocale.KIT_EXTRASLOT_SET.getMessage()).replace("{kit-name}", kit.getName()).replace("{slot}", String.valueOf(slot)));
    }
}
