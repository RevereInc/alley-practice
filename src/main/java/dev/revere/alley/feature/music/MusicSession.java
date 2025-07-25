package dev.revere.alley.feature.music;

import dev.revere.alley.feature.music.enums.MusicDisc;
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
public class MusicSession {
    private final MusicDisc disc;
    private final long startTime;
    private final Location jukeboxLocation;

    @Setter private BukkitTask task;
    @Setter private int elapsedSeconds = 0;
    @Setter private boolean paused = false;

    public MusicSession(MusicDisc disc, Location jukeboxLocation) {
        this.disc = disc;
        this.startTime = System.currentTimeMillis();
        this.jukeboxLocation = jukeboxLocation;
    }

    public boolean isFinished() {
        return elapsedSeconds >= disc.getDuration();
    }
}