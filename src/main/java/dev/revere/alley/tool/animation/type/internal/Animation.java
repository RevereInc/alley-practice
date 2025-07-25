package dev.revere.alley.tool.animation.type.internal;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 03/04/2025
 */
public abstract class Animation {
    private final List<String> frames;

    private final long updateInterval;
    private long lastUpdateTime;

    private int frameIndex;

    /**
     * Constructor for the AbstractAnimation class.
     *
     * @param frames         The frames of the animation.
     * @param updateInterval The interval in milliseconds between animation frames.
     */
    protected Animation(List<String> frames, long updateInterval) {
        this.frames = frames;
        this.updateInterval = updateInterval;
        this.frameIndex = 0;
        this.lastUpdateTime = System.currentTimeMillis();
    }

    /**
     * Gets the current animation frame based on timing logic.
     *
     * @return The current animation frame.
     */
    public String getCurrentFrame() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - this.lastUpdateTime >= this.updateInterval) {
            this.frameIndex = (this.frameIndex + 1) % this.frames.size();
            this.lastUpdateTime = currentTime;
        }
        return this.frames.get(this.frameIndex);
    }
}