package dev.revere.alley.feature.queue.command.admin.impl;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.queue.QueueService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class QueueReloadCommand extends BaseCommand {
    @CommandData(name = "queue.reload", aliases = {"reloadqueue", "reloadqueues"}, isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        this.plugin.getService(QueueService.class).reloadQueues();
        player.sendMessage(CC.translate("&aYou've reloaded the queues."));
    }
}