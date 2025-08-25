package dev.revere.alley.feature.kit.command.impl.storage;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.internal.locale.impl.KitLocale;
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
    @CommandData(name = "kit.saveall", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        this.plugin.getService(KitService.class).saveKits();
        player.sendMessage(CC.translate(KitLocale.KIT_SAVED_ALL.getMessage()));
    }
}