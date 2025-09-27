package dev.revere.alley.feature.kit.command.impl.manage;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Alley
 * @since 11/04/2025
 */
public class KitToggleCommand extends BaseCommand {
    @CommandData(
            name = "kit.toggle",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "kit toggle <kitName>",
            description = "Toggle a kit's enabled status."
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        String kitName = args[0];
        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(kitName);
        if (kit == null) {
            sender.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.KIT_NOT_FOUND)));
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