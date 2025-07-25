package dev.revere.alley.game.party.command.impl.leader;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.config.locale.impl.PartyLocale;
import dev.revere.alley.game.party.PartyService;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 29/05/2024 - 19:05
 */
public class PartyKickCommand extends BaseCommand {
    @Override
    @CommandData(name = "party.kick", aliases = "p.kick")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            player.sendMessage(CC.translate("&cUsage: /party kick (player)"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cThe player you are trying to kick is not online."));
            return;
        }

        PartyService partyService = this.plugin.getService(PartyService.class);
        Party party = partyService.getPartyByLeader(player);
        if (party == null) {
            player.sendMessage(CC.translate(PartyLocale.NOT_IN_PARTY.getMessage()));
            return;
        }

        partyService.kickMember(player, target);
    }
}