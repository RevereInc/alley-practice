package dev.revere.alley.game.event.map.command.impl.manage;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.game.event.map.EventMapService;
import dev.revere.alley.game.event.map.enums.EventMapType;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
public class EventMapCreateCommand extends BaseCommand {
    @CommandData(
            name = "eventmap.create",
            isAdminOnly = true,
            usage = "eventmap create <name>",
            description = "Create an event map."
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/eventmap create &6<name> <type>"));
            return;
        }

        String name = args[0];
        EventMapService eventMapRepository = Alley.getInstance().getService(EventMapService.class);
        if (eventMapRepository.getMap(name) != null) {
            player.sendMessage(CC.translate("&cAn event map with that name already exists."));
            return;
        }

        EventMapType type;
        try {
            type = EventMapType.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(CC.translate("&cAn event map type with that name does not exist. Available types: &6" + Arrays.toString(EventMapType.values())));
            return;
        }

        eventMapRepository.createMap(name, type);
        player.sendMessage(CC.translate("&aSuccessfully created an event map with the name &6" + name + "&a."));
    }
}