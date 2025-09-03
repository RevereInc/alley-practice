package dev.revere.alley.feature.command.impl.main.impl;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 06/06/2024 - 17:34
 */
public class AlleyReloadCommand extends BaseCommand {
    @Override
    @CommandData(
            name = "alley.reload",
            isAdminOnly = true,
            inGameOnly = false
    )
    public void onCommand(CommandArgs command) {
        this.plugin.getService(ConfigService.class).reloadConfigs(command.getSender(), true);
    }
}