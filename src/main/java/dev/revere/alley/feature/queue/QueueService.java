package dev.revere.alley.feature.queue;

import dev.revere.alley.bootstrap.lifecycle.Service;
import dev.revere.alley.feature.queue.internal.QueueTask;
import dev.revere.alley.library.menu.Menu;

import java.util.List;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface QueueService extends Service {
    /**
     * Gets the list of all active queues.
     *
     * @return A list of Queue instances.
     */
    List<Queue> getQueues();

    /**
     * Gets the menu UI for players to select a queue.
     *
     * @return The active Menu instance for queues.
     */
    Menu getQueueMenu();

    /**
     * Clears and re-populates all queues based on the currently loaded kits.
     */
    void reloadQueues();

    /**
     * Gets the total number of players currently playing in a specific game type (e.g., Ranked, FFA).
     *
     * @param queueName The name of the game type.
     * @return The number of players.
     */
    int getPlayerCountOfGameType(String queueName);

    QueueTask getQueueTask();
}