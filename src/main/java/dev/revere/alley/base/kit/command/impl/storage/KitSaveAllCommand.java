package dev.revere.alley.base.kit.command.impl.storage;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.kit.KitService;
import dev.revere.alley.config.locale.impl.KitLocale;
import dev.revere.alley.util.chat.CC;
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