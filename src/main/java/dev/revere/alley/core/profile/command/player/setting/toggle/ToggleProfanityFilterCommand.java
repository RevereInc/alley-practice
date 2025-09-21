package dev.revere.alley.core.profile.command.player.setting.toggle;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.SettingsLocaleImpl;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 27/04/2025
 */
public class ToggleProfanityFilterCommand extends BaseCommand {
    @CommandData(
            name = "toggleprofanityfilter",
            aliases = {"tpf"},
            cooldown = 1,
            usage = "toggleprofanityfilter",
            description = "Toggle the profanity filter on or off."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = this.getProfile(player.getUniqueId());
        profile.getProfileData().getSettingData().setProfanityFilterEnabled(!profile.getProfileData().getSettingData().isProfanityFilterEnabled());

        boolean chatFormatEnabled = this.getBoolean(SettingsLocaleImpl.SERVER_CHAT_FORMAT_ENABLED_BOOLEAN);
        if (!chatFormatEnabled) {
            player.sendMessage(CC.translate("&cThis feature is currently disabled on the server."));
            if (player.isOp()) {
                player.sendMessage(CC.translate("&cYou seem to be an operator. This feature is disabled because you're currently &c&lnot &cusing &6&lAlley's &cchat format. &7To enable it, head to settings config and enable chat format."));
            }
            return;
        }

        player.sendMessage(CC.translate(this.getMessage(GlobalMessagesLocaleImpl.PROFILE_TOGGLED_PROFANITY_FILTER)
                .replace("{status}", profile.getProfileData().getSettingData().isProfanityFilterEnabled() ? "&aenabled" : "&cdisabled"))
        );
    }
}