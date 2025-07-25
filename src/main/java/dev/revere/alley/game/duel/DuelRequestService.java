package dev.revere.alley.game.duel;

import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.plugin.lifecycle.Service;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface DuelRequestService extends Service {
    /**
     * Gets a list of all currently pending duel requests.
     *
     * @return A list of DuelRequest objects.
     */
    List<DuelRequest> getDuelRequests();

    /**
     * The primary method for creating and sending a duel request from one player to another.
     * This handles validation, arena selection, and sending the invitation.
     *
     * @param sender        The player initiating the request.
     * @param initialTarget The player being challenged.
     * @param kit           The kit for the duel.
     * @param arena         The specific arena chosen, or null to select a random one.
     */
    void createAndSendRequest(Player sender, Player initialTarget, Kit kit, @Nullable Arena arena);

    /**
     * Accepts a pending duel request, leading to the creation of a match.
     *
     * @param duelRequest The duel request to accept.
     */
    void acceptPendingRequest(DuelRequest duelRequest);

    /**
     * Finds a pending duel request between two players.
     * The order of sender/target does not matter.
     *
     * @param playerOne The first player.
     * @param playerTwo The second player.
     * @return The DuelRequest object, or null if none is pending.
     */
    DuelRequest getDuelRequest(Player playerOne, Player playerTwo);
}