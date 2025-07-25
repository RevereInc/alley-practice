package dev.revere.alley.game.match.command.admin.impl;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.game.match.Match;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 16/06/2025
 */
public class MatchResetBlocksCommand extends BaseCommand {
    @CommandData(name = "match.resetblocks", isAdminOnly = true, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        Match match = profile.getMatch();
        if (match == null) {
            player.sendMessage(CC.translate("&cYou are not in a match!"));
            return;
        }

        match.resetBlockChanges();
        match.sendMessage(CC.translate("&4" + player.getName() + " &ffelt like being a nerd and reset the blocks!"));
    }
}
