package dev.revere.alley.game.duel.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.server.ServerService;
import dev.revere.alley.config.locale.impl.ProfileLocale;
import dev.revere.alley.game.duel.DuelRequestService;
import dev.revere.alley.game.duel.menu.DuelRequestMenu;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 17/10/2024 - 20:09
 */
public class DuelCommand extends BaseCommand {
    @CommandData(name = "duel")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/duel &6<player>"));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cThat player is not online."));
            return;
        }

        if (target == player) {
            player.sendMessage(CC.translate("&cYou cannot duel yourself."));
            return;
        }

        DuelRequestService duelRequestService = this.plugin.getService(DuelRequestService.class);
        if (duelRequestService.getDuelRequest(player, target) != null) {
            player.sendMessage(CC.translate("&cYou already have a pending duel request with this player."));
            return;
        }

        ServerService serverService = this.plugin.getService(ServerService.class);
        if (!serverService.isQueueingAllowed()) {
            player.sendMessage(CC.translate("&cQueueing is temporarily disabled. Please try again later."));
            player.closeInventory();
            return;
        }

        Profile targetProfile = this.plugin.getService(ProfileService.class).getProfile(target.getUniqueId());
        if (!targetProfile.getProfileData().getSettingData().isReceiveDuelRequestsEnabled()) {
            player.sendMessage(CC.translate("&cThis player has disabled duel requests."));
            return;
        }

        if (targetProfile.isBusy()) {
            player.sendMessage(ProfileLocale.IS_BUSY.getMessage().replace("{color}", String.valueOf(targetProfile.getNameColor())).replace("{player}", target.getName()));
            return;
        }

        new DuelRequestMenu(target).openMenu(player);
    }
}