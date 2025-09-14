package dev.revere.alley.feature.kit.command.impl.storage;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.command.KitLocaleImpl;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 23/05/2024 - 01:18
 */
public class KitSaveAllCommand extends BaseCommand {
    @CommandData(
            name = "kit.saveall",
            isAdminOnly = true,
            usage = "kit saveall",
            description = "Save all kits to storage."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        this.plugin.getService(KitService.class).saveKits();
        player.sendMessage(CC.translate(this.getMessage(KitLocaleImpl.SAVED_ALL)));
    }
}