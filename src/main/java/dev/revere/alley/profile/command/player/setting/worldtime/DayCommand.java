package dev.revere.alley.profile.command.player.setting.worldtime;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 02/06/2024 - 10:57
 */
public class DayCommand extends BaseCommand {
    @Override
    @CommandData(name = "day")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        profile.getProfileData().getSettingData().setTimeDay(player);
        player.sendMessage(CC.translate("&aYou have set the time to day."));
    }
}
