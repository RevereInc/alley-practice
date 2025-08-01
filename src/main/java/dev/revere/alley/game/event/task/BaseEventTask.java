package dev.revere.alley.game.event.task;

import dev.revere.alley.Alley;
import dev.revere.alley.game.event.Event;
import dev.revere.alley.game.event.EventService;
import dev.revere.alley.game.event.enums.EventState;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
@Getter
public abstract class BaseEventTask extends BukkitRunnable {
    private final EventState state;
    private final Event event;
    private int stage;

    /**
     * Constructor for the EventTask class.
     *
     * @param event the event .
     * @param state the state of the event.
     */
    public BaseEventTask(Event event, EventState state) {
        this.event = event;
        this.state = state;
    }

    @Override
    public void run() {

        Logger.info("Running event task for event: " + this.event.getEventType().name() + " with state: " + this.state.name() + " and stage: " + this.stage);

        if (!Alley.getInstance().getService(EventService.class).isEventRunning()) {
            Logger.info("Event is not present, cancelling task.");
            this.cancel();
            return;
        }

        this.handleTaskImpl();

        this.stage++;
    }

    /**
     * Abstract method to be implemented by subclasses.
     * Logic specific to each event type will be defined here.
     */
    public abstract void handleTaskImpl();
}