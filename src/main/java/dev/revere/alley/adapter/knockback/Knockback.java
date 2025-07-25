package dev.revere.alley.adapter.knockback;

import dev.revere.alley.adapter.knockback.enums.KnockbackType;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 26/04/2025
 */
public interface Knockback {
    /**
     * Retrieves the plugin name of the knockback implementation.
     *
     * @return The plugin name as a String.
     */
    KnockbackType getType();

    void applyKnockback(Player player, String profile);
}