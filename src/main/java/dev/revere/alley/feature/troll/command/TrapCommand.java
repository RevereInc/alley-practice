package dev.revere.alley.feature.troll.command;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.common.time.TimeUtil;
import dev.revere.alley.feature.troll.TrollService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 01/09/2025
 */
public class TrapCommand extends BaseCommand {
    @CommandData(
            name = "trap",
            permission = "alley.command.troll.trap",
            usage = "trap <player> [duration]",
            description = "Trap a player in a confined space"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/trap <player> [duration]"));
            return;
        }

        String targetName = args[0];
        Player targetPlayer = this.plugin.getServer().getPlayer(targetName);
        if (targetPlayer == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        long durationMillis = -1;
        if (args.length >= 2) {
            String durationString = args[1];
            durationMillis = TimeUtil.parseTime(durationString);

            if (durationMillis <= 0) {
                player.sendMessage(CC.translate("&cInvalid duration. Please use a format like '30s', '5m', or '1h'."));
                return;
            }
        }

        this.plugin.getService(TrollService.class).trapPlayer(player, targetPlayer, durationMillis);

        if (durationMillis > 0) {
            String formattedTime = TimeUtil.millisToRoundedTime(durationMillis);
            player.sendMessage(CC.translate("&fYou've trapped &6" + targetPlayer.getName() + "&f for &6" + formattedTime + "&f."));
        } else {
            player.sendMessage(CC.translate("&fYou've trapped &6" + targetPlayer.getName() + "&f forever."));
        }
    }
}