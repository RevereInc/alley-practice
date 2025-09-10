package dev.revere.alley.feature.duel.command;

import dev.revere.alley.core.locale.internal.types.ErrorLocaleImpl;
import dev.revere.alley.core.locale.internal.types.ProfileLocaleImpl;
import dev.revere.alley.core.locale.internal.types.ServerLocaleImpl;
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
    @CommandData(
            name = "duel",
            usage = "duel <player>",
            description = "Send a duel request to a player"
    )
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
            player.sendMessage(this.getMessage(ErrorLocaleImpl.INVALID_PLAYER).replace("{input}", args[0]));
            return;
        }

        if (target == player) {
            player.sendMessage(this.getMessage(ProfileLocaleImpl.CANT_DUEL_SELF));
            return;
        }

        DuelRequestService duelRequestService = this.plugin.getService(DuelRequestService.class);
        if (duelRequestService.getDuelRequest(player, target) != null) {
            player.sendMessage(this.getMessage(ProfileLocaleImpl.PENDING_DUEL_REQUEST));
            return;
        }

        ServerService serverService = this.plugin.getService(ServerService.class);
        if (!serverService.isQueueingAllowed()) {
            player.sendMessage(this.getMessage(ServerLocaleImpl.QUEUE_TEMPORARILY_DISABLED));
            player.closeInventory();
            return;
        }

        Profile targetProfile = this.plugin.getService(ProfileService.class).getProfile(target.getUniqueId());
        if (!targetProfile.getProfileData().getSettingData().isReceiveDuelRequestsEnabled()) {
            player.sendMessage(this.getMessage(ProfileLocaleImpl.PLAYER_DUEL_REQUESTS_DISABLED)
                    .replace("{color}", String.valueOf(targetProfile.getNameColor()))
                    .replace("{player}", target.getName())
            );
            return;
        }

        if (targetProfile.isBusy()) {
            player.sendMessage(this.getMessage(ErrorLocaleImpl.IS_BUSY)
                    .replace("{color}", String.valueOf(targetProfile.getNameColor()))
                    .replace("{player}", target.getName())
            );
            return;
        }

        new DuelRequestMenu(target).openMenu(player);
    }
}