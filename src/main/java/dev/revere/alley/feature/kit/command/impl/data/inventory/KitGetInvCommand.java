package dev.revere.alley.feature.kit.command.impl.data.inventory;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.locale.impl.KitLocale;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 28/04/2024 - 22:25
 */
public class KitGetInvCommand extends BaseCommand {
    @CommandData(name = "kit.getinventory", aliases = "kit.getinv",permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/kit getinventory &b<kitName>"));
            return;
        }

        String kitName = args[0];
        Kit kit = Alley.getInstance().getKitService().getKit(kitName);

        if (kit == null) {
            player.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        player.getInventory().setContents(kit.getInventory());
        player.getInventory().setArmorContents(kit.getArmor());
        player.sendMessage(CC.translate(KitLocale.KIT_INVENTORY_GIVEN.getMessage().replace("{kit-name}", kitName)));
    }
}