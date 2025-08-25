package dev.revere.alley.feature.arena.command.impl.data;

import dev.revere.alley.core.config.internal.locale.impl.ArenaLocale;
import dev.revere.alley.feature.arena.selection.ArenaSelection;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaToolCommand extends BaseCommand {

    @CommandData(name = "arena.tool", aliases = "arena.wand", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (player.getInventory().first(ArenaSelection.SELECTION_TOOL) != -1) {
            player.getInventory().remove(ArenaSelection.SELECTION_TOOL);
            player.sendMessage(ArenaLocale.SELECTION_TOOL_REMOVED.getMessage());
            player.updateInventory();
            return;
        }

        player.getInventory().addItem(ArenaSelection.SELECTION_TOOL);
        player.sendMessage(ArenaLocale.SELECTION_TOOL_ADDED.getMessage());
        player.updateInventory();
    }
}
