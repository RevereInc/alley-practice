package dev.revere.alley.core.profile.command.player;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.core.profile.menu.match.MatchHistorySelectKitMenu;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 14/09/2024 - 23:05
 */
public class MatchHistoryCommand extends BaseCommand {
    @CommandData(
            name = "matchhistory",
            aliases = {"pastmatches", "previousmatches", "mh"},
            usage = "matchhistory",
            description = "View your match history"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Profile profile = this.plugin.getService(ProfileService.class).getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou cannot do this right now!"));
            return;
        }

        if (profile.getProfileData().getPreviousMatches().isEmpty()) {
            player.sendMessage(CC.translate("&cYou have no match history!"));
            return;
        }

        new MatchHistorySelectKitMenu().openMenu(player);
    }
}