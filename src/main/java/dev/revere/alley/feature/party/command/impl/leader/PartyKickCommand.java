package dev.revere.alley.feature.party.command.impl.leader;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.feature.party.Party;
import dev.revere.alley.feature.party.PartyService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 29/05/2024 - 19:05
 */
public class PartyKickCommand extends BaseCommand {
    @Override
    @CommandData(
            name = "party.kick",
            aliases = "p.kick",
            usage = "party kick <player>",
            description = "Kick a player from your party."
    )
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            command.sendUsage();
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        PartyService partyService = this.plugin.getService(PartyService.class);
        Party party = partyService.getPartyByLeader(player);
        if (party == null) {
            player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.ERROR_YOU_NOT_IN_PARTY)));
            return;
        }

        partyService.kickMember(player, target);
    }
}