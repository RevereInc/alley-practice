package dev.revere.alley.game.event;

import dev.revere.alley.plugin.lifecycle.Service;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
public interface EventService extends Service {
    /**
     * Terminates the current event and resets the last event time.
     * This method should be called when an event ends or is cancelled.
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
     * Retrieves the current active event.
     *
     * @return the active event, or null if no event is running.
     */
    Event getActiveEvent();

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