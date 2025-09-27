package dev.revere.alley.feature.duel.command;

import dev.revere.alley.core.locale.internal.impl.message.GameMessagesLocaleImpl;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
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
            command.sendUsage();
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER).replace("{input}", args[0]));
            return;
        }

        DuelRequestService duelRequestService = this.plugin.getService(DuelRequestService.class);
        DuelRequest duelRequest = duelRequestService.getDuelRequest(player, target);
        if (duelRequest == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_DUEL_REQUESTS_INVALID_FROM_PLAYER));
            return;
        }

        Profile targetProfile = this.plugin.getService(ProfileService.class).getProfile(target.getUniqueId());
        if (targetProfile.isBusy()) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_PLAYER_IS_BUSY)
                    .replace("{name-color}", String.valueOf(targetProfile.getNameColor()))
                    .replace("{player}", target.getName())
            );
            return;
        }

        duelRequestService.acceptPendingRequest(duelRequest);
        player.sendMessage(this.getString(GameMessagesLocaleImpl.DUEL_REQUEST_ACCEPTED)
                .replace("{name-color}", String.valueOf(targetProfile.getNameColor()))
                .replace("{player}", target.getName())
        );
    }
}