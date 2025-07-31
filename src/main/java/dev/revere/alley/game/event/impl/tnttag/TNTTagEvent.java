package dev.revere.alley.game.event.impl.tnttag;

import dev.revere.alley.game.event.Event;
import dev.revere.alley.game.event.enums.EventType;
import dev.revere.alley.game.event.map.EventMap;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Emmy
 * @project alley-practice
 * @since 31/07/2025
 */
public class TNTTagEvent extends Event {
    /**
     * Constructor for the Event class.
     *
     * @param eventType  the type of the event.
     * @param map        the map for the event.
     * @param maxPlayers the maximum number of players allowed in the event.
     * @param hostUUID   the UUID of the host player.
     */
    public TNTTagEvent(EventType eventType, EventMap map, int maxPlayers, UUID hostUUID) {
        super(eventType, map, maxPlayers, hostUUID);
    }

    @Override
    public void startEvent() {

    }

    @Override
    public void stopEvent() {

    }

    @Override
    public void handleJoin(Player player, boolean notify) {

    }

    @Override
    public void handleLeave(Player player, boolean notify) {

    }

    @Override
    public void handleDeath(Player player) {

    }

    @Override
    public void handleNewRound() {

    }

    @Override
    public boolean canEventEnd() {
        return false;
    }
}