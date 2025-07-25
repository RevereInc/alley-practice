package dev.revere.alley.game.ffa;

import dev.revere.alley.base.arena.Arena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.plugin.lifecycle.Service;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface FFAService extends Service {
    /**
     * Gets a list of all active, persistent FFA matches.
     * @return An unmodifiable list of FFA matches.
     */
    List<FFAMatch> getMatches();

    /**
     * Gets a list of all kits that are enabled for FFA mode.
     * @return An unmodifiable list of FFA-enabled kits.
     */
    List<Kit> getFfaKits();

    /**
     * Creates a new FFA match with the given parameters.
     *
     * @param arena      The arena the match is being played in
     * @param kit        The kit the players are using
     * @param maxPlayers The maximum amount of players allowed in the match
     */
    void createFFAMatch(Arena arena, Kit kit, int maxPlayers);

    /**
     * Finds the FFA match that a specific player is currently in.
     * @param player The player to search for.
     * @return An Optional containing the AbstractFFAMatch if the player is in one.
     */
    Optional<FFAMatch> getMatchByPlayer(Player player);

    /**
     * Gets a persistent FFA match by its associated kit name.
     * @param kitName The name of the kit.
     * @return The AbstractFFAMatch for that kit, or null if none exists.
     */
    FFAMatch getFFAMatch(String kitName);

    /**
     * An overloaded method to find the FFA match a player is in.
     * @param player The player to search for.
     * @return The AbstractFFAMatch, or null if the player is not in one.
     */
    FFAMatch getFFAMatch(Player player);

    /**
     * Forcefully reloads all FFA matches. This will kick all current players
     * and re-initialize the matches based on the current kit configurations.
     */
    void reloadFFAKits();

    /**
     * Checks if a specific kit is eligible for FFA mode.
     * @param kit The kit to check.
     * @return true if the kit is eligible for FFA, false otherwise.
     */
    boolean isNotEligibleForFFA(Kit kit);
}