package dev.revere.alley.feature.arena.command.impl.kit;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.command.ArenaLocaleImpl;
import dev.revere.alley.core.locale.internal.impl.command.KitLocaleImpl;
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

    @CommandData(
            name = "arena.addkit",
            isAdminOnly = true,
            usage = "arena addkit <arenaName> <kitName>",
            description = "Adds a kit to an arena"
    )
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
            player.sendMessage(this.getMessage(ArenaLocaleImpl.NOT_FOUND).replace("{arena-name}", arenaName));
            return;
        }

        if (arena.getType() == ArenaType.FFA) {
            player.sendMessage(this.getMessage(ArenaLocaleImpl.CANNOT_ADD_KITS_TO_FFA));
            return;
        }

        Kit kit = this.plugin.getService(KitService.class).getKit(kitName);
        if (kit == null) {
            player.sendMessage(this.getMessage(KitLocaleImpl.NOT_FOUND).replace("{kit-name}", kitName));
            return;
        }

        if (arena.getKits().contains(kit.getName())) {
            player.sendMessage(this.getMessage(ArenaLocaleImpl.KIT_ALREADY_ADDED_TO_ARENA)
                    .replace("{arena-name}", arenaName)
                    .replace("{kit-name}", kitName)
            );
            return;
        }

        arena.getKits().add(kit.getName());
        arenaService.saveArena(arena);
        player.sendMessage(this.getMessage(ArenaLocaleImpl.KIT_ADDED)
                .replace("{arena-name}", arenaName)
                .replace("{kit-name}", kitName)
        );
    }
}