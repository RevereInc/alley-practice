package dev.revere.alley.feature.ffa.command.impl.admin.manage;

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
public class FFAListPlayersCommand extends BaseCommand {
    @CommandData(name = "ffa.listplayers", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 1) {
            player.sendMessage(CC.translate("&cUsage: /ffa listplayers <kit>"));
            return;
        }

        String kitName = args[0];
        FFAMatch match = this.plugin.getService(FFAService.class).getFFAMatch(kitName);
        if (match == null) {
            player.sendMessage(CC.translate("&cThere is no FFA match with the name " + kitName + "."));
            return;
        }

        player.sendMessage("");
        player.sendMessage(CC.translate("     &6&l" + match.getKit().getDisplayName() + " Player List &f(" + match.getPlayers().size() + "&f)"));
        if (match.getPlayers().isEmpty()) {
            player.sendMessage(CC.translate("      &6│ &cNo Players available."));
        }
        match.getPlayers().forEach(participant -> player.sendMessage(CC.translate("      &6│ &6" + participant.getName())));
        player.sendMessage("");
    }
}
