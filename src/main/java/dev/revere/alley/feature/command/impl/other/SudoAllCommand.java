package dev.revere.alley.feature.command.impl.other;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project alley-practice
 * @date 22/06/2025
 */
public class SudoAllCommand extends BaseCommand {
    @CommandData(name = "sudoall", isAdminOnly = true, permission = "alley.admin.sudoall")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /sudoall <message>"));
            return;
        }

        String message = String.join(" ", args);
        for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
            onlinePlayer.chat(message);
        }

        player.sendMessage(CC.translate("&aSuccessfully sent message to all players: " + message));
    }
}
