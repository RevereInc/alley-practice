package dev.revere.alley.game.event.participant;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Emmy
 * @project alley-practice
 * @since 29/07/2025
 */
@Getter
@Setter
public class EventParticipant {
    private final UUID uuid;
    private final String username;

    private boolean alive;

    private final List<TeamMember> teamMembers = new ArrayList<>();

    /**
     * Constructor for the EventParticipant class.
     *
     * @param uuid the player of the participant object.
     */
    public EventParticipant(Player uuid) {
        this.uuid = uuid.getUniqueId();
        this.username = uuid.getName();

        this.alive = true;

        TeamMember leader = new TeamMember(uuid);
        this.teamMembers.add(leader);
    }

    /**
     * Method to determine whether the participant is eliminated.
     *
     * @return true if all team members are dead, false otherwise.
     */
    public boolean isEliminated() {
        return this.teamMembers.stream().allMatch(TeamMember::isDead);
    }
}