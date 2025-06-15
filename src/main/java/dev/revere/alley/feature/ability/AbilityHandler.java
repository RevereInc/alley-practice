package dev.revere.alley.feature.ability;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import dev.revere.alley.util.TimeUtil;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Remi
 * @project alley-practice
 * @date 13/06/2025
 */
public class AbilityHandler {
    private final Table<String, UUID, Long> cooldowns = HashBasedTable.create();

    /**
     * Checks if a specific ability has an active cooldown for a player.
     *
     * @param player  the player whose cooldown is being checked
     * @param ability the ability for which the cooldown is being checked
     * @return true if the player has an active cooldown for the ability, false otherwise
     */
    public boolean hasCooldown(Player player, String ability) {
        if (player == null || ability == null) {
            return false;
        }

        Long cooldownEnd = cooldowns.get(ability, player.getUniqueId());
        if (cooldownEnd == null) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        if (cooldownEnd <= currentTime) {
            cooldowns.remove(ability, player.getUniqueId());
            return false;
        }

        return true;
    }

    /**
     * Applies a cooldown to a specific ability for a player.
     *
     * @param player   the player to apply the cooldown to
     * @param ability  the ability for which the cooldown is applied
     * @param duration the duration of the cooldown in milliseconds
     */
    public void applyCooldown(Player player, String ability, long duration) {
        if (player == null || ability == null) {
            return;
        }

        if (duration <= 0) {
            removeCooldown(player, ability);
            return;
        }

        cooldowns.put(ability, player.getUniqueId(), System.currentTimeMillis() + duration);
    }

    /**
     * Removes a specific cooldown for a player.
     *
     * @param player  the player whose cooldown should be removed
     * @param ability the ability for which the cooldown should be removed
     */
    public void removeCooldown(Player player, String ability) {
        if (player == null || ability == null) {
            return;
        }

        cooldowns.remove(ability, player.getUniqueId());
    }

    /**
     * Removes all cooldowns for a specific player.
     *
     * @param player the player whose cooldowns should be removed
     */
    public void removeAllCooldowns(Player player) {
        if (player == null) {
            return;
        }
        UUID playerUniqueId = player.getUniqueId();

        cooldowns.columnKeySet().removeIf(uuid -> uuid.equals(playerUniqueId));
    }

    /**
     * Gets the remaining cooldown time for a specific ability of a player in milliseconds.
     *
     * @param player  the player whose cooldown is being checked
     * @param ability the ability for which the cooldown is being checked
     * @return the remaining cooldown time in milliseconds, or 0 if no cooldown is active
     */
    public long getRemainingCooldown(Player player, String ability) {
        if (player == null || ability == null) {
            return 0;
        }

        if (!hasCooldown(player, ability)) {
            return 0;
        }

        return cooldowns.get(ability, player.getUniqueId()) - System.currentTimeMillis();
    }

    /**
     * Gets the remaining cooldown time for a specific ability of a player in a human-readable format.
     *
     * @param player  the player whose cooldown is being checked
     * @param ability the ability for which the cooldown is being checked
     * @return a string representing the remaining cooldown time, or "0 seconds" if no cooldown is active
     */
    public String getCooldownRemaining(Player player, String ability) {
        long remaining = getRemainingCooldown(player, ability);
        return TimeUtil.millisToRoundedTime(remaining);
    }
}
