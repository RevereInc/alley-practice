package dev.revere.alley.base.arena.command.impl.data;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.annotation.CompleterData;
import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.arena.ArenaService;
import dev.revere.alley.base.arena.enums.ArenaType;
import dev.revere.alley.config.locale.impl.ArenaLocale;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaToggleCommand extends BaseCommand {

    @CompleterData(name = "arena.toggle")
    public List<String> arenaToggleCompleter(CommandArgs command) {
        List<String> completion = new ArrayList<>();

        if (command.getArgs().length == 1 && command.getPlayer().hasPermission(this.getAdminPermission())) {
            this.plugin.getService(ArenaService.class).getArenas().forEach(arena -> completion.add(arena.getName()));
        }

        return completion;
    }

    @CommandData(name = "arena.toggle", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/arena toggle &6<arenaName>"));
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
            player.sendMessage(CC.translate("&cYou cannot enable or disable Free-For-All arenas!"));
            return;
        }

        if (arena.getMinimum() == null || arena.getMaximum() == null || arena.getPos1() == null || arena.getPos2() == null) {
            player.sendMessage(CC.translate("&cYou must finish configuring this arena before enabling or disabling!"));
            return;
        }

        if (arena.getKits().isEmpty()) {
            player.sendMessage(CC.translate("&cYou must add at least one kit to this arena before enabling or disabling!"));
            return;
        }

        arena.setEnabled(!arena.isEnabled());
        arenaService.saveArena(arena);

        player.sendMessage(ArenaLocale.TOGGLED.getMessage().replace("{arena-name}", arena.getName()).replace("{status}", arena.isEnabled() ? "enabled" : "disabled"));
    }
}