package dev.revere.alley.feature.arena.command.impl.kit;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.library.command.annotation.CompleterData;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.core.locale.internal.types.ArenaLocaleImpl;
import dev.revere.alley.core.locale.internal.types.KitLocaleImpl;
import dev.revere.alley.common.text.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaRemoveKitCommand extends BaseCommand {
    @CompleterData(name = "arena.removekit")
    public List<String> arenaRemoveKitCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission(this.getAdminPermission())) {
            this.plugin.getService(ArenaService.class).getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @CommandData(
            name = "arena.removekit",
            isAdminOnly = true,
            usage = "arena removekit <arenaName> <kitName>",
            description = "Removes a kit from an arena"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/arena removekit &6<arenaName> <kitName>"));
            return;
        }

        String arenaName = args[0];
        String kitName = args[1];

        ArenaService arenaService = this.plugin.getService(ArenaService.class);
        Arena arena = arenaService.getArenaByName(arenaName);
        if (arena.getName() == null) {
            player.sendMessage(this.getMessage(ArenaLocaleImpl.NOT_FOUND).replace("{arena-name}", arenaName));
            return;
        }

        if (this.plugin.getService(KitService.class).getKit(kitName).getName() == null) {
            player.sendMessage(this.getMessage(KitLocaleImpl.KIT_NOT_FOUND).replace("{kit-name}", kitName));
            return;
        }

        if (!arena.getKits().contains(kitName)) {
            player.sendMessage(this.getMessage(ArenaLocaleImpl.ARENA_DOES_NOT_HAVE_KIT).replace("{arena-name}", arenaName).replace("{kit-name}", kitName));
            return;
        }

        arena.getKits().remove(kitName);
        arenaService.saveArena(arena);

        player.sendMessage(this.getMessage(ArenaLocaleImpl.KIT_REMOVED).replace("{arena-name}", arenaName).replace("{kit-name}", kitName));
    }
}