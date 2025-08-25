package dev.revere.alley.feature.match.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Getter
@Setter
public class GameParticipant<T extends GamePlayer> {
    // This is the leader of the participant
    private T leader;

    // These fields are all for game logic
    private boolean lostCheckpoint;
    private boolean bedBroken;
    private int teamHits;

    /**
     * Constructor for the GameParticipant class.
     *
     * @param leader The player.
     */
    public GameParticipant(T leader) {
        this.leader = leader;
    }

    /**
     * Gets the player associated with the participant.
     *
     * @return The player associated with the participant.
     */
    public List<T> getPlayers() {
        return this.leader.isDisconnected() ? Collections.emptyList() : Collections.singletonList(this.leader);
    }

    /**
     * Gets the player associated with the participant.
     *
     * @return The player associated with the participant.
     */
    public List<T> getAllPlayers() {
        return Collections.singletonList(this.leader);
    }

    /**
     * Gets the player associated with the participant.
     *
     * @return The player associated with the participant.
     */
    public int getPlayerSize() {
        return this.leader.isDead() ? 0 : 1;
    }

    /**
     * Gets the amount of players that are alive.
     *
     * @return The amount of players that are alive.
     */
    public int getAlivePlayerSize() {
        return this.leader.isDead() ? 0 : 1;
    }

    /**
     * Gets the conjoined names of the players in the participant.
     *
     * @return The conjoined names of the players in the participant.
     */
    public String getConjoinedNames() {
        return this.leader.getUsername();
    }


    /**
     * Adds a player to the team participant.
     *
     * @param player the player to add.
     */
    public void addPlayer(T player) {
        this.leader = player;
    }

    /**
     * Removes a player from the team participant.
     *
     * @param player The player to remove.
     */
    public void removePlayer(T player) {
        if (this.leader != null && this.leader.getUuid().equals(player.getUuid())) {
            this.leader = null;
        }
    }

    /**
     * Checks if all the players in the participant are dead.
     *
     * @return True if all the players are dead.
     */
    public boolean isAllDead() {
        return this.leader.isDead();
    }

    /**
     * Checks if all the players in the participant are eliminated.
     *
     * @return True if all the players are eliminated.
     */
    public boolean isAllEliminated() {
        return this.leader.isEliminated();
    }

    /**
     * Checks if the participant contains a player.
     *
     * @param uuid The UUID of the player.
     * @return True if the participant contains the player.
     */
    public boolean containsPlayer(UUID uuid) {
        return this.leader.getUuid().equals(uuid);
    }
}