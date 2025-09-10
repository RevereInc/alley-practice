package dev.revere.alley.feature.kit.command.impl.storage;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.core.locale.internal.types.KitLocaleImpl;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 23/05/2024 - 01:11
 */
public class KitSaveCommand extends BaseCommand {
    @CommandData(
            name = "kit.save",
            isAdminOnly = true,
            usage = "kit save <kitName>",
            description = "Save a kit to the database."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/kit save &6<kitName>"));
            return;
        }

        KitService kitService = this.plugin.getService(KitService.class);
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            player.sendMessage(CC.translate(this.getMessage(KitLocaleImpl.KIT_NOT_FOUND)));
            return;
        }

        kitService.saveKit(kit);
        player.sendMessage(CC.translate(this.getMessage(KitLocaleImpl.KIT_SAVED).replace("{kit-name}", kit.getDisplayName())));
    }
}