package dev.revere.alley.feature.arena.command.impl.data;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.arena.selection.ArenaSelection;
import dev.revere.alley.core.locale.internal.types.ArenaLocaleImpl;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaToolCommand extends BaseCommand {
    @CommandData(
            name = "arena.tool",
            aliases = "arena.wand",
            isAdminOnly = true,
            usage = "arena tool",
            description = "Gives or removes the arena selection tool"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (player.getInventory().first(ArenaSelection.SELECTION_TOOL) != -1) {
            player.getInventory().remove(ArenaSelection.SELECTION_TOOL);
            player.sendMessage(this.getMessage(ArenaLocaleImpl.SELECTION_TOOL_REMOVED));
            player.updateInventory();
            return;
        }

        player.getInventory().addItem(ArenaSelection.SELECTION_TOOL);
        player.sendMessage(this.getMessage(ArenaLocaleImpl.SELECTION_TOOL_ADDED));
        player.updateInventory();
    }
}