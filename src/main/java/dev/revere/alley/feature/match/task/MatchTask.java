package dev.revere.alley.feature.match.task;

import dev.revere.alley.feature.match.Match;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Remi
 * @project Alley
 * @date 5/25/2024
 */
@Getter
@Setter
public class MatchTask extends BukkitRunnable {
    private final MatchTaskManager manager;
    private final Match match;
    private int stage;

    /**
     * Constructor for the MatchTask class.
     *
     * @param match The match.
     */
    public MatchTask(Match match) {
        this.match = match;
        this.stage = 6;
        this.manager = new MatchTaskManager(match);
    }

    @Override
    public void run() {
        this.stage--;

        if (this.manager.endMatchIfTimeLimitExceeded()) {
            return;
        }

        switch (this.match.getState()) {
            case STARTING:
                this.manager.handleStartingStage();
                break;
            case RESTARTING_ROUND:
                this.manager.handleRestartingRoundStage();
                break;
            case ENDING_ROUND:
                this.manager.handleEndingRoundStage();
                break;
            case ENDING_MATCH:
                this.manager.handleEndingStage();
                break;
        }
    }
}