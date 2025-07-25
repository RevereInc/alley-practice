package dev.revere.alley.base.arena.command.impl.storage;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.arena.ArenaService;
import dev.revere.alley.config.locale.impl.ArenaLocale;
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
