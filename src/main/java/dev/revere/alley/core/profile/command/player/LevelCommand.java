package dev.revere.alley.core.profile.command.player;

import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.feature.level.menu.LevelMenu;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 22/04/2025
 */
public class LevelCommand extends BaseCommand {
    @CommandData(name = "level", aliases = {"levels"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        new LevelMenu(this.plugin.getService(ProfileService.class).getProfile(player.getUniqueId())).openMenu(player);
    }
}