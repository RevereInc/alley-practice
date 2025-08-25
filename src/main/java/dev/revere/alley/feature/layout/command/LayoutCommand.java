package dev.revere.alley.feature.layout.command;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.enums.ProfileState;
import dev.revere.alley.feature.layout.LayoutService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 03/05/2025
 */
public class LayoutCommand extends BaseCommand {
    @CommandData(name = "layout", aliases = {"layouteditor", "kiteditor"}, description = "Edit the layout of a kit.")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        ProfileService profileService = this.plugin.getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() != ProfileState.LOBBY && profile.getState() != ProfileState.WAITING) {
            player.sendMessage(CC.translate("&cYou are not in the lobby!"));
            return;
        }

        this.plugin.getService(LayoutService.class).getLayoutMenu().openMenu(player);
    }
}
