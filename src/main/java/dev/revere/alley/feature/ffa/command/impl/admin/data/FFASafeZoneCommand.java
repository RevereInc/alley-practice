package dev.revere.alley.feature.ffa.command.impl.admin.data;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.command.ArenaLocaleImpl;
import dev.revere.alley.core.locale.internal.impl.command.FFALocaleImpl;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.arena.ArenaType;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 12/06/2024 - 21:56
 */
public class FFASafeZoneCommand extends BaseCommand {
    @Override
    @CommandData(
            name = "ffa.safezone",
            isAdminOnly = true,
            usage = "ffa safezone <arenaName> <pos1/pos2>",
            description = "Set the safezone positions for an FFA arena."
    )
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
            player.sendMessage(this.getMessage(ArenaLocaleImpl.NOT_FOUND).replace("{arena-name}", arenaName));
            return;
        }

        if (arenaService.getArenaByName(arenaName).getType() != ArenaType.FFA) {
            player.sendMessage(this.getMessage(FFALocaleImpl.CAN_ONLY_SETUP_IN_FFA_ARENA));
            return;
        }

        if (!spawnType.equalsIgnoreCase("pos1") && !spawnType.equalsIgnoreCase("pos2")) {
            player.sendMessage(this.getMessage(FFALocaleImpl.INVALID_SPAWN_TYPE));
            return;
        }

        if (spawnType.equalsIgnoreCase("pos1")) {
            arenaService.getArenaByName(arenaName).setMaximum(player.getLocation());
        } else {
            arenaService.getArenaByName(arenaName).setMinimum(player.getLocation());
        }

        player.sendMessage(this.getMessage(FFALocaleImpl.SAFEZONE_SET)
                .replace("{arena-name}", arenaName)
                .replace("{pos}", spawnType.equalsIgnoreCase("pos1") ? "position 1" : "position 2")
        );
        arenaService.saveArena(arenaService.getArenaByName(arenaName));
    }
}