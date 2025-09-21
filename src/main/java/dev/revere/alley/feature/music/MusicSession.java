package dev.revere.alley.feature.music;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author Remi
 * @project alley-practice
 * @date 20/07/2025
 */

@Getter
@Setter
public class MusicSession {
    private final MusicDisc disc;
    private final long startTime;
    private final Location jukeboxLocation;

    private BukkitTask task;

    private int elapsedSeconds = 0;
    private boolean paused = false;

    /**
     * Constructor for the MusicSession class.
     *
     * @param disc            The music disc being played.
     * @param jukeboxLocation The location of the jukebox.
     */
    public MusicSession(MusicDisc disc, Location jukeboxLocation) {
        this.disc = disc;
        this.startTime = System.currentTimeMillis();
        this.jukeboxLocation = jukeboxLocation;
    }

    /**
     * Checks if the music session has finished playing the disc.
     *
     * @return true if the disc has finished playing, false otherwise.
     */
    public boolean isFinished() {
        return elapsedSeconds >= disc.getDuration();
    }
}