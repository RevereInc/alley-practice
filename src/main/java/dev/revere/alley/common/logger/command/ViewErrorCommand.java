package dev.revere.alley.common.logger.command;

import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import dev.revere.alley.common.logger.Logger;
import dev.revere.alley.common.text.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @since 03/04/2025
 */
public class ViewErrorCommand extends BaseCommand {
    @CommandData(
            name = "viewerror",
            isAdminOnly = true,
            inGameOnly = false,
            usage = "viewerror <error>",
            description = "View a logged error by its ID."
    )
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (sender instanceof Player) {
            sender.sendMessage(CC.translate("&cThis command can only be used in console."));
            return;
        }

        if (args.length == 0) {
            command.sendUsage();
            return;
        }

        try {
            UUID errorId = UUID.fromString(args[0]);
            Logger.viewException(errorId);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(CC.translate("&cInvalid error ID!"));
        }
    }
}