package dev.revere.alley.feature.command.impl.other.troll;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author Remi
 * @project Alley
 * @date 6/19/2024
 */
public class LaunchCommand extends BaseCommand {
    @CommandData(
            name = "launch",
            isAdminOnly = true,
            inGameOnly = false,
            description = "Launch a player",
            usage = "launch <player> | all"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            command.sendUsage();
            return;
        }

        if (args[0].equalsIgnoreCase("all")) {
            player.getServer().getOnlinePlayers().forEach(target -> target.setVelocity(new Vector(0, 1, 0).multiply(15)));
            player.sendMessage(CC.translate("&fYou've launched all players"));
            return;
        }

        Player targetPlayer = player.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        targetPlayer.setVelocity(new Vector(0, 1, 0).multiply(15));
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.TROLL_PLAYER_LAUNCHED)
                .replace("{name-color}", String.valueOf(this.getProfile(targetPlayer.getUniqueId()).getNameColor()))
                .replace("{player}", targetPlayer.getName())
        );
    }
}