package dev.revere.alley.game.event.participant;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

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

    /**
     * Constructor for the EventParticipant class.
     *
     * @param uuid the player of the participant object.
     */
    public EventParticipant(Player uuid) {
        this.uuid = uuid.getUniqueId();
        this.username = uuid.getName();

        this.alive = true;
    }
}