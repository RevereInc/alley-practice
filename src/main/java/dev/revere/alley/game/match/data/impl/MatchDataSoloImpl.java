package dev.revere.alley.game.match.data.impl;

import dev.revere.alley.game.match.data.MatchData;
import lombok.Getter;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @since 29/05/2025
 */
@Getter
public class MatchDataSoloImpl extends MatchData {
    private final UUID winner;
    private final UUID loser;

    /**
     * Constructor for the MatchDataSoloImpl class.
     *
     * @param kit    The kit used in the match.
     * @param arena  The arena where the match took place.
     * @param winner The UUID of the winning player.
     * @param loser  The UUID of the losing player.
     */
    public MatchDataSoloImpl(String kit, String arena, UUID winner, UUID loser) {
        super(kit, arena);
        this.loser = loser;
        this.winner = winner;
    }
}