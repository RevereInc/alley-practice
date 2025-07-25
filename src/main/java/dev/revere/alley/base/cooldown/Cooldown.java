package dev.revere.alley.base.cooldown;

import dev.revere.alley.base.cooldown.enums.CooldownType;
import dev.revere.alley.util.TaskUtil;
import lombok.Getter;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
@Getter
public class Cooldown {
    private final CooldownType type;
    private final Runnable actionToRun;
    private long startTime;
    private BukkitTask cooldownTask;

    /**
     * Constructor for the Cooldown class.
     *
     * @param type        the type of cooldown
     * @param actionToRun the action to run
     */
    public Cooldown(CooldownType type, Runnable actionToRun) {
        this.type = type;
        this.actionToRun = actionToRun;
        this.startTime = 0L;
    }

    /**
     * Calculate the end time of the cooldown.
     *
     * @return the end time of the cooldown
     */
    public long calculateEndTime() {
        return this.startTime + this.type.getCooldownDuration();
    }

    /**
     * Check if the cooldown is active.
     *
     * @return true if the cooldown is active, false otherwise
     */
    public boolean isActive() {
        return this.calculateEndTime() > System.currentTimeMillis() && this.cooldownTask != null;
    }

    /**
     * Reset the cooldown time and start a new task.
     */
    public void resetCooldown() {
        this.startTime = System.currentTimeMillis();
        this.cancelExistingTask();
        this.startNewCooldownTask();
    }

    /**
     * Cancel the cooldown.
     */
    public void cancelCooldown() {
        this.startTime = 0L;
        this.cancelExistingTask();
    }

    /**
     * Get the remaining time of the cooldown.
     *
     * @return the remaining time of the cooldown
     */
    public int remainingTime() {
        return (int) ((this.calculateEndTime() - System.currentTimeMillis()) / 1000);
    }

    /**
     * Get the remaining time of the cooldown in minutes.
     *
     * @return the remaining time of the cooldown in minutes
     */
    public String remainingTimeInMinutes() {
        return String.format("%02d:%02d", this.remainingTime() / 60, this.remainingTime() % 60);
    }

    /**
     * Get the remaining time of the cooldown in milliseconds.
     *
     * @return the remaining time of the cooldown in milliseconds
     */
    public long remainingTimeMillis() {
        long remaining = calculateEndTime() - System.currentTimeMillis();
        return Math.max(0, remaining);
    }

    /**
     * Cancel the existing task.
     */
    private void cancelExistingTask() {
        if (this.cooldownTask != null) {
            this.cooldownTask.cancel();
            this.cooldownTask = null;
        }
    }

    /**
     * Start a new cooldown task.
     */
    private void startNewCooldownTask() {
        this.cooldownTask = TaskUtil.runLaterAsync(() -> {
            this.actionToRun.run();
            this.cancelExistingTask();
        }, this.type.getCooldownDuration() / 50L);
    }
}