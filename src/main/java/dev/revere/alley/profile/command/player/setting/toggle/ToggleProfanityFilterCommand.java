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
 * @since 27/04/2025
 */
public class ToggleProfanityFilterCommand extends BaseCommand {
    @CommandData(name = "toggleprofanityfilter", aliases = {"tpf"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        profile.getProfileData().getSettingData().setProfanityFilterEnabled(!profile.getProfileData().getSettingData().isProfanityFilterEnabled());

        player.sendMessage(CC.translate(ProfileLocale.TOGGLED_PROFANITY_FILTER.getMessage().replace("{status}", profile.getProfileData().getSettingData().isProfanityFilterEnabled() ? "&aenabled" : "&cdisabled")));
    }
}