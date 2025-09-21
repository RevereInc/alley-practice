package dev.revere.alley.feature.match.snapshot.command;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.match.snapshot.SnapshotService;
import dev.revere.alley.feature.match.snapshot.Snapshot;
import dev.revere.alley.feature.match.snapshot.menu.SnapshotMenu;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 15/06/2024 - 22:19
 */
public class InventoryCommand extends BaseCommand {
    @CommandData(
            name = "inventory",
            aliases = {"inv", "snapshot"},
            usage = "inventory <playerId>",
            description = "View a player's inventory."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 1) {
            command.sendUsage();
            return;
        }

        String uuid = args[0];
        Snapshot snapshot;
        SnapshotService snapshotRepository = AlleyPlugin.getInstance().getService(SnapshotService.class);

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