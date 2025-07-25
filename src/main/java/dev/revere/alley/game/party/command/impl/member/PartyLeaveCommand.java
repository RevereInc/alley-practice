package dev.revere.alley.game.party.command.impl.member;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.config.locale.impl.PartyLocale;
import dev.revere.alley.game.party.PartyService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 22/05/2024 - 20:36
 */
public class PartyLeaveCommand extends BaseCommand {
    @Override
    @CommandData(name = "party.leave", aliases = {"p.leave"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        UUID playerUUID = player.getUniqueId();

        PartyService partyService = this.plugin.getService(PartyService.class);
        if (partyService.getPartyByLeader(player) != null) {
            partyService.disbandParty(player);
            //player.sendMessage(CC.translate(PartyLocale.PARTY_DISBANDED.getMessage()));
            return;
        }

        if (partyService.getPartyByMember(playerUUID) != null) {
            partyService.leaveParty(player);
            player.sendMessage(CC.translate(PartyLocale.PARTY_LEFT.getMessage()));
            return;
        }

        player.sendMessage(CC.translate(PartyLocale.NOT_IN_PARTY.getMessage()));
    }
}