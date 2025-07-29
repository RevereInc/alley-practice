package dev.revere.alley.game.event.map.command;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.util.chat.CC;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @date 23/12/2024 - 11:25
 */
public class EventMapCommand extends BaseCommand {
    @CommandData(
            name = "eventmap",
            isAdminOnly = true
    )
    @Override
    public void onCommand(CommandArgs command) {
        Arrays.asList(
                "",
                "&6&lEventMap Commands",
                " &f● &6/eventmap list &7| List all maps",
                " &f● &6/eventmap view &8(&7map-name&8) &7| View information about a map",
                " &f● &6/eventmap delete &8(&7map-name&8) &7| Delete an event-map",
                " &f● &6/eventmap create &8(&7map-name&8) &7| Create an event-map",
                " &f● &6/eventmap tool &7| Get the Selection tool",
                " &f● &6/eventmap pos1 &7| Set the first position",
                " &f● &6/eventmap pos2 &7| Set the second position",
                " &f● &6/eventmap center &7| Set the center",
                " &f● &6/eventmap spawn &7| Set the spawn",
                ""
        ).forEach(line -> command.getPlayer().sendMessage(CC.translate(line)));
    }
}