package dev.revere.alley.feature.party.command.impl.leader.punishment;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.party.PartyService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 11/12/2024 - 13:33
 */
public class PartyUnbanCommand extends BaseCommand {
    @CommandData(name = "party.unban", aliases = "p.unban")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/party unban &6<player>"));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cThat player is not online."));
            return;
        }

        if (target.equals(player)) {
            player.sendMessage(CC.translate("&cYou cannot unban yourself from the party."));
            return;
        }

        this.plugin.getService(PartyService.class).unbanMember(player, target);
    }
}