package dev.revere.alley.game.match.listener.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.api.constant.PluginConstant;
import dev.revere.alley.game.match.Match;
import dev.revere.alley.game.match.MatchService;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
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

        if (player.hasPermission(Alley.getInstance().getService(PluginConstant.class).getAdminPermissionPrefix() + ".bypass.command.restriction")) {
            return;
        }

        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        MatchService matchService = Alley.getInstance().getService(MatchService.class);

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