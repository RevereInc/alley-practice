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
 * @date 02/06/2024 - 11:05
 */
public class SunsetCommand extends BaseCommand {
    @CommandData(
            name = "sunset",
            aliases = "sunrise",
            cooldown = 1,
            usage = "sunset",
            description = "Set your personal world time to sunset."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = this.getProfile(player.getUniqueId());

        profile.getProfileData().getSettingData().setTimeSunset(player);
        player.sendMessage(this.getMessage(ProfileLocaleImpl.WORLD_TIME_SET)
                .replace("{time}", profile.getProfileData().getSettingData().getTime().toLowerCase())
        );
    }
}