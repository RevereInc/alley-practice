package dev.revere.alley.feature.ffa.command.impl.admin.data;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.ffa.FFAMatch;
import dev.revere.alley.feature.ffa.FFAService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public class FFAMaxPlayersCommand extends BaseCommand {
    @CommandData(
            name = "ffa.maxplayers",
            isAdminOnly = true,
            usage = "ffa maxplayers <kit> <maxPlayers>",
            description = "Set the max players for a FFA kit."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 2) {
            command.sendUsage();
            return;
        }

        String kitName = args[0];
        int maxPlayers = Integer.parseInt(args[1]);

        FFAMatch match = this.plugin.getService(FFAService.class).getFFAMatch(kitName);
        if (match == null) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.FFA_NOT_FOUND).replace("{ffa-name}", kitName));
            return;
        }

        match.setMaxPlayers(maxPlayers);
        player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.FFA_MAX_PLAYERS_SET)
                .replace("{kit-name}", kitName)
                .replace("{max-players}", String.valueOf(maxPlayers))
        );
    }
}