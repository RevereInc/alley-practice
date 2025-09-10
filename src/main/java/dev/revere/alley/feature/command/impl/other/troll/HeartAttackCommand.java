package dev.revere.alley.feature.command.impl.other.troll;

import dev.revere.alley.core.locale.internal.types.ErrorLocaleImpl;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 28/10/2024 - 09:09
 */
public class HeartAttackCommand extends BaseCommand {
    @CommandData(
            name = "heartattack",
            isAdminOnly = true,
            usage = "heartattack <player>",
            description = "Gives a player a heart attack."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/heartattack &6<player>"));
            return;
        }

        String targetName = args[0];
        Player target = player.getServer().getPlayer(targetName);
        if (target == null) {
            player.sendMessage(this.getMessage(ErrorLocaleImpl.INVALID_PLAYER));
            return;
        }

        target.setHealth(0.5D);
        player.sendMessage(CC.translate("&fYou've given &6" + target.getName() + " &fa heart attack."));
    }
}