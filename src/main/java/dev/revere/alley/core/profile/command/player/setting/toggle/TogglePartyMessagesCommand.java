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
 * @date 25/05/2024 - 23:35
 */

public class TogglePartyMessagesCommand extends BaseCommand {
    @Override
    @CommandData(
            name = "togglepartymessages",
            cooldown = 1,
            usage = "togglepartymessages",
            description = "Toggle party messages on or off."
    )
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = this.getProfile(player.getUniqueId());
        profile.getProfileData().getSettingData().setPartyMessagesEnabled(!profile.getProfileData().getSettingData().isPartyMessagesEnabled());

        player.sendMessage(CC.translate(this.getMessage(GlobalMessagesLocaleImpl.PROFILE_TOGGLED_PARTY_MESSAGES)
                .replace("{status}", profile.getProfileData().getSettingData().isPartyMessagesEnabled() ? "&aenabled" : "&cdisabled"))
        );
    }
}
