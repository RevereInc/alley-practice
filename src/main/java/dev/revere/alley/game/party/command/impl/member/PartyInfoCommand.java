package dev.revere.alley.game.party.command.impl.member;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.config.locale.impl.PartyLocale;
import dev.revere.alley.game.party.PartyService;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.util.chat.CC;
import org.bukkit.configuration.file.FileConfiguration;
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
    @CommandData(name = "party.info", aliases = {"p.info"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        PartyService partyService = this.plugin.getService(PartyService.class);
        Party party = partyService.getPartyByMember(player.getUniqueId());

        if (party == null) {
            player.sendMessage(CC.translate(PartyLocale.NOT_IN_PARTY.getMessage()));
            return;
        }

        UUID leaderUUID = party.getLeader().getUniqueId();

        String members = party.getMembers().stream()
                .filter(uuid -> !uuid.equals(leaderUUID))
                .map(uuid -> this.plugin.getServer().getPlayer(uuid))
                .filter(Objects::nonNull)
                .map(Player::getName)
                .collect(Collectors.joining(", "));

        FileConfiguration config = this.plugin.getService(ConfigService.class).getMessagesConfig();
        List<String> info = config.getStringList("party.info-command.text");
        String noMembersFormat = CC.translate(config.getString("party.info-command.no-members-format"));

        for (String line : info) {
            player.sendMessage(CC.translate(line)
                    .replace("{leader}", this.plugin.getServer().getPlayer(leaderUUID).getName())
                    .replace("{members}", members.isEmpty() ? noMembersFormat : members));
        }
    }
}
