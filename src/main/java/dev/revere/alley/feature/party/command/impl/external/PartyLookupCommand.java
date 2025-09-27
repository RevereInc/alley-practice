package dev.revere.alley.feature.party.command.impl.external;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.feature.party.Party;
import dev.revere.alley.feature.party.PartyService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 23/01/2025
 */
public class PartyLookupCommand extends BaseCommand {
    @CommandData(
            name = "party.lookup",
            usage = "party lookup <player>",
            description = "Sends info about a specific party",
            cooldown = 1
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        Player target = this.plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        Party party = this.plugin.getService(PartyService.class).getParty(target);
        if (party == null) {
            player.sendMessage(CC.translate("&cThis player is not in a party."));
            return;
        }

        List<String> message = this.getStringList(GlobalMessagesLocaleImpl.PARTY_LOOKUP);
        message.replaceAll(line -> line
                .replace("{name-color}", String.valueOf(this.getProfile(party.getLeader().getUniqueId()).getNameColor()))
                .replace("{leader}", party.getLeader().getName())
                .replace("{members}", String.valueOf(party.getMembers().size()))
                .replace("{status}", party.getState().getName())
                .replace("{privacy-desc}", party.getState().getDescription())
                .replace("{privacy}", party.getState().getName())
        );

        message.forEach(line -> player.sendMessage(CC.translate(line)));
    }
}