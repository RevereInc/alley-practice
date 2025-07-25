package dev.revere.alley.game.match.command.player;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.game.match.menu.CurrentMatchesMenu;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class CurrentMatchesCommand extends BaseCommand {
    @CommandData(name = "currentmatches", aliases = {"matches", "games", "currentgames"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (this.plugin.getService(ProfileService.class).getProfile(player.getUniqueId()).getState() != ProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou can only execute this command at lobby."));
            return;
        }

        new CurrentMatchesMenu().openMenu(player);
    }
}