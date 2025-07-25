package dev.revere.alley.game.host.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.game.host.HostMenu;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.enums.ProfileState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 08/06/2024 - 21:31
 */
public class HostCommand extends BaseCommand {
    @Override
    @CommandData(name = "host", permission = "alley.command.donator.host")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (this.plugin.getService(ProfileService.class).getProfile(player.getUniqueId()).getState() != ProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou must be in the lobby to host a competition."));
            return;
        }

        new HostMenu().openMenu(player);
    }
}
