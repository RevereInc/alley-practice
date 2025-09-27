package dev.revere.alley.core.profile.command.player.setting.toggle;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 03/03/2025
 */
public class ToggleScoreboardLinesCommand extends BaseCommand {
    @CommandData(
            name = "togglescoreboardlines",
            aliases = "tsl",
            cooldown = 1,
            usage = "togglescoreboardlines",
            description = "Toggle the scoreboard lines."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = this.getProfile(player.getUniqueId());
        profile.getProfileData().getSettingData().setShowScoreboardLines(!profile.getProfileData().getSettingData().isShowScoreboardLines());

        player.sendMessage(CC.translate(this.getString(GlobalMessagesLocaleImpl.PROFILE_TOGGLED_SCOREBOARD_LINES)
                .replace("{status}", profile.getProfileData().getSettingData().isShowScoreboardLines() ? "&aenabled" : "&cdisabled"))
        );
    }
}