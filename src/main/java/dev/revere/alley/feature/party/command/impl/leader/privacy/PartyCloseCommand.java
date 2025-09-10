package dev.revere.alley.feature.party.command.impl.leader.privacy;

import dev.revere.alley.core.locale.internal.types.ErrorLocaleImpl;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.party.PartyState;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 16/11/2024 - 23:16
 */
public class PartyCloseCommand extends BaseCommand {
    @CommandData(
            name = "party.close",
            aliases = {"p.close"},
            usage = "party close",
            description = "Close your party to new members."
    )
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
            player.sendMessage(this.getMessage(ErrorLocaleImpl.MUST_BE_IN_LOBBY));
            return;
        }

        profile.getParty().setState(PartyState.PRIVATE);
        player.sendMessage(CC.translate("&aYou have locked your party. Nobody can join unless invited."));
    }
}