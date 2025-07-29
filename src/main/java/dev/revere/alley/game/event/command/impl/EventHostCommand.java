package dev.revere.alley.game.event.command.impl;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.game.event.Event;
import dev.revere.alley.game.event.EventService;
import dev.revere.alley.game.event.enums.EventType;
import dev.revere.alley.game.event.impl.sumo.SumoEvent;
import dev.revere.alley.game.event.map.EventMapService;
import dev.revere.alley.game.event.map.enums.EventMapType;
import dev.revere.alley.util.TimeUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
public class EventHostCommand extends BaseCommand {
    @CommandData(
            name = "event.host",
            isAdminOnly = true,
            usage = "event host <type>",
            description = "Host an event"
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/event host &6<type>"));
            return;
        }

        EventMapService eventMapService = this.plugin.getService(EventMapService.class);
        EventService eventService = this.plugin.getService(EventService.class);
        if (eventService.isEventRunning()) {
            player.sendMessage(CC.translate("&cThere is already an event running."));
            return;
        }

        if (!eventService.canStartNewEvent()) {
            player.sendMessage(CC.translate("&cYou must wait before hosting again. (" + TimeUtil.millisToRoundedTime(eventService.getLastEvent()) + "&c)"));
            return;
        }

        EventType eventType;
        try {
            eventType = EventType.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(CC.translate("&cInvalid event type. Available types: &6" + Arrays.toString(EventType.values())));
            return;
        }

        if (eventType == EventType.SUMO) {
            Event event = new SumoEvent(eventMapService.getRandomEventMap(EventMapType.SUMO), player.getUniqueId());
            event.startEvent();
            eventService.setEvent(event);
            event.handleJoin(player, false);
        }

        player.sendMessage(CC.translate("&aSuccessfully hosted the event &6" + eventType.name() + "&a."));
    }
}