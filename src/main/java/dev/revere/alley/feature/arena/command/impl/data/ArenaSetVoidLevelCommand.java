package dev.revere.alley.feature.arena.command.impl.data;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.internal.locale.impl.ArenaLocale;
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
    @CommandData(name = "arena.setvoidlevel", aliases = "arena.void", isAdminOnly = true)
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
            player.sendMessage(ArenaLocale.NOT_FOUND.getMessage().replace("{arena-name}", args[0]));
            return;
        }

        if (arena.getType() != ArenaType.STANDALONE) {
            player.sendMessage(CC.translate("&cYou can only set the void level for standalone arenas!"));
            return;
        }

        int voidLevel;
        try {
            voidLevel = Integer.parseInt(args[1]);
            if (voidLevel < 0 || voidLevel > 256) {
                player.sendMessage(CC.translate("&cVoid level must be between 0 and 256!"));
                return;
            }
        } catch (NumberFormatException exception) {
            player.sendMessage(CC.translate("&cInvalid void level! Please enter a valid number."));
            return;
        }

        ((StandAloneArena) arena).setVoidLevel(voidLevel);
        arenaService.saveArena(arena);

        player.sendMessage(ArenaLocale.VOID_LEVEL_SET.getMessage().replace("{arena-name}", arena.getName()).replace("{void-level}", String.valueOf(voidLevel)));
    }
}