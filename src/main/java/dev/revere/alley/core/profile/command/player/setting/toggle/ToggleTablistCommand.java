package dev.revere.alley.core.profile.command.player.setting.toggle;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.internal.locale.impl.ProfileLocale;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 23:35
 */

public class ToggleTablistCommand extends BaseCommand {
    @Override
    @CommandData(name = "toggletablist")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        profile.getProfileData().getSettingData().setTablistEnabled(!profile.getProfileData().getSettingData().isTablistEnabled());

        player.sendMessage(CC.translate(ProfileLocale.TOGGLED_TABLIST.getMessage().replace("{status}", profile.getProfileData().getSettingData().isTablistEnabled() ? "&aenabled" : "&cdisabled")));
    }
}
