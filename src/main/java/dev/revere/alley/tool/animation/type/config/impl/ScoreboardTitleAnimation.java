package dev.revere.alley.tool.animation.type.config.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.tool.animation.type.config.TextAnimation;

/**
 * @author Emmy
 * @project Alley
 * @since 27/03/2025
 */
public class ScoreboardTitleAnimation extends TextAnimation {
    public ScoreboardTitleAnimation() {
        super(Alley.getInstance().getService(ConfigService.class).getScoreboardConfig(), "scoreboard.title");
    }
}