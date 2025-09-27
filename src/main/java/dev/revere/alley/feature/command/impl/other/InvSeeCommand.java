package dev.revere.alley.feature.command.impl.other;

import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.library.command.BaseCommand;
import dev.revere.alley.library.command.CommandArgs;
import dev.revere.alley.library.command.annotation.CommandData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 20/06/2024 - 01:15
 */
public class InvSeeCommand extends BaseCommand {
    @CommandData(
            name = "invsee",
            aliases = {"seeinventory", "seeinv"},
            isAdminOnly = true,
            usage = "invsee <player>",
            description = "View another player's inventory."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            command.sendUsage();
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_PLAYER));
            return;
        }

        player.openInventory(target.getInventory());
    }
}
