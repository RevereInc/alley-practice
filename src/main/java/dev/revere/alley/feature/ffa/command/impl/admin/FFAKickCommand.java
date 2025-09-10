package dev.revere.alley.feature.ffa.command.impl.admin;

import dev.revere.alley.core.locale.internal.types.ErrorLocaleImpl;
import dev.revere.alley.core.locale.internal.types.FFALocaleImpl;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.ffa.FFAMatch;
import dev.revere.alley.feature.ffa.FFAService;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public class FFAKickCommand extends BaseCommand {
    @CommandData(
            name = "ffa.kick",
            isAdminOnly = true,
            usage = "ffa kick <player>",
            description = "Kick a player from their current FFA match."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 1) {
            player.sendMessage(CC.translate("&cUsage: /ffa kick <player>"));
            return;
        }

        Player targetPlayer = player.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(this.getMessage(ErrorLocaleImpl.INVALID_PLAYER));
            return;
        }

        FFAMatch match = this.plugin.getService(FFAService.class).getFFAMatch(targetPlayer);
        if (match == null) {
            player.sendMessage(this.getMessage(FFALocaleImpl.PLAYER_NOT_IN_FFA));
            return;
        }

        match.leave(targetPlayer);
        player.sendMessage(this.getMessage(FFALocaleImpl.KICKED_PLAYER)
                .replace("{player}", targetPlayer.getName())
                .replace("{ffa-name}", match.getName())
                .replace("{player-color}", String.valueOf(this.plugin.getService(ProfileService.class).getProfile(targetPlayer.getUniqueId()).getNameColor()))
        );
    }
}