package dev.revere.alley.feature.arena.command.impl.data;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.arena.ArenaType;
import dev.revere.alley.feature.arena.internal.types.StandAloneArena;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 06/03/2025
 */
public class ArenaSetVoidLevelCommand extends BaseCommand {
    @CommandData(
            name = "arena.setvoidlevel",
            aliases = "arena.void",
            isAdminOnly = true,
            usage = "arena setvoidlevel <arenaName> <voidLevel>",
            description = "Sets the void level for a standalone arena."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/arena setvoidlevel &6<arenaName> <voidLevel>"));
            return;
        }

        ArenaService arenaService = this.plugin.getService(ArenaService.class);
        Arena arena = arenaService.getArenaByName(args[0]);
        if (arena == null) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ARENA_NOT_FOUND).replace("{arena-name}", args[0]));
            return;
        }

        if (arena.getType() != ArenaType.STANDALONE) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ARENA_MUST_BE_STANDALONE));
            return;
        }

        int voidLevel;
        try {
            voidLevel = Integer.parseInt(args[1]);
            if (voidLevel < 0 || voidLevel > 256) {
                player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ARENA_VOID_LEVEL_OUT_OF_BOUNDS));
                return;
            }
        } catch (NumberFormatException e) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ERROR_INVALID_NUMBER).replace("{input}", args[1]));
            return;
        }

        ((StandAloneArena) arena).setVoidLevel(voidLevel);
        arenaService.saveArena(arena);

        player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ARENA_VOID_LEVEL_SET)
                .replace("{arena-name}", arena.getName())
                .replace("{void-level}", String.valueOf(voidLevel))
        );
    }
}