package dev.revere.alley.visual.scoreboard.internal.types.match.types;

import dev.revere.alley.visual.scoreboard.internal.types.match.BaseMatchScoreboard;
import dev.revere.alley.visual.scoreboard.internal.types.match.annotation.ScoreboardData;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
@ScoreboardData(isDefault = true)
public class MatchScoreboardRegular extends BaseMatchScoreboard {
    @Override
    protected String getSoloConfigPath() {
        return "scoreboard.lines.playing.solo.regular-match";
    }

    @Override
    protected String getTeamConfigPath() {
        return "scoreboard.lines.playing.team.regular-match";
    }
}