package dev.revere.alley.feature.arena.command.impl.data;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.core.config.internal.locale.impl.ArenaLocale;
import dev.revere.alley.common.text.CC;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @date 15/09/2024 - 11:45
 */
public class ArenaSetDisplayNameCommand extends BaseCommand {
    @CommandData(name = "arena.setdisplayname", isAdminOnly = true, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(CC.translate("&6Usage: &e/arena setdisplayname &6<arenaName> <displayName>"));
            return;
        }

        String arenaName = args[0];
        ArenaService arenaService = this.plugin.getService(ArenaService.class);
        Arena arena = arenaService.getArenaByName(arenaName);
        if (arena == null) {
            sender.sendMessage(ArenaLocale.NOT_FOUND.getMessage().replace("{arena-name}", arenaName));
            return;
        }

        String displayName = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        arena.setDisplayName(displayName);
        arenaService.saveArena(arena);

        sender.sendMessage(ArenaLocale.DISPLAY_NAME_SET.getMessage().replace("{arena-name}", arenaName).replace("{display-name}", displayName));
    }
}