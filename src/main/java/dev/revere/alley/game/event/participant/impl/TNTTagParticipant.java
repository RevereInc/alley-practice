package dev.revere.alley.game.event.participant.impl;

import dev.revere.alley.game.event.participant.EventParticipant;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 31/07/2025
 */
public class TNTTagParticipant extends EventParticipant {
    /**
     * Constructor for the EventParticipant class.
     *
     * @param uuid the player of the participant object.
     */
    public TNTTagParticipant(Player uuid) {
        super(uuid);
    }
}