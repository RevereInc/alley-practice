package dev.revere.alley.feature.kit.command.impl.data;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.internal.locale.impl.KitLocale;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Alley
 * @since 30/06/2025
 */
public class KitResetLayoutsCommand extends BaseCommand {
    @CommandData(name = "kit.resetlayouts", isAdminOnly = true, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&6Usage: &e/kit resetlayouts &b<kitName>"));
            return;
        }

        String kitName = args[0];
        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(kitName);
        if (kit == null) {
            sender.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        this.plugin.getService(ProfileService.class).resetLayoutForKit(kit);
    }
}