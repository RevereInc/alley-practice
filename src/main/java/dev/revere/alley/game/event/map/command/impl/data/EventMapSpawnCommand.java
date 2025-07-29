package dev.revere.alley.game.event.map.command.impl.data;

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
public class EventMapSpawnCommand extends BaseCommand {
    @CommandData(
            name = "eventmap.spawn",
            isAdminOnly = true,
            usage = "eventmap spawn <name>",
            description = "Spawn an event map."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/eventmap spawn &6<name>"));
            return;
        }

        EventMapService eventMapService = this.plugin.getService(EventMapService.class);
        EventMap eventMap = eventMapService.getMap(args[0]);
        if (eventMap == null) {
            player.sendMessage(CC.translate("&cAn event map with that name does not exist."));
            return;
        }

        eventMap.setSpawn(player.getLocation());
        eventMapService.saveMap(eventMap);
        player.sendMessage(CC.translate("&aSuccessfully spawned the event map &6" + eventMap.getName() + "&a."));
    }
}