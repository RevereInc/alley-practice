package dev.revere.alley.feature.server.command.impl;

import dev.revere.alley.feature.server.menu.ServiceMenu;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 09/03/2025
 */
public class ServiceMenuCommand extends BaseCommand {
    @CommandData(name = "service.menu", isAdminOnly = true, usage = "/service menu", description = "Service menu command.")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        new ServiceMenu().openMenu(player);
    }
}
