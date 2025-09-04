package dev.revere.alley.feature.duel.command;

import dev.revere.alley.core.config.internal.locale.impl.ErrorLocale;
import dev.revere.alley.core.config.internal.locale.impl.ProfileLocale;
import dev.revere.alley.core.config.internal.locale.impl.ServerLocale;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.server.ServerService;
import dev.revere.alley.feature.duel.DuelRequestService;
import dev.revere.alley.feature.duel.menu.DuelRequestMenu;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.common.text.CC;
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
            player.sendMessage(ErrorLocale.INVALID_PLAYER.getMessage().replace("{input}", args[0]));
            return;
        }

        if (target == player) {
            player.sendMessage(ProfileLocale.CANT_DUEL_SELF.getMessage());
            return;
        }

        DuelRequestService duelRequestService = this.plugin.getService(DuelRequestService.class);
        if (duelRequestService.getDuelRequest(player, target) != null) {
            player.sendMessage(ProfileLocale.PENDING_DUEL_REQUEST.getMessage());
            return;
        }

        ServerService serverService = this.plugin.getService(ServerService.class);
        if (!serverService.isQueueingAllowed()) {
            player.sendMessage(ServerLocale.QUEUE_TEMPORARILY_DISABLED.getMessage());
            player.closeInventory();
            return;
        }

        Profile targetProfile = this.plugin.getService(ProfileService.class).getProfile(target.getUniqueId());
        if (!targetProfile.getProfileData().getSettingData().isReceiveDuelRequestsEnabled()) {
            player.sendMessage(ProfileLocale.PLAYER_DUEL_REQUESTS_DISABLED.getMessage()
                    .replace("{color}", String.valueOf(targetProfile.getNameColor()))
                    .replace("{player}", target.getName())
            );
            return;
        }

        if (targetProfile.isBusy()) {
            player.sendMessage(ErrorLocale.IS_BUSY.getMessage()
                    .replace("{color}", String.valueOf(targetProfile.getNameColor()))
                    .replace("{player}", target.getName())
            );
            return;
        }

        new DuelRequestMenu(target).openMenu(player);
    }
}