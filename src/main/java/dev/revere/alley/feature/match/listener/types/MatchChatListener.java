package dev.revere.alley.feature.match.listener.types;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.constants.PluginConstant;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.feature.match.Match;
import dev.revere.alley.feature.match.MatchService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * @author Emmy
 * @project Alley
 * @since 26/06/2025
 */
public class MatchChatListener implements Listener {
    @EventHandler
    private void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission(AlleyPlugin.getInstance().getService(PluginConstant.class).getAdminPermissionPrefix() + ".bypass.command.restriction")) {
            return;
        }

        ProfileService profileService = AlleyPlugin.getInstance().getService(ProfileService.class);
        MatchService matchService = AlleyPlugin.getInstance().getService(MatchService.class);

        Profile profile = profileService.getProfile(player.getUniqueId());
        Match match = profile.getMatch();
        if (match == null) return;

        String commandInput = event.getMessage().toLowerCase();

        for (String blockedCommand : matchService.getBlockedCommands()) {
            if (commandInput.startsWith("/" + blockedCommand.toLowerCase())) {
                event.getPlayer().sendMessage(CC.translate("&cThis command is blocked during matches."));
                event.setCancelled(true);
                return;
            }
        }
    }
}