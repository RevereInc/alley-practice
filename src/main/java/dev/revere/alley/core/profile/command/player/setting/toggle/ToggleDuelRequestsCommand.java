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
 * @since 13/07/2025
 */
public class ToggleDuelRequestsCommand extends BaseCommand {
    @CommandData(
            name = "toggleduelrequests",
            cooldown = 1,
            usage = "toggleduelrequests",
            description = "Toggle receiving duel requests."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Profile profile = this.getProfile(player.getUniqueId());
        profile.getProfileData().getSettingData().setReceiveDuelRequestsEnabled(!profile.getProfileData().getSettingData().isReceiveDuelRequestsEnabled());

        player.sendMessage(CC.translate(this.getMessage(ProfileLocaleImpl.TOGGLED_DUEL_REQUESTS)
                .replace("{status}", profile.getProfileData().getSettingData().isReceiveDuelRequestsEnabled() ? "&aenabled" : "&cdisabled"))
        );
    }
}
