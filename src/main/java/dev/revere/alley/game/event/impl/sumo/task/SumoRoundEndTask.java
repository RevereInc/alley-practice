package dev.revere.alley.game.event.impl.sumo.task;

import dev.revere.alley.game.event.Event;
import dev.revere.alley.game.event.enums.EventState;
import dev.revere.alley.game.event.task.EventTask;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
public class SumoRoundEndTask extends EventTask {
    /**
     * Constructor for the SumoRoundEndTask class.
     *
     * @param event the event .
     */
    public SumoRoundEndTask(Event event) {
        super(event, EventState.ENDING_ROUND);
    }

    @Override
    public void handleTaskImpl() {
        Event event = this.getEvent();

        if (event.canEventEnd()) {
            event.stopEvent();
        } else {
            if (this.getStage() >= 3) {
                event.handleNewRound();
            }
        }
    }
}