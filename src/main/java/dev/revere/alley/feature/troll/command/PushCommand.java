package dev.revere.alley.feature.troll.command;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 25/06/2024 - 20:26
 */
public class PushCommand extends BaseCommand {
    @CommandData(name = "push", permission = "alley.command.troll.push", usage = "push <player> <value>", description = "Push a player")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/push &6<player> <value>"));
            return;
        }

        String targetName = args[0];
        Player target = player.getServer().getPlayer(targetName);

        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        double value;

        try {
            value = Double.parseDouble(args[1]);
        } catch (NumberFormatException exception) {
            player.sendMessage(CC.translate("&cInvalid number."));
            return;
        }

        if (value > 10) {
            player.sendMessage(CC.translate("&cValue cannot be greater than 10."));
            return;
        }

        target.setVelocity(player.getLocation().getDirection().multiply(value));

        player.sendMessage(CC.translate("&fYou've pushed &6" + target.getName()));
        target.sendMessage(CC.translate("&fYou've been pushed by &6" + player.getName()));
    }
}