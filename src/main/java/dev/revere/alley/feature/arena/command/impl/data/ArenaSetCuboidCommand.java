package dev.revere.alley.feature.arena.command.impl.data;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.internal.locale.impl.ArenaLocale;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.arena.ArenaType;
import dev.revere.alley.feature.arena.selection.ArenaSelection;
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
public class ArenaSetCuboidCommand extends BaseCommand {

    @CompleterData(name = "arena.setcuboid")
    public List<String> arenaCuboidCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission(this.getAdminPermission())) {
            this.plugin.getService(ArenaService.class).getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @CommandData(name = "arena.setcuboid", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/arena setcuboid &6<arenaName>"));
            return;
        }

        ArenaSelection arenaSelection = ArenaSelection.createSelection(player);
        if (!arenaSelection.hasSelection()) {
            player.sendMessage(ArenaLocale.NO_SELECTION.getMessage());
            return;
        }

        String arenaName = args[0];
        ArenaService arenaService = this.plugin.getService(ArenaService.class);
        Arena arena = arenaService.getArenaByName(arenaName);
        if (arena == null) {
            player.sendMessage(ArenaLocale.NOT_FOUND.getMessage().replace("{arena-name}", arenaName));
            return;
        }

        if (arena.getType() == ArenaType.FFA) {
            player.sendMessage(CC.translate("&cYou cannot set cuboids for Free-For-All arenas! You must use: &4/arena setsafezone pos1/pos2&c."));
            return;
        }

        arena.setMinimum(arenaSelection.getMinimum());
        arena.setMaximum(arenaSelection.getMaximum());
        arenaService.saveArena(arena);

        player.sendMessage(ArenaLocale.CUBOID_SET.getMessage().replace("{arena-name}", arena.getName()));
    }
}