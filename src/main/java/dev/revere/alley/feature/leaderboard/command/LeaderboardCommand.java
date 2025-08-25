package dev.revere.alley.feature.leaderboard.command;

import dev.revere.alley.feature.leaderboard.menu.LeaderboardMenu;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 19/05/2024 - 11:29
 */
public class LeaderboardCommand extends BaseCommand {
    @Override
    @CommandData(name = "leaderboard", aliases = {"leaderboards", "lb"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        new LeaderboardMenu().openMenu(player);
    }
}
