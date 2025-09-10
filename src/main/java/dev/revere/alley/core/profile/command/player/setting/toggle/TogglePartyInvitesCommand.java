package dev.revere.alley.core.profile.command.player.setting.toggle;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.locale.internal.types.ProfileLocaleImpl;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 23:35
 */

public class TogglePartyInvitesCommand extends BaseCommand {
    @Override
    @CommandData(
            name = "togglepartyinvites",
            cooldown = 1,
            usage = "togglepartyinvites",
            description = "Toggle party invites on or off."
    )
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        profile.getProfileData().getSettingData().setPartyInvitesEnabled(!profile.getProfileData().getSettingData().isPartyInvitesEnabled());

        player.sendMessage(CC.translate(this.getMessage(ProfileLocaleImpl.TOGGLED_PARTY_INVITES).replace("{status}", profile.getProfileData().getSettingData().isPartyInvitesEnabled() ? "&aenabled" : "&cdisabled")));
    }
}
