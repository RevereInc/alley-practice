package dev.revere.alley.feature.duel.command;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.internal.locale.impl.ProfileLocale;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.feature.duel.DuelRequest;
import dev.revere.alley.feature.duel.DuelRequestService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 17/10/2024 - 20:31
 */
public class AcceptCommand extends BaseCommand {
    @CommandData(name = "accept", aliases = {"duel.accept"})
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
            player.sendMessage(CC.translate("&cThat player is not online."));
            return;
        }

        DuelRequestService duelRequestService = this.plugin.getService(DuelRequestService.class);
        DuelRequest duelRequest = duelRequestService.getDuelRequest(player, target);
        if (duelRequest == null) {
            player.sendMessage(CC.translate("&cYou do not have a pending duel request from that player."));
            return;
        }

        Profile targetProfile = this.plugin.getService(ProfileService.class).getProfile(target.getUniqueId());
        if (targetProfile.isBusy()) {
            player.sendMessage(ProfileLocale.IS_BUSY.getMessage().replace("{color}", String.valueOf(targetProfile.getNameColor())).replace("{player}", target.getName()));
            return;
        }

        duelRequestService.acceptPendingRequest(duelRequest);
        player.sendMessage(CC.translate("&aYou have accepted the duel request from " + target.getName() + "."));
    }
}
