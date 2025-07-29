package dev.revere.alley.game.event;

import dev.revere.alley.plugin.lifecycle.Service;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
public interface EventService extends Service {
    /**
     * Starts the event if it can be started.
     */
    void terminateEvent();

    /**
     * Sets the current event.
     *
     * @param event the event to set.
     */
    void setEvent(Event event);

    /**
     * Retrieves the last event time in milliseconds.
     *
     * @return the timestamp of the last event in milliseconds.
     */
    long getLastEvent();

    /**
     * Checks if there is an event ongoing.
     *
     * @return true if an event is running, false otherwise.
     */
    boolean isEventRunning();

    /**
     * Checks if you can start a new event.
     *
     * @return true if you can start a new event, false otherwise.
     */
    boolean canStartNewEvent();
}