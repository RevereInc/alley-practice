package dev.revere.alley.feature.troll.command;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 28/10/2024 - 09:09
 */
public class HeartAttackCommand extends BaseCommand {
    @CommandData(
            name = "heartattack",
            usage = "heartattack <player>",
            description = "Give a player a heart attack (0.5 health)",
            permission = "alley.command.troll.heartattack"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/heartattack &6<player>"));
            return;
        }

        String targetName = args[0];
        Player target = player.getServer().getPlayer(targetName);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        target.setHealth(0.5D);
        player.sendMessage(CC.translate("&fYou've given &6" + target.getName() + " &fa heart attack."));
    }
}