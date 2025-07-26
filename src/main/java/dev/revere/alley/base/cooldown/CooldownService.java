package dev.revere.alley.base.cooldown;

import dev.revere.alley.base.cooldown.enums.CooldownType;
import dev.revere.alley.plugin.lifecycle.Service;
import dev.revere.alley.tool.triple.impl.MutableTriple;

import java.util.List;
import java.util.UUID;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface CooldownService extends Service {
    List<MutableTriple<UUID, CooldownType, Cooldown>> getCooldowns();

    /**
     * Adds or updates a cooldown for a specific player and type.
     * If a cooldown of the same type already exists for the player, it is replaced.
     *
     * @param uuid     The UUID of the player.
     * @param type     The type of cooldown.
     * @param cooldown The Cooldown object containing duration information.
     */
    void addCooldown(UUID uuid, CooldownType type, Cooldown cooldown);

    /**
     * Retrieves an active cooldown for a player.
     *
     * @param uuid The UUID of the player.
     * @param type The type of cooldown to retrieve.
     * @return The Cooldown object if one is active, otherwise null.
     */
    Cooldown getCooldown(UUID uuid, CooldownType type);

    /**
     * Removes an active cooldown for a player.
     *
     * @param uuid The UUID of the player.
     * @param type The type of cooldown to remove.
     */
    void removeCooldown(UUID uuid, CooldownType type);
}