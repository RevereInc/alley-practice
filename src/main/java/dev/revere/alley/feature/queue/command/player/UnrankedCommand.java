package dev.revere.alley.feature.queue.command.player;

import dev.revere.alley.feature.queue.menu.QueuesMenuModern;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project alley-practice
 * @date 22/06/2025
 */
public class UnrankedCommand extends BaseCommand {
    @CommandData(name = "unranked")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        new QueuesMenuModern().openMenu(player);
    }
}
