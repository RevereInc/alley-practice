package dev.revere.alley.feature.command.impl.other.gamemode;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.ErrorLocaleImpl;
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
public class CreativeCommand extends BaseCommand {
    @CommandData(
            name = "gmc",
            aliases = {"gm.c", "gamemode.c", "gm.1", "gm1", "gamemode.1", "gamemode.creative"},
            isAdminOnly = true,
            usage = "gamemode creative [player]",
            description = "Set your or another player's gamemode to Creative."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.setGameMode(GameMode.CREATIVE);
            player.sendMessage(CC.translate("&eYour gamemode has been updated to Creative."));
            return;
        }

        Player targetPlayer = this.plugin.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(this.getMessage(ErrorLocaleImpl.INVALID_PLAYER));
            return;
        }

        targetPlayer.setGameMode(GameMode.CREATIVE);
        player.sendMessage(CC.translate("&eYou have updated &d" + targetPlayer.getName() + "'s &egamemode to Creative."));
        targetPlayer.sendMessage(CC.translate("&eYour gamemode has been updated to Creative."));
    }
}