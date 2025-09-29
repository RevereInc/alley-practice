package dev.revere.alley.feature.command.troll;

import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 25/06/2024 - 19:42
 */
public class StrikeCommand extends BaseCommand {
    @CommandData(
            name = "strike",
            isAdminOnly = true,
            inGameOnly = true,
            usage = "strike <player> | all",
            description = "Strike a player with lightning"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        String targetName = args[0];
        Player targetPlayer = player.getServer().getPlayer(targetName);

        if (targetPlayer == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        targetPlayer.getWorld().strikeLightning(targetPlayer.getLocation());
        player.sendMessage(this.getString(GlobalMessagesLocaleImpl.TROLL_PLAYER_STRUCK_BY_LIGHTNING)
                .replace("{name-color}", String.valueOf(this.getProfile(targetPlayer.getUniqueId()).getNameColor()))
                .replace("{player}", targetPlayer.getName())
        );
    }
}