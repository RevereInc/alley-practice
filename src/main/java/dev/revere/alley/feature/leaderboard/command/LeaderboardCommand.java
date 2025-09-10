package dev.revere.alley.feature.leaderboard.command;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.leaderboard.menu.LeaderboardMenu;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 19/05/2024 - 11:29
 */
public class LeaderboardCommand extends BaseCommand {
    @CommandData(
            name = "leaderboard",
            aliases = {"leaderboards", "lb"},
            usage = "leaderboard",
            description = "View the global leaderboards."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        new LeaderboardMenu().openMenu(player);
    }
}
