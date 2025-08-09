package dev.revere.alley.feature.match.command.player;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.common.text.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
public class SpectateCommand extends BaseCommand {
    @CommandData(name = "spectate", aliases = {"spec"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&6Usage: &e/spectate &6<player>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cThat player is not online."));
            return;
        }

        if (player.equals(target)) {
            player.sendMessage(CC.translate("&cYou cannot spectate yourself."));
            return;
        }

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.LOBBY && profile.getState() != ProfileState.TOURNAMENT_LOBBY) {
            player.sendMessage(CC.translate("&cYou can only spectate while in a lobby."));
            return;
        }

        Profile targetProfile = this.plugin.getService(ProfileService.class).getProfile(target.getUniqueId());
        if (targetProfile.getState() != ProfileState.PLAYING) {
            player.sendMessage(CC.translate("&cThat player is not currently in a match."));
            return;
        }

        if (targetProfile.getFfaMatch() != null && profile.getState() == ProfileState.TOURNAMENT_LOBBY) {
            player.sendMessage(CC.translate("&cTournament players cannot spectate FFA matches."));
            return;
        }

        if (targetProfile.getFfaMatch() != null) {
            targetProfile.getFfaMatch().addSpectator(player);
        } else if (targetProfile.getMatch() != null) {
            targetProfile.getMatch().addSpectator(player);
        } else {
            player.sendMessage(CC.translate("&cYou are unable to spectate that player."));
        }
    }
}