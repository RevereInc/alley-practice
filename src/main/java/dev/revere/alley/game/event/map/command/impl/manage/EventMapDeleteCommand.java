package dev.revere.alley.game.event.map.command.impl.manage;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.game.event.map.EventMap;
import dev.revere.alley.game.event.map.EventMapService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
public class EventMapDeleteCommand extends BaseCommand {
    @CommandData(
            name = "eventmap.delete",
            isAdminOnly = true,
            usage = "eventmap delete <name>",
            description = "Delete an event map."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/eventmap delete &6<name>"));
            return;
        }

        EventMapService eventMapService = this.plugin.getService(EventMapService.class);
        EventMap eventMap = eventMapService.getMap(args[0]);
        if (eventMapService.getMap(eventMap) == null) {
            player.sendMessage(CC.translate("&cAn event map with that name does not exist."));
            return;
        }

        eventMapService.deleteMap(eventMap);
        player.sendMessage(CC.translate("&aSuccessfully deleted the event map with the name &6" + eventMap.getName() + "&a."));
    }
}