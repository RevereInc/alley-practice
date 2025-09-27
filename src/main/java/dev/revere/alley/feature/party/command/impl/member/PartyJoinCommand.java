package dev.revere.alley.feature.party.command.impl.member;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.party.PartyService;
import dev.revere.alley.feature.party.Party;
import dev.revere.alley.feature.party.PartyState;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 16/11/2024 - 23:25
 */
public class PartyJoinCommand extends BaseCommand {
    @CommandData(
            name = "party.join",
            aliases = {"p.join"},
            usage = "party join <player>",
            description = "Join a player's party."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cThat player is not online."));
            return;
        }

        Party party = this.plugin.getService(PartyService.class).getPartyByLeader(target);
        if (party == null) {
            player.sendMessage(CC.translate("&cThat player is not in a party."));
            return;
        }

        if (party.getState() != PartyState.PUBLIC) {
            player.sendMessage(CC.translate("&cThat party is not open to the public."));
            return;
        }

        this.plugin.getService(PartyService.class).joinParty(player, target);
    }
}