package dev.revere.alley.game.event.impl.sumo;

import dev.revere.alley.Alley;
import dev.revere.alley.game.event.Event;
import dev.revere.alley.game.event.enums.EventState;
import dev.revere.alley.game.event.enums.EventTeamSize;
import dev.revere.alley.game.event.enums.EventType;
import dev.revere.alley.game.event.impl.sumo.task.SumoRoundEndTask;
import dev.revere.alley.game.event.impl.sumo.task.SumoRoundStartTask;
import dev.revere.alley.game.event.map.EventMap;
import dev.revere.alley.game.event.participant.EventParticipant;
import dev.revere.alley.game.event.task.BaseEventTask;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
@Getter
public class SumoEvent extends Event {
    private EventParticipant participantA;
    private EventParticipant participantB;

    private final EventTeamSize teamSize;

    //TODO: methods for this event weren't made for duos and trios, so they need to be adjusted accordingly.

    /**
     * Constructor for the SumoEvent class.
     *
     * @param map      the event map.
     * @param host     the UUID of the host player.
     * @param teamSize the size of the teams in the event.
     */
    public SumoEvent(EventMap map, UUID host, EventTeamSize teamSize) {
        super(EventType.SUMO, map, 100, host);
        this.participantA = null;
        this.participantB = null;
        this.teamSize = teamSize;

        //TODO: handle team size logic, e.g. if team size is SOLO, only allow 2 players, if DUO, allow 4 players, etc.
        // also if a player joins the event with an existing party, split the party into teams based on the team size.
    }

    @Override
    public void handleDeath(Player player) {
        EventParticipant losingParticipant = this.getParticipant(player.getUniqueId());
        losingParticipant.setAlive(false);

        EventParticipant winningParticipant = this.participantA.getUuid().equals(player.getUniqueId()) ? this.participantB : this.participantA;
        winningParticipant.setAlive(true);

        //TODO: tp to arena spawn / start spectating

        //TODO: get cosmetic death message
        this.notifyParticipants("&c" + winningParticipant.getUsername() + " &fkilled &c" + losingParticipant.getUsername() + "&c!");

        this.setState(EventState.ENDING_ROUND);

        BaseEventTask task = new SumoRoundEndTask(this);
        this.setEventTask(task);
    }

    @Override
    public void handleRoundStart() {
        if (this.participantA == null || !this.participantA.isAlive()) {
            this.participantA = this.getRandomParticipant();
        } else {
            Player playerA = Alley.getInstance().getServer().getPlayer(this.participantA.getUuid());
            if (playerA != null) {
                this.initializePlayer(playerA);
                playerA.teleport(this.getMap().getPos1());
            }
        }

        if (this.participantB == null || !this.participantB.isAlive()) {
            this.participantB = this.getRandomParticipant();
        } else {
            Player playerB = Alley.getInstance().getServer().getPlayer(this.participantB.getUuid());
            if (playerB != null) {
                this.initializePlayer(playerB);
                playerB.teleport(this.getMap().getPos2());
            }
        }

        BaseEventTask task = new SumoRoundStartTask(this);
        this.setEventTask(task);
    }

    /**
     * Get a random participant besides participant A or B, based on who got eliminated.
     *
     * @return an eligible participant.
     */
    public EventParticipant getRandomParticipant() {
        List<EventParticipant> eligibleParticipants = this.getParticipants().values().stream()
                .filter(EventParticipant::isAlive)
                .filter(participant -> !participant.equals(this.participantA) && !participant.equals(this.participantB))
                .collect(Collectors.toList());

        if (eligibleParticipants.isEmpty()) {
            return null;
        }

        return eligibleParticipants.get(ThreadLocalRandom.current().nextInt(eligibleParticipants.size()));
    }
}