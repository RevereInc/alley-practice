package dev.revere.alley.game.match.command.player;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
public class LeaveSpectatorCommand extends BaseCommand {
    @CommandData(name = "leavespectator", aliases = {"unspec"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.SPECTATING) {
            player.sendMessage(CC.translate("&cYou are not spectating a match."));
            return;
        }

        if (profile.getFfaMatch() != null) {
            profile.getFfaMatch().removeSpectator(player);
            return;
        }

        profile.getMatch().removeSpectator(player, true);
    }
}
