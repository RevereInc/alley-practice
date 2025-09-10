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

public class ToggleScoreboardCommand extends BaseCommand {
    @CommandData(
            name = "togglescoreboard",
            aliases = {"tsb", "togglesb"},
            cooldown = 1,
            usage = "togglescoreboard",
            description = "Toggle the scoreboard on or off"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        profile.getProfileData().getSettingData().setScoreboardEnabled(!profile.getProfileData().getSettingData().isScoreboardEnabled());

        player.sendMessage(CC.translate(this.getMessage(ProfileLocaleImpl.TOGGLED_SCOREBOARD).replace("{status}", profile.getProfileData().getSettingData().isScoreboardEnabled() ? "&aenabled" : "&cdisabled")));
    }
}
