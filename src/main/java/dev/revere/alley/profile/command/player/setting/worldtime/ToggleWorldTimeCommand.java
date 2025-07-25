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
 * @date 13/10/2024 - 10:25
 */
public class ToggleWorldTimeCommand extends BaseCommand {
    @CommandData(name = "toggleworldtime")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        switch (profile.getProfileData().getSettingData().getWorldTime()) {
            case DEFAULT:
                profile.getProfileData().getSettingData().setTimeDay(player);
                player.sendMessage(CC.translate("&aYou have set the time to day."));
                break;
            case DAY:
                profile.getProfileData().getSettingData().setTimeSunset(player);
                player.sendMessage(CC.translate("&aYou have set the time to sunset."));
                break;
            case SUNSET:
                profile.getProfileData().getSettingData().setTimeNight(player);
                player.sendMessage(CC.translate("&aYou have set the time to night."));
                break;
            case NIGHT:
                profile.getProfileData().getSettingData().setTimeDefault(player);
                player.sendMessage(CC.translate("&aYou have reset your world time."));
                break;
        }
    }
}
