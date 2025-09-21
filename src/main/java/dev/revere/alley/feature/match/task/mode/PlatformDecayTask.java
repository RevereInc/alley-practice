package dev.revere.alley.feature.match.task.mode;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.locale.internal.impl.message.GameMessagesLocaleImpl;
import dev.revere.alley.feature.arena.internal.types.StandAloneArena;
import dev.revere.alley.feature.match.Match;
import dev.revere.alley.feature.match.MatchState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Remi
 * @project alley-practice
 * @date 22/07/2025
 */
public class PlatformDecayTask extends BukkitRunnable {
    private final AlleyPlugin plugin;
    private final Match match;
    private final int initialRadius;
    private final int sideRadius;
    private int currentRadius;

    private final boolean isSlowedDown;
    private boolean notifiedAt75;
    private boolean notifiedAt50;
    private boolean notifiedAt25;

    private static final long FAST_INTERVAL = 4L;
    private static final long SLOW_INTERVAL = 40L;

    /**
     * Public constructor to start the decay process.
     */
    private PlatformDecayTask(Match match) {
        this.plugin = AlleyPlugin.getInstance();
        this.match = match;

        if (!(match.getArena() instanceof StandAloneArena)) {
            throw new IllegalArgumentException("PlatformDecayTask requires a StandAloneArena.");
        }

        int[] radii = this.calculateBuildRadii();
        this.sideRadius = radii[0];
        this.initialRadius = radii[1];
        this.currentRadius = this.initialRadius;
        this.isSlowedDown = false;
    }

    /**
     * Private constructor used only for rescheduling itself with preserved state.
     */
    private PlatformDecayTask(PlatformDecayTask oldTask) {
        this.plugin = oldTask.plugin;
        this.match = oldTask.match;
        this.initialRadius = oldTask.initialRadius;
        this.sideRadius = oldTask.sideRadius;
        this.currentRadius = oldTask.currentRadius;

        this.notifiedAt75 = oldTask.notifiedAt75;
        this.notifiedAt50 = oldTask.notifiedAt50;
        this.notifiedAt25 = oldTask.notifiedAt25;

        this.isSlowedDown = true;
    }

    /**
     * The static entry point to begin the decay task for a match.
     */
    public static void start(Match match) {
        new PlatformDecayTask(match).runTaskTimer(AlleyPlugin.getInstance(), 20L, FAST_INTERVAL);
    }

    @Override
    public void run() {
        LocaleService localeService = this.plugin.getService(LocaleService.class);
        if (match.getState() != MatchState.RUNNING) {
            return;
        }

        if (currentRadius <= 5) {
            match.sendMessage(localeService.getMessage(GameMessagesLocaleImpl.MATCH_PLATFORM_DECAY_WILL_NO_LONGER_DECAY));
            this.cancel();
            return;
        }

        StandAloneArena arena = (StandAloneArena) match.getArena();
        Location center = arena.getCenter();
        int centerX = center.getBlockX();
        int centerZ = center.getBlockZ();
        int minY = arena.getMinimum().getBlockY();
        int maxY = arena.getMaximum().getBlockY();

        for (int x = -currentRadius; x <= currentRadius; x++) {
            for (int z = -currentRadius; z <= currentRadius; z++) {
                if (Math.abs(x) + Math.abs(z) == currentRadius) {
                    for (int y = minY; y <= maxY; y++) {
                        Location blockLocation = new Location(center.getWorld(), centerX + x, y, centerZ + z);
                        if (blockLocation.getBlock().getType() != Material.AIR) {
                            blockLocation.getBlock().setType(Material.AIR);
                        }
                    }
                }
            }
        }

        handleNotifications();
        currentRadius--;

        if (!isSlowedDown && currentRadius <= sideRadius) {
            this.cancel();
            new PlatformDecayTask(this).runTaskTimer(this.plugin, SLOW_INTERVAL, SLOW_INTERVAL);
        }
    }

    private int[] calculateBuildRadii() {
        StandAloneArena arena = (StandAloneArena) match.getArena();
        Location center = arena.getCenter();
        Location minBound = arena.getMinimum();
        Location maxBound = arena.getMaximum();
        World world = center.getWorld();
        int centerX = center.getBlockX();
        int centerZ = center.getBlockZ();
        int foundEdgeX = centerX;
        scanLoopX:
        for (int x = maxBound.getBlockX(); x >= centerX; x--) {
            for (int y = maxBound.getBlockY(); y >= minBound.getBlockY(); y--) {
                if (world.getBlockAt(x, y, centerZ).getType() != Material.AIR) {
                    foundEdgeX = x;
                    break scanLoopX;
                }
            }
        }
        int foundEdgeZ = centerZ;
        scanLoopZ:
        for (int z = maxBound.getBlockZ(); z >= centerZ; z--) {
            for (int y = maxBound.getBlockY(); y >= minBound.getBlockY(); y--) {
                if (world.getBlockAt(centerX, y, z).getType() != Material.AIR) {
                    foundEdgeZ = z;
                    break scanLoopZ;
                }
            }
        }
        int radiusX = Math.abs(foundEdgeX - centerX);
        int radiusZ = Math.abs(foundEdgeZ - centerZ);
        int sideRadius = Math.max(radiusX, radiusZ);
        int cornerRadius = radiusX + radiusZ;
        return new int[]{sideRadius, cornerRadius};
    }

    private void handleNotifications() {
        LocaleService localeService = this.plugin.getService(LocaleService.class);
        if (initialRadius == 0) return;
        float remainingPercentage = ((float) currentRadius / initialRadius) * 100;
        if (remainingPercentage <= 25 && !notifiedAt25) {
            String message = localeService.getMessage(GameMessagesLocaleImpl.MATCH_PLATFORM_DECAY_NOTIFICATION_25_FORMAT);
            Sound sound = Sound.valueOf(localeService.getMessage(GameMessagesLocaleImpl.MATCH_PLATFORM_DECAY_NOTIFICATION_25_SOUND));
            sendMessageAndSound(message, sound);
            notifiedAt25 = true;
        } else if (remainingPercentage <= 50 && !notifiedAt50) {
            String message = localeService.getMessage(GameMessagesLocaleImpl.MATCH_PLATFORM_DECAY_NOTIFICATION_50_FORMAT);
            Sound sound = Sound.valueOf(localeService.getMessage(GameMessagesLocaleImpl.MATCH_PLATFORM_DECAY_NOTIFICATION_50_SOUND));
            sendMessageAndSound(message, sound);
            notifiedAt50 = true;
        } else if (remainingPercentage <= 75 && !notifiedAt75) {
            String message = localeService.getMessage(GameMessagesLocaleImpl.MATCH_PLATFORM_DECAY_NOTIFICATION_75_FORMAT);
            Sound sound = Sound.valueOf(localeService.getMessage(GameMessagesLocaleImpl.MATCH_PLATFORM_DECAY_NOTIFICATION_75_SOUND));
            sendMessageAndSound(message, sound);
            notifiedAt75 = true;
        }
    }

    private void sendMessageAndSound(String message, Sound sound) {
        match.sendMessage(CC.translate(message));
        match.playSound(sound);
    }
}