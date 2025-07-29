package dev.revere.alley.game.event.map.command.impl.manage;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.game.event.map.EventMapService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
public class EventMapListCommand extends BaseCommand {
    @CommandData(
            name = "eventmap.list",
            permission = "alley.command.eventmap.list",
            usage = "eventmap list",
            description = "List all maps"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        EventMapService eventMapRepository = Alley.getInstance().getService(EventMapService.class);

        if (eventMapRepository.getMaps().isEmpty()) {
            Arrays.asList(
                    "",
                    "&6&lEventMap List &f(&60&f)",
                    " &cNo maps available.",
                    ""
            ).forEach(line -> player.sendMessage(CC.translate(line)));
        } else {
            player.sendMessage("");
            player.sendMessage(CC.translate(" &6&lEventMap List &f(&6" + eventMapRepository.getMaps().size() + "&f)"));
            eventMapRepository.getMaps().forEach(map -> player.sendMessage(CC.translate(" &f● &6" + map.getName())));
            player.sendMessage("");
        }
    }
}