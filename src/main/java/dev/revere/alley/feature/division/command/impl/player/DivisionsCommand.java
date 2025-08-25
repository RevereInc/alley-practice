package dev.revere.alley.feature.division.command.impl.player;

import dev.revere.alley.feature.division.menu.DivisionsMenu;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 6/2/2024
 */
public class DivisionsCommand extends BaseCommand {
    @CommandData(name = "divisions", aliases = {"division.menu"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        new DivisionsMenu().openMenu(player);
    }
}
