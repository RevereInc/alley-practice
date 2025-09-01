package dev.revere.alley.feature.troll.command;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author Remi
 * @project Alley
 * @date 6/19/2024
 */
public class LaunchCommand extends BaseCommand {
    @CommandData(
            name = "launch",
            permission = "alley.command.troll.launch",
            description = "Launch a player",
            usage = "/launch <player> | all"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&6Usage: &e/launch &6<player> &7| &6all"));
            return;
        }

        if (args[0].equalsIgnoreCase("all")) {
            player.getServer().getOnlinePlayers().forEach(target -> target.setVelocity(new Vector(0, 1, 0).multiply(15)));
            player.sendMessage(CC.translate("&fYou've launched all players"));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found"));
            return;
        }

        target.setVelocity(new Vector(0, 1, 0).multiply(15));
        player.sendMessage(CC.translate("&fYou've launched &6" + target.getName()));
    }
}