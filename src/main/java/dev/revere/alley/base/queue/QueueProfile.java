package dev.revere.alley.base.queue;

import dev.revere.alley.Alley;
import dev.revere.alley.tool.reflection.ReflectionService;
import dev.revere.alley.tool.reflection.impl.ActionBarReflectionServiceImpl;
import dev.revere.alley.util.TimeUtil;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.chat.Symbol;
import lombok.Data;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Data
public class QueueProfile {
    private final int TICK_THRESHOLD = 6;
    private final int RANGE_INCREMENT = 10;
    private final int MAX_RANGE = 220;

    private final Queue queue;
    private final UUID uuid;
    private int range;
    private int ticks;
    private int elo;

    private long startTime = System.currentTimeMillis();

    /**
     * Constructor for the QueueProfile class.
     *
     * @param queue The queue.
     * @param uuid  The UUID of the queue.
     */
    public QueueProfile(Queue queue, UUID uuid) {
        this.queue = queue;
        this.uuid = uuid;
    }

    /**
     * Method to queue the player.
     *
     * @param player The player to queue.
     */
    public void queueRange(Player player) {
        this.ticks++;

        if (player != null) {
            String message = "&6" + this.queue.getQueueType() + " " + this.queue.getKit().getDisplayName() + " &7│ &f" + TimeUtil.getFormattedElapsedTime(getElapsedTime());
            ReflectionService reflectionService = Alley.getInstance().getService(ReflectionService.class);
            reflectionService.getReflectionService(ActionBarReflectionServiceImpl.class).sendMessage(player, message);
        }

        if (this.ticks % this.TICK_THRESHOLD != 0) {
            return;
        }

        if (!this.queue.isRanked()) {
            if (player != null) {
                Arrays.asList(
                        "",
                        "&6&l" + this.queue.getKit().getDisplayName(),
                        " &f● &6Ping Range: &fN/A",
                        "   &7Searching for match...",
                        ""
                ).forEach(line -> player.sendMessage(CC.translate(line)));
            }
            return;
        }

        if (this.range >= this.MAX_RANGE) {
            return;
        }

        int previousRange = this.range;
        this.range = Math.min(this.range + this.RANGE_INCREMENT, this.MAX_RANGE);

        if (this.range != previousRange) {
            if (player != null) {
                Arrays.asList(
                        "",
                        "&6&l" + this.queue.getKit().getDisplayName() + " &6&l" + Symbol.RANKED_STAR + "Ranked",
                        " &f● &6ELO Range: &f" + this.getMinimumElo() + " &7&l" + Symbol.ARROW_R + "&f " + this.getMaximumElo(),
                        " &f● &6Ping Range: &fN/A",
                        "   " + (this.range == this.MAX_RANGE ? "&c&lRANGE LIMIT REACHED..." : "&7Searching for match..."),
                        ""
                ).forEach(line -> player.sendMessage(CC.translate(line)));
            }
        }
    }

    /**
     * Method to get the minimum elo.
     *
     * @return The minimum elo.
     */
    private int getMinimumElo() {
        int minimumElo = this.elo - this.range;
        return Math.max(minimumElo, 0);
    }

    /**
     * Method to get the maximum elo.
     *
     * @return The maximum elo.
     */
    private int getMaximumElo() {
        int max = this.elo + this.range;
        return Math.min(max, 3000);
    }

    /**
     * Method to get the elapsed time.
     *
     * @return The elapsed time.
     */
    public long getElapsedTime() {
        return System.currentTimeMillis() - this.startTime;
    }
}