package dev.revere.alley.feature.party.command.impl.member;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.feature.party.Party;
import dev.revere.alley.feature.party.PartyRequest;
import dev.revere.alley.feature.party.PartyService;
import dev.revere.alley.feature.party.PartyState;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 18:33
 */
public class PartyAcceptCommand extends BaseCommand {
    @Override
    @CommandData(
            name = "party.accept",
            aliases = "p.accept",
            usage = "party accept <player>",
            description = "Accept a party invite."
    )
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            player.sendMessage(CC.translate("&cUsage: /party accept <player>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(CC.translate("&cThe player you are trying to join is not online."));
            return;
        }

        PartyService partyService = this.plugin.getService(PartyService.class);

        Party party = partyService.getPartyByLeader(target);
        if (party == null) {
            player.sendMessage(CC.translate("&cThe player you are trying to join does not have a party."));
            return;
        }

        if (party.getState() == PartyState.PUBLIC) {
            partyService.joinParty(player, target);
            return;
        }

        PartyRequest partyRequest = partyService.getRequest(player);
        if (partyRequest == null || !partyRequest.getSender().equals(target)) {
            player.sendMessage(CC.translate(this.getMessage(GlobalMessagesLocaleImpl.ERROR_YOU_NO_PARTY_INVITE_FROM_PLAYER)
                    .replace("{name-color}", String.valueOf(this.getProfile(target.getUniqueId()).getNameColor()))
                    .replace("{player}", target.getName()))
            );
            return;
        }

        if (partyRequest.hasExpired()) {
            partyService.removeRequest(partyRequest);
            player.sendMessage(CC.translate("&cThe party request has expired."));
            return;
        }

        partyService.joinParty(player, target);
        partyService.removeRequest(partyRequest);
    }
}