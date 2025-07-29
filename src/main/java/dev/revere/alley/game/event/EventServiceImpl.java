package dev.revere.alley.game.event;

import dev.revere.alley.plugin.annotation.Service;
import lombok.Getter;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
@Getter
@Service(provides = EventService.class, priority = 460)
public class EventServiceImpl implements EventService {
    private Event activeEvent;
    private final long period = 20L * 60 * 1000;
    private long lastEvent = 0L;

    @Override
    public void terminateEvent() {
        this.activeEvent = null;
        this.lastEvent = System.currentTimeMillis();
    }

    @Override
    public void setEvent(Event event) {
        if (this.activeEvent != null) {
            throw new IllegalStateException("An event is already running. Please terminate the current event before starting a new one.");
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
        return System.currentTimeMillis() >= this.period;
    }
}