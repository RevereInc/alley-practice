package dev.revere.alley.feature.match.command.admin.impl;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.match.Match;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 16/06/2025
 */
public class MatchResetBlocksCommand extends BaseCommand {
    @CommandData(
            name = "match.resetblocks",
            isAdminOnly = true,
            usage = "match resetblocks",
            description = "Reset all block changes in your current match."
    )
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
