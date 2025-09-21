package dev.revere.alley.feature.ffa.command.impl.admin.data;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.arena.ArenaType;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 11/04/2025
 */
public class FFASetSpawnCommand extends BaseCommand {
    @CommandData(
            name = "ffa.setspawn",
            isAdminOnly = true,
            usage = "ffa setspawn <arenaName> <spawnNumber>",
            description = "Set a spawn point for an FFA arena."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            command.sendUsage();
            return;
        }

        ArenaService arenaService = this.plugin.getService(ArenaService.class);
        Arena arena = arenaService.getArenaByName(args[0]);
        if (arena == null) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ARENA_NOT_FOUND).replace("{arena-name}", args[0]));
            return;
        }

        if (arena.getType() != ArenaType.FFA) {
            player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.FFA_CAN_ONLY_SETUP_IN_FFA_ARENA));
            return;
        }

        arena.setPos1(player.getLocation());
        player.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.FFA_SPAWN_SET).replace("{arena-name}", arena.getName()));
    }
}