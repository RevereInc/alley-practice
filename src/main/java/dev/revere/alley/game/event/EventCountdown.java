package dev.revere.alley.game.event;

import lombok.Data;

/**
 * @author Emmy
 * @project alley-practice
 * @date 23/12/2024 - 11:56
 */
@Data
public class EventCountdown {
    private long start;
    private long duration;

    /**
     * Constructor for the EventCountdown class.
     *
     * @param duration the duration.
     */
    public EventCountdown(long duration) {
        this.start = System.currentTimeMillis();
        this.duration = this.start + duration;
    }

    /**
     * Checks if the countdown is over.
     *
     * @return true if the countdown is over, false otherwise.
     */
    public boolean isOver() {
        return System.currentTimeMillis() - this.duration >= 0;
    }
}