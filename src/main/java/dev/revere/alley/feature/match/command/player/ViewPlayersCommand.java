package dev.revere.alley.feature.match.command.player;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.feature.match.menu.SpectatorTeleportMenu;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 26/06/2025
 */
public class ViewPlayersCommand extends BaseCommand {
    @CommandData(name = "viewplayers")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getState() != ProfileState.SPECTATING) {
            player.sendMessage(CC.translate("&cYou cannot do this in your current state."));
            return;
        }

        new SpectatorTeleportMenu(profile.getMatch()).openMenu(player);
    }
}
