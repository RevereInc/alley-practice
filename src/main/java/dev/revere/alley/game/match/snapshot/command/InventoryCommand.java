package dev.revere.alley.game.match.snapshot.command;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.game.match.snapshot.SnapshotService;
import dev.revere.alley.game.match.snapshot.Snapshot;
import dev.revere.alley.game.match.snapshot.menu.SnapshotMenu;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 15/06/2024 - 22:19
 */
public class InventoryCommand extends BaseCommand {
    @CommandData(name = "inventory")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            player.sendMessage(CC.translate("&cUsage: /inventory (uuid)"));
            return;
        }

        String uuid = args[0];
        Snapshot snapshot;
        SnapshotService snapshotRepository = Alley.getInstance().getService(SnapshotService.class);

        try {
            snapshot = snapshotRepository.getSnapshot(UUID.fromString(uuid));
        } catch (Exception exception) {
            snapshot = snapshotRepository.getSnapshot(uuid);
        }

        if (snapshot == null) {
            player.sendMessage(CC.translate("&cThis inventory has expired."));
            return;
        }

        new SnapshotMenu(snapshot).openMenu(player);
    }
}