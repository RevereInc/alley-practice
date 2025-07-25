package dev.revere.alley.game.party.command.impl.leader.privacy;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.game.party.enums.PartyState;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 16/11/2024 - 23:14
 */
public class PartyOpenCommand extends BaseCommand {
    @CommandData(name = "party.open", aliases = {"p.open"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getParty() == null) {
            player.sendMessage(CC.translate("&cYou are not in a party."));
            return;
        }

        if (!profile.getState().equals(ProfileState.LOBBY)) {
            player.sendMessage(CC.translate("&cYou must be in the lobby to open your party."));
            return;
        }

        profile.getParty().setState(PartyState.PUBLIC);
        player.sendMessage(CC.translate("&aYou have opened your party to the public. Anybody can now join."));
    }
}