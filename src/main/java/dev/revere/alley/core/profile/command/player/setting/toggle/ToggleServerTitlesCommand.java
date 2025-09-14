package dev.revere.alley.core.profile.command.player.setting.toggle;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.ProfileLocaleImpl;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 19/07/2025
 */
public class ToggleServerTitlesCommand extends BaseCommand {
    @CommandData(
            name = "toggleservertitles",
            cooldown = 1,
            usage = "toggleservertitles",
            description = "Toggle the server titles on or off"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = this.getProfile(player.getUniqueId());
        profile.getProfileData().getSettingData().setServerTitles(!profile.getProfileData().getSettingData().isServerTitles());

        player.sendMessage(CC.translate(this.getMessage(ProfileLocaleImpl.TOGGLED_SERVER_TITLES)
                .replace("{status}", profile.getProfileData().getSettingData().isServerTitles() ? "&aenabled" : "&cdisabled"))
        );
    }
}