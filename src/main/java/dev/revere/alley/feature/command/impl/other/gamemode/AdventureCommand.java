package dev.revere.alley.feature.command.impl.other.gamemode;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
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
public class AdventureCommand extends BaseCommand {
    @CommandData(
            name = "gma",
            aliases = {"gm.a", "gamemode.a", "gm.2", "gm2", "gamemode.2", "gamemode.adventure"},
            isAdminOnly = true,
            usage = "gamemode adventure [player]",
            description = "Set your or another player's gamemode to Adventure."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.setGameMode(GameMode.ADVENTURE);
            player.sendMessage(CC.translate("&eYour gamemode has been updated to Adventure."));
            return;
        }

        Player targetPlayer = this.plugin.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        targetPlayer.setGameMode(GameMode.ADVENTURE);
        player.sendMessage(CC.translate("&eYou have updated &d" + targetPlayer.getName() + "'s &egamemode to Adventure."));
        targetPlayer.sendMessage(CC.translate("&eYour gamemode has been updated to Adventure."));
    }
}