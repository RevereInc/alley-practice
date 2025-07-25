package dev.revere.alley.base.kit.command.impl.manage;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.kit.KitService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Alley
 * @date 28/04/2024 - 22:07
 */
public class KitListCommand extends BaseCommand {
    @CommandData(name = "kit.list", aliases = {"kits"}, isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        KitService kitService = this.plugin.getService(KitService.class);

        sender.sendMessage("");
        sender.sendMessage(CC.translate("     &6&lKit List &f(" + kitService.getKits().size() + "&f)"));
        if (kitService.getKits().isEmpty()) {
            sender.sendMessage(CC.translate("      &f● &cNo Kits available."));
        }
        kitService.getKits().forEach(kit -> sender.sendMessage(CC.translate("      &f● &6" + kit.getDisplayName() + " &f(" + (kit.isEnabled() ? "&aEnabled" : "&cDisabled") + "&f)")));
        sender.sendMessage("");
    }
}