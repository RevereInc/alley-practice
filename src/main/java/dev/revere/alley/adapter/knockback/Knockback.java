package dev.revere.alley.adapter.knockback;

import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 26/04/2025
 */
public interface Knockback {
    /**
     * Retrieves the bootstrap name of the knockback implementation.
     *
     * @return The bootstrap name as a String.
     */
    KnockbackType getType();

    /**
     * Applies knockback to a given player based on the specified profile.
     *
     * @param player  The player to whom knockback is to be applied.
     * @param profile The profile determining the knockback settings.
     */
    void applyKnockback(Player player, String profile);
}