package dev.revere.alley.feature.party.command.impl.leader;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.internal.locale.impl.PartyLocale;
import dev.revere.alley.feature.party.PartyService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 22/05/2024 - 20:36
 */
public class PartyDisbandCommand extends BaseCommand {
    @Override
    @CommandData(name = "party.disband", aliases = {"p.disband"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        PartyService partyService = this.plugin.getService(PartyService.class);
        if (partyService.getPartyByLeader(player) != null) {
            partyService.disbandParty(player);
            //player.sendMessage(CC.translate(PartyLocale.PARTY_DISBANDED.getMessage()));
            return;
        }

        player.sendMessage(CC.translate(PartyLocale.NOT_LEADER.getMessage()));
    }
}
