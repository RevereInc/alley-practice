package dev.revere.alley.game.ffa.command.impl.admin.data;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.kit.KitService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.config.locale.impl.KitLocale;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 21/05/2024 - 00:25
 */
public class FFASetSlotCommand extends BaseCommand {
    @CommandData(name = "ffa.setslot", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/ffa setslot &6<kitName> <slot>"));
            return;
        }

        KitService kitService = this.plugin.getService(KitService.class);
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

        if (!kit.isFfaEnabled()) {
            player.sendMessage(CC.translate("&cFFA mode is not enabled for this kit!"));
            return;
        }

        kit.setFfaSlot(slot);
        kitService.saveKit(kit);
        player.sendMessage(CC.translate(KitLocale.KIT_FFASLOT_SET.getMessage()).replace("{kit-name}", kit.getName()).replace("{slot}", String.valueOf(slot)));
    }
}
