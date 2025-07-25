package dev.revere.alley.base.kit.command.impl.manage;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.kit.KitService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.config.locale.impl.KitLocale;
import dev.revere.alley.util.chat.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Alley
 * @since 11/04/2025
 */
public class KitToggleCommand extends BaseCommand {
    @CommandData(name = "kit.toggle", isAdminOnly = true, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&6Usage: &e/kit toggle &6<kitName>"));
            return;
        }

        String kitName = args[0];
        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(kitName);
        if (kit == null) {
            sender.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        if (kit.getIcon() == null) {
            sender.sendMessage(CC.translate("&cThis kit does not have an icon set. Please set an icon before toggling."));
            return;
        }

        if (kit.getCategory() == null) {
            sender.sendMessage(CC.translate("&cThis kit does not have a category set. Please set a category before toggling."));
            return;
        }

        if (kit.getKitSettings().isEmpty()) {
            sender.sendMessage(CC.translate("&cThis kit does not have any settings defined. Please configure the kit settings before toggling."));
            return;
        }

        kit.setEnabled(!kit.isEnabled());
        kitService.saveKit(kit);
        String status = kit.isEnabled() ? CC.translate("&aenabled") : CC.translate("&cdisabled");
        sender.sendMessage(CC.translate("&aSuccessfully " + status + " &athe kit &6" + kit.getName() + "&a."));
    }
}