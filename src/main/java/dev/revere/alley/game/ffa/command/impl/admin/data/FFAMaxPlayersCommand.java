package dev.revere.alley.game.ffa.command.impl.admin.data;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.game.ffa.FFAMatch;
import dev.revere.alley.game.ffa.FFAService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public class FFAMaxPlayersCommand extends BaseCommand {
    @CommandData(name = "ffa.maxplayers", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 2) {
            player.sendMessage(CC.translate("&cUsage: /ffa maxplayers <kit> <maxPlayers>"));
            return;
        }

        String kitName = args[0];
        int maxPlayers = Integer.parseInt(args[1]);

        FFAMatch match = this.plugin.getService(FFAService.class).getFFAMatch(kitName);
        if (match == null) {
            player.sendMessage(CC.translate("&cThere is no FFA match with the name " + kitName + "."));
            return;
        }

        match.setMaxPlayers(maxPlayers);
        player.sendMessage(CC.translate("&aSuccessfully set the max players for the FFA match."));
    }
}
