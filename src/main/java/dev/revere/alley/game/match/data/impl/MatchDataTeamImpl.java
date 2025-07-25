package dev.revere.alley.game.match.data.impl;

import dev.revere.alley.game.match.data.MatchData;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @since 29/05/2025
 */
@Getter
public class MatchDataTeamImpl extends MatchData {
    private final List<UUID> players;
    private final List<UUID> opponentPlayers;

    private final List<UUID> winners;

    private final String winnerTeam;

    /**
     * Constructor for the MatchDataTeamImpl class.
     *
     * @param kit              The kit used in the match.
     * @param arena            The arena where the match took place.
     * @param players          The list of UUIDs of players on the team.
     * @param opponentPlayers  The list of UUIDs of players on the opposing team.
     * @param winners          The list of UUIDs of winning players.
     * @param winnerTeam       The name of the winning team.
     */
    public MatchDataTeamImpl(String kit, String arena, List<UUID> players, List<UUID> opponentPlayers, List<UUID> winners, String winnerTeam) {
        super(kit, arena);
        this.players = players;
        this.opponentPlayers = opponentPlayers;
        this.winners = winners;
        this.winnerTeam = winnerTeam;
    }

}