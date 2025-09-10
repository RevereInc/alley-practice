package dev.revere.alley.feature.duel.command;

import dev.revere.alley.core.locale.internal.types.ServerLocaleImpl;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.server.ServerService;
import dev.revere.alley.feature.duel.menu.DuelRequestsMenu;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 22/10/2024 - 18:19
 */
public class DuelRequestsCommand extends BaseCommand {
    @CommandData(
            name = "duelrequests",
            aliases = {"viewduelrequests", "viewrequests"},
            usage = "duelrequests",
            description = "View your duel requests"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (this.plugin.getService(ProfileService.class).getProfile(player.getUniqueId()).getMatch() != null) {
            player.sendMessage(CC.translate("&cYou are already in a match."));
            return;
        }

        ServerService serverService = this.plugin.getService(ServerService.class);
        if (!serverService.isQueueingAllowed()) {
            player.sendMessage(this.getMessage(ServerLocaleImpl.QUEUE_TEMPORARILY_DISABLED));
            player.closeInventory();
            return;
        }

        new DuelRequestsMenu().openMenu(player);
    }
}