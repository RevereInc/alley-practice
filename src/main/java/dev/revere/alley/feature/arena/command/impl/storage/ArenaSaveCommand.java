package dev.revere.alley.feature.arena.command.impl.storage;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project alley-practice
 * @since 09/09/2025
 */
public class ArenaSaveCommand extends BaseCommand {
    @CommandData(
            name = "arena.save",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "arena save <name>",
            description = "Save an arena to the database."
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&6Usage: &e/arena save &6<name>"));
            return;
        }

        String arenaName = args[0];
        ArenaService arenaService = this.plugin.getService(ArenaService.class);
        Arena arena = arenaService.getArenaByName(arenaName);
        if (arena == null) {
            sender.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ARENA_NOT_FOUND).replace("{arena-name}", arenaName));
            return;
        }

        arenaService.saveArena(arena);
        sender.sendMessage(this.getMessage(GlobalMessagesLocaleImpl.ARENA_SAVED).replace("{arena-name}", arena.getName()));
    }
}