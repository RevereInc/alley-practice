package dev.revere.alley.feature.duel.command;

import dev.revere.alley.core.locale.internal.types.ErrorLocaleImpl;
import dev.revere.alley.core.locale.internal.types.ProfileLocaleImpl;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.duel.DuelRequest;
import dev.revere.alley.feature.duel.DuelRequestService;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 17/10/2024 - 20:31
 */
public class AcceptCommand extends BaseCommand {
    @CommandData(
            name = "accept",
            aliases = {"duel.accept"},
            usage = "accept <player>",
            description = "Accept a duel request"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/accept &6<player>"));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(this.getMessage(ErrorLocaleImpl.INVALID_PLAYER).replace("{input}", args[0]));
            return;
        }

        DuelRequestService duelRequestService = this.plugin.getService(DuelRequestService.class);
        DuelRequest duelRequest = duelRequestService.getDuelRequest(player, target);
        if (duelRequest == null) {
            player.sendMessage(this.getMessage(ProfileLocaleImpl.NO_PENDING_DUEL_REQUEST));
            return;
        }

        Profile targetProfile = this.plugin.getService(ProfileService.class).getProfile(target.getUniqueId());
        if (targetProfile.isBusy()) {
            player.sendMessage(this.getMessage(ErrorLocaleImpl.IS_BUSY)
                    .replace("{color}", String.valueOf(targetProfile.getNameColor()))
                    .replace("{player}", target.getName())
            );
            return;
        }

        duelRequestService.acceptPendingRequest(duelRequest);
        player.sendMessage(this.getMessage(ProfileLocaleImpl.ACCEPTED_DUEL_REQUEST)
                .replace("{color}", String.valueOf(targetProfile.getNameColor()))
                .replace("{player}", target.getName())
        );
    }
}