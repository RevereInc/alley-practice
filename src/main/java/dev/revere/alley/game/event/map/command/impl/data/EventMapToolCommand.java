package dev.revere.alley.game.event.map.command.impl.data;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.arena.selection.ArenaSelection;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
public class EventMapToolCommand extends BaseCommand {
    @CommandData(
            name = "eventmap.tool",
            isAdminOnly = true,
            usage = "eventmap tool",
            description = "Get the Selection tool"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (player.getInventory().first(ArenaSelection.SELECTION_TOOL) != -1) {
            player.getInventory().remove(ArenaSelection.SELECTION_TOOL);
            player.sendMessage(CC.translate("&cSelection tool has been removed."));
            player.updateInventory();
            return;
        }

        player.getInventory().addItem(ArenaSelection.SELECTION_TOOL);
        player.sendMessage(CC.translate("&aSelection tool has been added to your inventory."));
        player.updateInventory();
    }
}