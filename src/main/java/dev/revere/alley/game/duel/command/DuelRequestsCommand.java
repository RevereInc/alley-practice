package dev.revere.alley.game.duel.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.server.ServerService;
import dev.revere.alley.game.duel.menu.DuelRequestsMenu;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 22/10/2024 - 18:19
 */
public class DuelRequestsCommand extends BaseCommand {
    @CommandData(name = "duelrequests", aliases = {"viewduelrequests", "viewrequests"})
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (this.plugin.getService(ProfileService.class).getProfile(player.getUniqueId()).getMatch() != null) {
            player.sendMessage(CC.translate("&cYou are already in a match."));
            return;
        }

        ServerService serverService = this.plugin.getService(ServerService.class);
        if (!serverService.isQueueingAllowed()) {
            player.sendMessage(CC.translate("&cQueueing is temporarily disabled. Please try again later."));
            player.closeInventory();
            return;
        }

        new DuelRequestsMenu().openMenu(player);
    }
}