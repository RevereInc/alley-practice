package dev.revere.alley.feature.arena.command.impl.storage;

import dev.revere.alley.core.config.internal.locale.impl.ArenaLocale;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaSaveAllCommand extends BaseCommand {
    @CommandData(name = "arena.saveall", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        for (Arena arena : this.plugin.getService(ArenaService.class).getArenas()) {
            this.plugin.getService(ArenaService.class).saveArena(arena);
        }

        player.sendMessage(ArenaLocale.SAVED_ALL.getMessage());
    }
}
