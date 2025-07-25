package dev.revere.alley.base.kit.command.impl.data;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.kit.KitService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.config.locale.impl.KitLocale;
import dev.revere.alley.util.chat.CC;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @date 08/10/2024 - 19:41
 */
public class KitSetDisclaimerCommand extends BaseCommand {
    @CommandData(name = "kit.setdisclaimer", permission = "alley.kit.setdisclaimer", inGameOnly = false)
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
            sender.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        String disclaimer = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        kit.setDisclaimer(disclaimer);
        kitService.saveKit(kit);
        sender.sendMessage(CC.translate(KitLocale.KIT_DISCLAIMER_SET.getMessage()).replace("{kit-name}", kit.getName()).replace("{disclaimer}", disclaimer));
    }
}