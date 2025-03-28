package dev.revere.alley.feature.kit.command.impl.manage;

import dev.revere.alley.Alley;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Alley
 * @date 28/04/2024 - 22:07
 */
public class KitListCommand extends BaseCommand {
    @CommandData(name = "kit.list", aliases = {"kits"}, permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        sender.sendMessage("");
        sender.sendMessage(CC.translate("     &b&lKit List &f(" + Alley.getInstance().getKitService().getKits().size() + "&f)"));
        if (Alley.getInstance().getKitService().getKits().isEmpty()) {
            sender.sendMessage(CC.translate("      &f● &cNo Kits available."));
        }
        Alley.getInstance().getKitService().getKits().forEach(kit -> sender.sendMessage(CC.translate("      &f● &b" + kit.getDisplayName() + " &f(" + (kit.isEnabled() ? "&aEnabled" : "&cDisabled") + "&f)")));
        sender.sendMessage("");
    }
}