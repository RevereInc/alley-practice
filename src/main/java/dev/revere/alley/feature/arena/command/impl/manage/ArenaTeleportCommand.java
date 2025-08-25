package dev.revere.alley.feature.arena.command.impl.manage;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.internal.locale.impl.ArenaLocale;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.library.command.annotation.CompleterData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 01/06/2024 - 00:08
 */
public class ArenaTeleportCommand extends BaseCommand {

    @CompleterData(name = "arena.teleport", aliases = "arena.tp")
    public List<String> arenaTeleportCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission(this.getAdminPermission())) {
            this.plugin.getService(ArenaService.class).getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @Override
    @CommandData(name = "arena.teleport", aliases = "arena.tp", isAdminOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/arena teleport &6<arenaName>"));
            return;
        }

        String arenaName = args[0];
        Arena arena = this.plugin.getService(ArenaService.class).getArenaByName(arenaName);

        if (arena == null) {
            player.sendMessage(ArenaLocale.NOT_FOUND.getMessage().replace("{arena-name}", arenaName));
            return;
        }

        if (arena.getCenter() == null) {
            player.sendMessage(CC.translate("&cThe arena does not have a defined center location."));
            if (arena.getPos1() == null) {
                return;
            }

            player.teleport(arena.getPos1());
            player.sendMessage(CC.translate("&aYou have been teleported to position 1 instead."));
            return;
        }

        player.teleport(arena.getCenter());
    }
}