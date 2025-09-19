package dev.revere.alley.feature.party.command.impl.member;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.party.Party;
import dev.revere.alley.feature.party.PartyService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Alley
 * @date 24/05/2024 - 19:17
 */
public class PartyInfoCommand extends BaseCommand {
    @Override
    @CommandData(
            name = "party.info",
            aliases = {"p.info"},
            usage = "party info",
            description = "View information about your party."
    )
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        PartyService partyService = this.plugin.getService(PartyService.class);
        Party party = partyService.getPartyByMember(player.getUniqueId());

        if (party == null) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ERROR_YOU_NOT_IN_PARTY));
            return;
        }

        UUID leaderUUID = party.getLeader().getUniqueId();

        String members = party.getMembers().stream()
                .filter(uuid -> !uuid.equals(leaderUUID))
                .map(uuid -> this.plugin.getServer().getPlayer(uuid))
                .filter(Objects::nonNull)
                .map(Player::getName)
                .collect(Collectors.joining(", "));

        List<String> info = this.getMessageList(GlobalMessagesLocaleImpl.PARTY_INFO);
        String noMembersFormat = this.getMessage(GlobalMessagesLocaleImpl.PARTY_INFO_NO_MEMBERS_FORMAT);
        for (String line : info) {
            player.sendMessage(CC.translate(line)
                    .replace("{leader}", this.plugin.getServer().getPlayer(leaderUUID).getName())
                    .replace("{privacy}", party.getState().getName())
                    .replace("{size}", String.valueOf(party.getMembers().size() + 1))
                    .replace("{members-amount}", String.valueOf(party.getMembers().size()))
                    .replace("{members}", members.isEmpty() ? noMembersFormat : members));
        }
    }
}