package dev.revere.alley.feature.troll.command;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.troll.TrollService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 28/10/2024 - 09:00
 */
public class TrollCommand extends BaseCommand {
    @Override
    @CommandData(
            name = "troll",
            aliases = "playertroll",
            inGameOnly = false,
            permission = "alley.command.troll.demo-menu"
    )
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&6Usage: &e/troll &6(player)"));
            return;
        }

        Player targetPlayer = this.plugin.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            sender.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        this.plugin.getService(TrollService.class).openDemoMenu(targetPlayer);
        if (sender == targetPlayer) {
            sender.sendMessage(CC.translate("&cWHY IN THE WORLD WOULD YOU TROLL YOURSELF?!"));
        } else {
            sender.sendMessage(CC.translate("&eYou have trolled &d" + targetPlayer.getName() + "&e."));
        }
    }
}