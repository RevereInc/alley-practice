package dev.revere.alley.core.profile.command.player.setting.worldtime;

import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 02/06/2024 - 11:02
 */
public class NightCommand extends BaseCommand {
    @CommandData(
            name = "night",
            cooldown = 1,
            usage = "night",
            description = "Set your personal world time to night."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = this.getProfile(player.getUniqueId());

        profile.getProfileData().getSettingData().setTimeNight(player);
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.PROFILE_WORLD_TIME_SET)
                .replace("{time}", profile.getProfileData().getSettingData().getTime().toLowerCase())
        );
    }
}