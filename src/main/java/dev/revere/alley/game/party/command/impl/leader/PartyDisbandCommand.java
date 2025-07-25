package dev.revere.alley.game.party.command.impl.leader;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.config.locale.impl.PartyLocale;
import dev.revere.alley.game.party.PartyService;
import dev.revere.alley.util.chat.CC;
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
