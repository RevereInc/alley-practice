package dev.revere.alley.feature.command.impl.other.gamemode;

import dev.revere.alley.core.locale.internal.types.ErrorLocaleImpl;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.common.text.CC;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 13/06/2025
 */
public class SurvivalCommand extends BaseCommand {
    @CommandData(
            name = "gms",
            aliases = {"gm.s", "gamemode.s", "gm.0", "gm0", "gamemode.0", "gamemode.survival"},
            isAdminOnly = true,
            usage = "gamemode survival [player]",
            description = "Sets your or another player's gamemode to Survival."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(CC.translate("&eYour gamemode has been updated to Survival."));
            return;
        }

        Player targetPlayer = this.plugin.getServer().getPlayer(args[0]);
        if (targetPlayer == null) {
            player.sendMessage(this.getMessage(ErrorLocaleImpl.INVALID_PLAYER));
            return;
        }

        targetPlayer.setGameMode(GameMode.SURVIVAL);
        player.sendMessage(CC.translate("&eYou have updated &d" + targetPlayer.getName() + "'s &egamemode to Survival."));
        targetPlayer.sendMessage(CC.translate("&eYour gamemode has been updated to Survival."));
    }
}