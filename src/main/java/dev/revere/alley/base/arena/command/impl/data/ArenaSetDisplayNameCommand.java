package dev.revere.alley.base.arena.command.impl.data;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.arena.ArenaService;
import dev.revere.alley.config.locale.impl.ArenaLocale;
import dev.revere.alley.util.chat.CC;
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

        sender.sendMessage(CC.translate("&aSuccessfully set the display name of the arena &e" + arenaName + " &ato &e" + displayName + "&a."));
    }
}
