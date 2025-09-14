package dev.revere.alley.feature.command.impl.main.impl;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 06/06/2024 - 17:34
 */
public class AlleyReloadCommand extends BaseCommand {

    //TODO: reload gui with options: Config files (messages), menus, Caches(kits, arenas, levels, etc..)

    @CommandData(
            name = "alley.reload",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "alley.reload",
            description = "Reload Alley plugin configurations."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        player.sendMessage(CC.translate("&6&lAlley &freloading..."));
        this.plugin.getService(ConfigService.class).reloadConfigs();
        player.sendMessage(CC.translate("&6&lAlley &a&lreloaded&f."));
    }
}