package dev.revere.alley.game.event.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.constant.PluginConstant;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
public class EventCommand extends BaseCommand {
    @CommandData(
            name = "event",
            usage = "event",
            description = "Event commands Help"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Arrays.asList(
                "",
                "&6&lEvent Commands",
                " &f● &6/event join &7| Join an event",
                " &f● &6/event leave &7| Leave an event",
                " &f● &6/event info &7| Get information about the event",
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));

        if (player.hasPermission(this.plugin.getService(PluginConstant.class).getAdminPermissionPrefix())) {
            Arrays.asList(
                    "",
                    "&6&lAdmin Help",
                    " &f● &6/event host &8(&7type&8) &7| Host an event",
                    " &f● &6/event start &7| Join an event",
                    " &f● &6/event end &7| End the current event",
                    ""
            ).forEach(line -> player.sendMessage(CC.translate(line)));
        }
    }
}
