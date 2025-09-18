package dev.revere.alley.feature.party.command.impl.leader;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.feature.party.PartyService;
import dev.revere.alley.feature.server.ServerService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 22/05/2024 - 20:33
 */
public class PartyCreateCommand extends BaseCommand {
    @Override
    @CommandData(
            name = "party.create",
            aliases = {"p.create"},
            usage = "party create",
            description = "Create a party."
    )
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        UUID playerUUID = player.getUniqueId();

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        PartyService partyService = this.plugin.getService(PartyService.class);
        ServerService serverService = this.plugin.getService(ServerService.class);

        if (profileService.getProfile(playerUUID).getState() != ProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou must be at spawn to execute this command."));
            return;
        }

        if (partyService.getPartyByLeader(player) != null) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.PARTY_ALREADY_IN));
            return;
        }

        if (!serverService.isQueueingAllowed()) {
            player.sendMessage(CC.translate("&cYou cannot create a party while server queueing is disabled."));
            return;
        }

        partyService.createParty(player);
    }
}