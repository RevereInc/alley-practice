package dev.revere.alley.profile.command.player;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.level.menu.LevelMenu;
import dev.revere.alley.profile.ProfileService;
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