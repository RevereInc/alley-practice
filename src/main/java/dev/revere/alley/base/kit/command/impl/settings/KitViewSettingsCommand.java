package dev.revere.alley.base.kit.command.impl.settings;

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
 * @date 08/10/2024 - 20:04
 */
public class KitViewSettingsCommand extends BaseCommand {
    @CommandData(name = "kit.viewsettings", isAdminOnly = true, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&6Usage: &e/kit viewsettings &6<kitName>"));
            return;
        }

        Kit kit = this.plugin.getService(KitService.class).getKit(args[0]);
        if (kit == null) {
            sender.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&6&lKit Settings for " + kit.getName()));
        kit.getKitSettings().forEach(setting -> sender.sendMessage(CC.translate(" &f● &6" + setting.getName() + " &f(" + (setting.isEnabled() ? "&aEnabled" : "&cDisabled") + "&f)")));
        sender.sendMessage("");
    }
}