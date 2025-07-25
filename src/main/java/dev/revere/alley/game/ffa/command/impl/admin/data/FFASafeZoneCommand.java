package dev.revere.alley.game.ffa.command.impl.admin.data;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.arena.ArenaService;
import dev.revere.alley.base.arena.enums.ArenaType;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 12/06/2024 - 21:56
 */
public class FFASafeZoneCommand extends BaseCommand {
    @Override
    @CommandData(name = "ffa.safezone", isAdminOnly = true)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/ffa safezone &6<arenaName> <pos1/pos2>"));
            return;
        }

        String arenaName = args[0];
        String spawnType = args[1];

        ArenaService arenaService = this.plugin.getService(ArenaService.class);

        if (arenaService.getArenaByName(arenaName) == null) {
            player.sendMessage(CC.translate("&cAn arena with that name does not exist!"));
            return;
        }

        if (arenaService.getArenaByName(arenaName).getType() != ArenaType.FFA) {
            player.sendMessage(CC.translate("&cYou can only set the safezone for Free-For-All arenas!"));
            return;
        }

        if (!spawnType.equalsIgnoreCase("pos1") && !spawnType.equalsIgnoreCase("pos2")) {
            player.sendMessage(CC.translate("&cInvalid spawn type! Valid types: pos1, pos2"));
            return;
        }

        if (spawnType.equalsIgnoreCase("pos1")) {
            arenaService.getArenaByName(arenaName).setMaximum(player.getLocation());
            player.sendMessage(CC.translate("&aSafe Zone position 1 has been set for arena &6" + arenaName + "&a!"));
        } else {
            arenaService.getArenaByName(arenaName).setMinimum(player.getLocation());
            player.sendMessage(CC.translate("&aSafe Zone position 2 has been set for arena &6" + arenaName + "&a!"));
        }

        arenaService.saveArena(arenaService.getArenaByName(arenaName));
    }
}