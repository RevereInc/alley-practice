package dev.revere.alley.feature.arena.command.impl.kit;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.internal.locale.impl.ArenaLocale;
import dev.revere.alley.core.config.internal.locale.impl.KitLocale;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.arena.ArenaType;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
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
public class ArenaAddKitCommand extends BaseCommand {

    @CompleterData(name = "arena.addkit")
    public List<String> arenaAddKitCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission(this.getAdminPermission())) {
            this.plugin.getService(ArenaService.class).getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @CommandData(name = "arena.addkit", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/arena addkit &6<arenaName> <kitName>"));
            return;
        }

        String arenaName = args[0];
        String kitName = args[1];

        ArenaService arenaService = this.plugin.getService(ArenaService.class);
        Arena arena = arenaService.getArenaByName(arenaName);
        if (arena == null) {
            player.sendMessage(ArenaLocale.NOT_FOUND.getMessage().replace("{arena-name}", arenaName));
            return;
        }

        if (arena.getType() == ArenaType.FFA) {
            player.sendMessage(CC.translate("&cYou cannot add kits to Free-For-All arenas!"));
            return;
        }

        Kit kit = this.plugin.getService(KitService.class).getKit(kitName);
        if (kit == null) {
            player.sendMessage(KitLocale.KIT_NOT_FOUND.getMessage().replace("{kit-name}", kitName));
            return;
        }

        if (arena.getKits().contains(kit.getName())) {
            player.sendMessage(ArenaLocale.KIT_ALREADY_ADDED_TO_ARENA.getMessage().replace("{arena-name}", arenaName).replace("{kit-name}", kitName));
            return;
        }

        arena.getKits().add(kit.getName());
        arenaService.saveArena(arena);
        player.sendMessage(ArenaLocale.KIT_ADDED.getMessage().replace("{arena-name}", arenaName).replace("{kit-name}", kitName));
    }
}