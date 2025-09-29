package dev.revere.alley.feature.arena.command.impl.manage;

import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.arena.menu.ArenaPositionMenu;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 28/09/2025
 */
public class ArenaPositionsCommand extends BaseCommand {
    @CommandData(
            name = "arena.positions",
            aliases = {"arena.pos", "arena.position"},
            isAdminOnly = true,
            usage = "arena positions <arenaName>",
            description = "View all positions of an arena"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        String arenaName = args[0];
        Arena arena = this.plugin.getService(ArenaService.class).getArenaByName(arenaName);
        if (arena == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ARENA_NOT_FOUND).replace("{arena-name}", arenaName));
            return;
        }

        new ArenaPositionMenu(arena).openMenu(player);
    }
}