package dev.revere.alley.feature.kit.command.impl.data.inventory;

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
 * @date 28/04/2024 - 22:25
 */
public class KitGetInvCommand extends BaseCommand {
    @CommandData(
            name = "kit.getinventory",
            aliases = "kit.getinv",
            isAdminOnly = true,
            usage = "kit getinventory <kitName>",
            description = "Get the inventory of a kit"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/kit getinventory &6<kitName>"));
            return;
        }

        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            player.sendMessage(CC.translate(this.getMessage(GlobalMessagesLocaleImpl.KIT_NOT_FOUND)));
            return;
        }

        player.getInventory().setContents(kit.getItems());
        player.getInventory().setArmorContents(kit.getArmor());
        player.sendMessage(CC.translate(this.getMessage(GlobalMessagesLocaleImpl.KIT_INVENTORY_GIVEN).replace("{kit-name}", kit.getName())));
    }
}