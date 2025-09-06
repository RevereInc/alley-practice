package dev.revere.alley.feature.ffa.command.impl.admin.data;

import dev.revere.alley.core.config.internal.locale.impl.ArenaLocale;
import dev.revere.alley.core.config.internal.locale.impl.FFALocale;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.arena.ArenaType;
import dev.revere.alley.common.text.CC;
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
            player.sendMessage(ArenaLocale.NOT_FOUND.getMessage().replace("{arena-name}", args[0]));
            return;
        }

        if (arena.getType() != ArenaType.FFA) {
            player.sendMessage(FFALocale.CAN_ONLY_SETUP_IN_FFA_ARENA.getMessage());
            return;
        }

        arena.setPos1(player.getLocation());
        player.sendMessage(FFALocale.SPAWN_SET.getMessage().replace("{arena-name}", arena.getName()));
    }
}