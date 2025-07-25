package dev.revere.alley.feature.title.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.title.menu.TitleMenu;
import dev.revere.alley.profile.ProfileService;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 22/04/2025
 */
public class TitleCommand extends BaseCommand {
    @CommandData(name = "title", aliases = {"titles"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        new TitleMenu(this.plugin.getService(ProfileService.class).getProfile(player.getUniqueId())).openMenu(player);
    }
}