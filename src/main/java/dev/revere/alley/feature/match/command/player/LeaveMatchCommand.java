package dev.revere.alley.feature.match.command.player;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class LeaveMatchCommand extends BaseCommand {
    @CommandData(name = "leave", aliases = {"leavematch", "suicide"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() != ProfileState.PLAYING) {
            player.sendMessage(CC.translate("&cYou are not in a match."));
            return;
        }

        profile.getMatch().handleDisconnect(player);
    }
}