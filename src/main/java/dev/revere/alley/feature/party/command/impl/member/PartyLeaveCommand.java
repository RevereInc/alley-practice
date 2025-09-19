package dev.revere.alley.feature.party.command.impl.member;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.party.PartyService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 22/05/2024 - 20:36
 */
public class PartyLeaveCommand extends BaseCommand {
    @Override
    @CommandData(
            name = "party.leave",
            aliases = {"p.leave"},
            usage = "party leave",
            description = "Leave your party."
    )
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
            player.sendMessage(CC.translate(this.getMessage(GlobalMessagesLocaleImpl.PARTY_LEFT)));
            return;
        }

        player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ERROR_YOU_NOT_IN_PARTY));
    }
}