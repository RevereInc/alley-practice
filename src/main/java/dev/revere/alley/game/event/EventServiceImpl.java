package dev.revere.alley.game.event;

import dev.revere.alley.Alley;
import dev.revere.alley.game.event.enums.EventTeamSize;
import dev.revere.alley.game.event.enums.EventType;
import dev.revere.alley.game.event.impl.sumo.SumoEvent;
import dev.revere.alley.game.event.map.EventMap;
import dev.revere.alley.game.event.map.EventMapService;
import dev.revere.alley.game.event.map.enums.EventMapType;
import dev.revere.alley.plugin.annotation.Service;
import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
@Getter
@Service(provides = EventService.class, priority = 460)
public class EventServiceImpl implements EventService {
    private Event activeEvent;
    private long lastEvent = 0L;
    private final long COOLDOWN_PERIOD = 20L * 60 * 1000;

    @Override
    public void startEvent(Player player, EventType eventType, EventTeamSize teamSize) {
        Event event;
        switch (eventType) {
            case SUMO:
                event = new SumoEvent(this.getAccordingEventMapType(eventType), player.getUniqueId(), teamSize);
                break;
            default:
                throw new IllegalArgumentException("Unsupported event type: " + eventType);
        }

        event.startEvent();
        this.setEvent(event);
        event.handleJoin(player, false);
    }

    private EventMap getAccordingEventMapType(EventType eventType) {
        EventMapService eventMapService = Alley.getInstance().getService(EventMapService.class);
        EventMapType eventMapType;

        switch (eventType) {
            case SUMO:
                eventMapType = EventMapType.SUMO;
                break;
            default:
                throw new IllegalArgumentException("Unsupported event type: " + eventType);
        }

        return eventMapService.getRandomEventMap(eventMapType);
    }

    @Override
    public void terminateEvent() {
        this.activeEvent = null;
        this.lastEvent = System.currentTimeMillis();
    }

    @Override
    public void setEvent(Event event) {
        if (this.activeEvent != null) {
            throw new IllegalStateException("An event is already running.");
        }

        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null.");
        }

        this.activeEvent = event;
    }

    @Override
    public boolean isEventRunning() {
        return this.activeEvent != null;
    }

    @Override
    public boolean canStartNewEvent() {
        return System.currentTimeMillis() >= this.COOLDOWN_PERIOD;
    }
}