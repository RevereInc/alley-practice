package dev.revere.alley.game.party.command.impl.leader.punishment;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.game.party.PartyService;
import dev.revere.alley.util.chat.CC;
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