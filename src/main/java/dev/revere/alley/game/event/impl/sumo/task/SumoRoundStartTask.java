package dev.revere.alley.game.event.impl.sumo.task;

import dev.revere.alley.game.event.Event;
import dev.revere.alley.game.event.enums.EventState;
import dev.revere.alley.game.event.impl.sumo.SumoEvent;
import dev.revere.alley.game.event.task.EventTask;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
public class SumoRoundStartTask extends EventTask {
    /**
     * Constructor for the SumoRoundStartTask class.
     *
     * @param event the event .
     */
    public SumoRoundStartTask(Event event) {
        super(event, EventState.STARTING_ROUND);
    }

    @Override
    public void handleTaskImpl() {
        Event event = this.getEvent();
        SumoEvent sumoEvent = (SumoEvent) event;

        if (this.getStage() >= 3) {
            event.setTask(null);
            event.setRoundStart(System.currentTimeMillis());
            event.setState(EventState.RUNNING_ROUND);

            Player playerA = event.getParticipantPlayer(sumoEvent.getParticipantB());
            Player playerB = event.getParticipantPlayer(sumoEvent.getParticipantA());

            sumoEvent.handleCooldown(playerA, playerB, false);
        } else {
            Player playerA = event.getParticipantPlayer(sumoEvent.getParticipantB());
            Player playerB = event.getParticipantPlayer(sumoEvent.getParticipantA());

            ((SumoEvent) event).handleCooldown(playerA, playerB, true);
            event.notifyParticipants("&a" + (3 - this.getStage()) + "...");
        }
    }
}