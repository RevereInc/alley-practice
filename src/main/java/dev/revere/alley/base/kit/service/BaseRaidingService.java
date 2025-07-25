package dev.revere.alley.base.kit.service;

import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.plugin.lifecycle.Service;
import dev.revere.alley.game.match.player.enums.BaseRaiderRole;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface BaseRaidingService extends Service {
    /**
     * Sets or updates the kit used for a specific role within a parent raiding kit.
     * This change is immediately saved to the configuration.
     *
     * @param parentKit The main raiding kit.
     * @param role      The role to map.
     * @param roleKit   The kit to assign to the role.
     */
    void setRaidingKitMapping(Kit parentKit, BaseRaiderRole role, Kit roleKit);

    /**
     * Removes a role-specific kit mapping from a parent raiding kit.
     *
     * @param parentKit The main raiding kit.
     * @param role      The role mapping to remove.
     */
    void removeRaidingKitMapping(Kit parentKit, BaseRaiderRole role);

    /**
     * Gets the assigned sub-kit for a specific role within a parent kit.
     *
     * @param parentKit The parent raiding kit.
     * @param role      The raider role.
     * @return The corresponding Kit, or null if no mapping exists.
     */
    Kit getRaidingKitByRole(Kit parentKit, BaseRaiderRole role);

    /**
     * Finds the first available parent kit configured for Raiding mode.
     *
     * @return The first raiding-enabled Kit found, or null if none exist.
     */
    Kit getRaidingKit();

    /**
     * Checks if a given kit is configured as a parent for Raiding mode.
     *
     * @param kit The kit to check.
     * @return True if the kit has raiding role mappings.
     */
    boolean hasRaidingKit(Kit kit);
}