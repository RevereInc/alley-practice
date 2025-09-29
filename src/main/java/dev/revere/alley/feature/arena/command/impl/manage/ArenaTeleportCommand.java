package dev.revere.alley.feature.arena.command.impl.manage;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.arena.menu.ArenaPositionMenu;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.library.command.annotation.CompleterData;
import io.netty.channel.epoll.Epoll;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 01/06/2024 - 00:08
 */
public class ArenaTeleportCommand extends BaseCommand {
    @CompleterData(
            name = "arena.teleport",
            aliases = "arena.tp"
    )
    public List<String> arenaTeleportCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission(this.getAdminPermission())) {
            this.plugin.getService(ArenaService.class).getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @CommandData(
            name = "arena.teleport",
            aliases = "arena.tp",
            isAdminOnly = true,
            usage = "arena teleport <arenaName>",
            description = "Teleports you to the center of an arena."
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

        if (arena.getCenter() == null) {
            player.sendMessage(CC.translate("&cCenter appears to be not set for this arena."));
            new ArenaPositionMenu(arena).openMenu(player);
            return;
        }

        player.teleport(arena.getCenter());
    }
}