package dev.revere.alley.feature.arena.command.impl.data;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.internal.locale.impl.ArenaLocale;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.arena.ArenaType;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.library.command.annotation.CompleterData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaSetSpawnCommand extends BaseCommand {

    @CompleterData(name = "arena.setspawn")
    public List<String> arenaSetSpawnCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission(this.getAdminPermission())) {
            this.plugin.getService(ArenaService.class).getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @CommandData(name = "arena.setspawn", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/arena setspawn &6<arenaName> <blue/red/ffa>"));
            return;
        }

        String arenaName = args[0];
        String spawnType = args[1];

        ArenaService arenaService = this.plugin.getService(ArenaService.class);
        Arena arena = arenaService.getArenaByName(arenaName);
        if (arena == null) {
            player.sendMessage(ArenaLocale.NOT_FOUND.getMessage().replace("{arena-name}", arenaName));
            return;
        }

        if (!spawnType.equalsIgnoreCase("blue") && !spawnType.equalsIgnoreCase("red") && !spawnType.equalsIgnoreCase("ffa")) {
            player.sendMessage(CC.translate("&cInvalid spawn type! Valid types: blue, red, ffa"));
            return;
        }

        switch (spawnType.toLowerCase()) {
            case "blue":
                if (arena.getType() == ArenaType.FFA) {
                    player.sendMessage(CC.translate("&cFFA Arenas do not need a spawn position!"));
                    return;
                }
                arena.setPos1(player.getLocation());
                player.sendMessage(ArenaLocale.BLUE_SPAWN_SET.getMessage().replace("{arena-name}", arenaName));
                break;
            case "ffa":
                if (arena.getType() != ArenaType.FFA) {
                    player.sendMessage(CC.translate("&cThis arena is not an FFA arena!"));
                    return;
                }
                arena.setPos1(player.getLocation());
                player.sendMessage(ArenaLocale.FFA_SPAWN_SET.getMessage().replace("{arena-name}", arenaName));
                break;
            default:
                if (arena.getType() == ArenaType.FFA) {
                    player.sendMessage(CC.translate("&cFFA Arenas do not need a spawn position!"));
                    return;
                }
                arena.setPos2(player.getLocation());
                player.sendMessage(ArenaLocale.RED_SPAWN_SET.getMessage().replace("{arena-name}", arenaName));
                break;
        }

        arenaService.saveArena(arena);
    }
}