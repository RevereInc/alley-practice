package dev.revere.alley.feature.command.impl.other.troll;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.ErrorLocaleImpl;
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
            player.sendMessage(CC.translate("&6Usage: &e/strike &6<player>"));
            return;
        }

        String targetName = args[0];
        Player target = player.getServer().getPlayer(targetName);

        if (target == null) {
            player.sendMessage(this.getMessage(ErrorLocaleImpl.INVALID_PLAYER));
            return;
        }

        target.getWorld().strikeLightning(target.getLocation());
        player.sendMessage(CC.translate("&fYou've struck &6" + target.getName()));
    }
}