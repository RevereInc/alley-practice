package dev.revere.alley.provider.scoreboard.impl.match.impl;

import dev.revere.alley.provider.scoreboard.impl.match.BaseMatchScoreboard;
import dev.revere.alley.provider.scoreboard.impl.match.annotation.ScoreboardData;

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