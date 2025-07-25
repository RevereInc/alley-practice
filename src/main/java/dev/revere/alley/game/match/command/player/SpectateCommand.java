package dev.revere.alley.game.match.command.player;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.util.chat.CC;
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

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou can only spectate players in the lobby."));
            return;
        }

        Profile targetProfile = this.plugin.getService(ProfileService.class).getProfile(target.getUniqueId());
        if (targetProfile.getFfaMatch() != null) {
            targetProfile.getFfaMatch().addSpectator(player);
            return;
        }

        if (targetProfile.getState() != ProfileState.PLAYING) {
            player.sendMessage(CC.translate("&cYou are unable to spectate that player."));
            return;
        }

        targetProfile.getMatch().addSpectator(player);
    }
}