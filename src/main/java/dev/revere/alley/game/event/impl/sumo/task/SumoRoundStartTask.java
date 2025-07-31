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

            this.applyCooldownPhrase(sumoEvent, false);
        } else {
            this.applyCooldownPhrase(sumoEvent, true);
            event.notifyParticipants("&a" + (3 - this.getStage()) + "...");
        }
    }

    /**
     * Applies the cooldown phrase to all team members of both participants.
     *
     * @param event         the SumoEvent instance.
     * @param denyMovement  whether to deny movement during cooldown.
     */
    private void applyCooldownPhrase(SumoEvent event, boolean denyMovement) {
        event.getTeamMembers(event.getParticipantA())
                .forEach(teamMember -> event.handleCooldown(teamMember.getPlayer(), denyMovement));

        event.getTeamMembers(event.getParticipantB())
                .forEach(teamMember -> event.handleCooldown(teamMember.getPlayer(), denyMovement));
    }
}