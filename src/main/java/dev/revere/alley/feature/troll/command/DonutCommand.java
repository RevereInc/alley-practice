package dev.revere.alley.feature.troll.command;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.troll.TrollService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project alley-practice
 * @date 22/07/2025
 */
public class DonutCommand extends BaseCommand {
    @CommandData(
            name = "donut",
            permission = "alley.command.troll.donut"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /donut <player>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(CC.translate("&cPlayer not found or not online."));
            return;
        }

        this.plugin.getService(TrollService.class).spawnDonut(target);
        player.sendMessage(CC.translate("&aYou have successfully donut'd " + target.getName() + "!"));
    }
}
