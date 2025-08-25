package dev.revere.alley.feature.ffa.command.impl.admin.data;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.arena.ArenaType;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 11/04/2025
 */
public class FFASetSpawnCommand extends BaseCommand {
    @CommandData(name = "ffa.setspawn", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/ffa setspawn &6<arenaName>"));
            return;
        }

        ArenaService arenaService = this.plugin.getService(ArenaService.class);
        Arena arena = arenaService.getArenaByName(args[0]);
        if (arena == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        if (arena.getType() != ArenaType.FFA) {
            player.sendMessage(CC.translate("&cYou can only set the spawn for Free-For-All arenas!"));
            return;
        }

        arena.setPos1(player.getLocation());
        player.sendMessage(CC.translate("&aFFA spawn position has been set for arena &6" + arena.getName() + "&a!"));
    }
}