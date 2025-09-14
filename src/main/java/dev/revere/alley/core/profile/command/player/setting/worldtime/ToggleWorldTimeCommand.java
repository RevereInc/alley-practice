package dev.revere.alley.core.profile.command.player.setting.worldtime;

import dev.revere.alley.core.locale.internal.impl.ProfileLocaleImpl;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 13/10/2024 - 10:25
 */
public class ToggleWorldTimeCommand extends BaseCommand {
    @CommandData(
            name = "toggleworldtime",
            cooldown = 1,
            usage = "toggleworldtime",
            description = "Toggle through your world time settings."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = this.getProfile(player.getUniqueId());

        switch (profile.getProfileData().getSettingData().getWorldTime()) {
            case DEFAULT:
                profile.getProfileData().getSettingData().setTimeDay(player);
                player.sendMessage(this.getMessage(ProfileLocaleImpl.WORLD_TIME_SET).replace("{time}", profile.getProfileData().getSettingData().getTime().toLowerCase()));
                break;
            case DAY:
                profile.getProfileData().getSettingData().setTimeSunset(player);
                player.sendMessage(this.getMessage(ProfileLocaleImpl.WORLD_TIME_SET).replace("{time}", profile.getProfileData().getSettingData().getTime().toLowerCase()));
                break;
            case SUNSET:
                profile.getProfileData().getSettingData().setTimeNight(player);
                player.sendMessage(this.getMessage(ProfileLocaleImpl.WORLD_TIME_SET).replace("{time}", profile.getProfileData().getSettingData().getTime().toLowerCase()));
                break;
            case NIGHT:
                profile.getProfileData().getSettingData().setTimeDefault(player);
                player.sendMessage(this.getMessage(ProfileLocaleImpl.WORLD_TIME_SET).replace("{time}", profile.getProfileData().getSettingData().getTime().toLowerCase()));
                break;
        }
    }
}
