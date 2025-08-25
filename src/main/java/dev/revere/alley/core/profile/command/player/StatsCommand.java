package dev.revere.alley.core.profile.command.player;

import dev.revere.alley.common.PlayerUtil;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.profile.menu.statistic.StatisticsMenu;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 17/11/2024 - 12:07
 */
public class StatsCommand extends BaseCommand {
    @CommandData(name = "stats", aliases = {"statistics"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            new StatisticsMenu(player).openMenu(player);
            return;
        }

        Player onlineTarget = Bukkit.getPlayerExact(args[0]);
        OfflinePlayer target = onlineTarget != null ? onlineTarget : PlayerUtil.getOfflinePlayerByName(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        new StatisticsMenu(target).openMenu(player);
    }
}