package dev.revere.alley.provider.scoreboard.impl.match;

import dev.revere.alley.game.match.Match;
import dev.revere.alley.game.match.enums.MatchState;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.provider.scoreboard.Scoreboard;
import dev.revere.alley.provider.scoreboard.impl.match.impl.state.MatchScoreboardEndingImpl;
import dev.revere.alley.provider.scoreboard.impl.match.impl.state.MatchScoreboardStartingImpl;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
public class MatchScoreboardImpl implements Scoreboard {
    private final MatchScoreboardRegistry registry;

    private final MatchScoreboardStartingImpl matchScoreboardStarting;
    private final MatchScoreboardEndingImpl matchScoreboardEnding;

    /**
     * Constructor for the MatchScoreboard class.
     * It instantiates the registry, which automatically discovers all providers.
     */
    public MatchScoreboardImpl() {
        this.registry = new MatchScoreboardRegistry();
        this.registry.initialize();

        this.matchScoreboardStarting = new MatchScoreboardStartingImpl();
        this.matchScoreboardEnding = new MatchScoreboardEndingImpl();
    }

    @Override
    public List<String> getLines(Profile profile) {
        return Collections.emptyList();
    }

    @Override
    public List<String> getLines(Profile profile, Player player) {
        Match match = profile.getMatch();

        GameParticipant<MatchGamePlayerImpl> you = match.getParticipant(player);
        GameParticipant<MatchGamePlayerImpl> opponent = match.getOpponent(player);
        if (you == null || opponent == null) {
            return Collections.emptyList();
        }

        if (match.getState() == MatchState.STARTING) {
            return matchScoreboardStarting.getLines(profile, player, you, opponent);
        }
        if (match.getState() == MatchState.ENDING_MATCH) {
            return matchScoreboardEnding.getLines(profile, player, you, opponent);
        }

        MatchScoreboard scoreboardImpl = registry.getScoreboard(match);
        if (scoreboardImpl == null) {
            return Collections.emptyList();
        }

        return scoreboardImpl.getLines(profile, player, you, opponent);
    }
}