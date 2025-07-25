package dev.revere.alley.profile.command.player.setting.toggle;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.config.locale.impl.ProfileLocale;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 23:35
 */

public class TogglePartyInvitesCommand extends BaseCommand {
    @Override
    @CommandData(name = "togglepartyinvites")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        profile.getProfileData().getSettingData().setPartyInvitesEnabled(!profile.getProfileData().getSettingData().isPartyInvitesEnabled());

        player.sendMessage(CC.translate(ProfileLocale.TOGGLED_PARTY_INVITES.getMessage().replace("{status}", profile.getProfileData().getSettingData().isPartyInvitesEnabled() ? "&aenabled" : "&cdisabled")));
    }
}
