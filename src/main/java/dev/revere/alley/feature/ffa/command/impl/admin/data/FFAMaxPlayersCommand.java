package dev.revere.alley.feature.ffa.command.impl.admin.data;

import dev.revere.alley.core.config.internal.locale.impl.FFALocale;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.ffa.FFAMatch;
import dev.revere.alley.feature.ffa.FFAService;
import dev.revere.alley.common.text.CC;
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
            player.sendMessage(FFALocale.NOT_FOUND.getMessage().replace("{ffa-name}", kitName));
            return;
        }

        match.setMaxPlayers(maxPlayers);
        player.sendMessage(FFALocale.MAX_PLAYERS_SET.getMessage()
                .replace("{kit-name}", kitName)
                .replace("{max-players}", String.valueOf(maxPlayers))
        );
    }
}
