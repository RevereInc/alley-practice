package dev.revere.alley.feature.command.impl.other.gamemode;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 13/06/2025
 */
public class SpectatorCommand extends BaseCommand {
    @CommandData(name = "gmsp", aliases = {"gm.sp", "gamemode.sp", "gm.3", "gm3", "gamemode.3", "gamemode.spectator"}, isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage(CC.translate("&eYour gamemode has been updated to Spectator."));
            return;
        }

        Player targetPlayer = this.plugin.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        targetPlayer.setGameMode(GameMode.SPECTATOR);
        player.sendMessage(CC.translate("&eYou have updated &d" + targetPlayer.getName() + "'s &egamemode to Spectator."));
        targetPlayer.sendMessage(CC.translate("&eYour gamemode has been updated to Spectator."));
    }
}