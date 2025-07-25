package dev.revere.alley.game.match.data;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Emmy
 * @project Alley
 * @since 29/05/2025
 */
@Getter
@Setter
public abstract class MatchData {
    private final String kit;
    private final String arena;

    private final long creationTime;
    private boolean ranked;

    /**
     * Constructor for the AbstractMatchData class.
     *
     * @param kit   The kit used in the match.
     * @param arena The arena where the match took place.
     */
    public MatchData(String kit, String arena) {
        this.kit = kit;
        this.arena = arena;
        this.creationTime = System.currentTimeMillis();
        this.ranked = false;
    }
}