package dev.revere.alley.game.event.impl.sumo.task;

import dev.revere.alley.game.event.Event;
import dev.revere.alley.game.event.EventCountdown;
import dev.revere.alley.game.event.enums.EventState;
import dev.revere.alley.game.event.task.EventTask;
import dev.revere.alley.tool.logger.Logger;
import org.bukkit.Bukkit;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
public class SumoGameTask extends EventTask {
    /**
     * Constructor for the SumoGameTask class.
     *
     * @param event the event .
     */
    public SumoGameTask(Event event) {
        super(event, EventState.PREPARING);
    }

    @Override
    public void handleTaskImpl() {
        Event event = this.getEvent();

        Logger.info("&cEvent is running.");

        if (this.getStage() >= 300) {
            Logger.info("&cEvent has been running for too long, cancelling.");

            event.notifyParticipants("&cThe event has been cancelled due to a lack of players.");
            if (event.getParticipants() != null) {
                event.getParticipants().forEach((uuid, participant) -> event.handleLeave(Bukkit.getPlayer(participant.getUuid()), false));
            }

            event.stopEvent();
            return;
        }

        if (event.getPlayers().size() <= 1 && event.getCountdown() != null) {
            Logger.info("&cNot enough players to start the event.");
            event.setCountdown(null);
            event.sendNotEnoughPlayersMessage(event.getEventType());
        }

        if (event.getPlayers().size() == event.getMaxPlayers() || (this.getStage() >= 30 && event.getPlayers().size() >= 2)) {
            Logger.info("&cEvent has enough players to start. Starting in 10 seconds.");
            if (event.getCountdown() == null) {
                event.setCountdown(new EventCountdown(11_000));

                //TODO: broadcast starting in bhla bla message
            } else {
                if (event.getCountdown().isOver()) {
                    Logger.info("&cEvent has started.");
                    event.setState(EventState.STARTING_ROUND);
                    event.handleNewRound();
                    event.setTotalPlayers(event.getPlayers().size());
                    event.setTask(new SumoRoundStartTask(event));
                }
            }
        }

        if (this.getStage() % 10 == 0) {
            event.broadcastEvent(event.getEventType());
        }
    }
}