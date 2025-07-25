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
 * @project alley-practice
 * @since 13/07/2025
 */
public class ToggleDuelRequestsCommand extends BaseCommand {
    @Override
    @CommandData(name = "toggleduelrequests")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        profile.getProfileData().getSettingData().setReceiveDuelRequestsEnabled(!profile.getProfileData().getSettingData().isReceiveDuelRequestsEnabled());

        player.sendMessage(CC.translate(ProfileLocale.TOGGLED_DUEL_REQUESTS.getMessage().replace("{status}", profile.getProfileData().getSettingData().isReceiveDuelRequestsEnabled() ? "&aenabled" : "&cdisabled")));
    }
}
