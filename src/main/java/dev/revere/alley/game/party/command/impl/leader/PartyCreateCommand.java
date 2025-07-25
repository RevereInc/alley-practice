package dev.revere.alley.game.party.command.impl.leader;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.server.ServerService;
import dev.revere.alley.config.locale.impl.PartyLocale;
import dev.revere.alley.game.party.PartyService;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 22/05/2024 - 20:33
 */
public class PartyCreateCommand extends BaseCommand {
    @Override
    @CommandData(name = "party.create", aliases = {"p.create"})
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
            player.sendMessage(CC.translate(PartyLocale.ALREADY_IN_PARTY.getMessage()));
            return;
        }

        if (!serverService.isQueueingAllowed()) {
            player.sendMessage(CC.translate("&cYou cannot create a party while server queueing is disabled."));
            return;
        }

        partyService.createParty(player);
        //player.sendMessage(CC.translate(PartyLocale.PARTY_CREATED.getMessage()));
    }
}