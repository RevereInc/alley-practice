package dev.revere.alley.feature.kit.command.impl.data;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @date 08/10/2024 - 19:41
 */
public class KitSetDisclaimerCommand extends BaseCommand {
    @CommandData(
            name = "kit.setdisclaimer",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "kit setdisclaimer <kitName> <disclaimer>",
            description = "Set the disclaimer for a kit."
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(CC.translate("&6Usage: &e/kit setdisclaimer &6<kitName> <disclaimer>"));
            return;
        }

        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            sender.sendMessage(CC.translate(this.getMessage(GlobalMessagesLocaleImpl.KIT_NOT_FOUND)));
            return;
        }

        String disclaimer = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        kit.setDisclaimer(disclaimer);
        kitService.saveKit(kit);
        sender.sendMessage(CC.translate(this.getMessage(GlobalMessagesLocaleImpl.KIT_DISCLAIMER_SET)).replace("{kit-name}", kit.getName()).replace("{disclaimer}", disclaimer));
    }
}