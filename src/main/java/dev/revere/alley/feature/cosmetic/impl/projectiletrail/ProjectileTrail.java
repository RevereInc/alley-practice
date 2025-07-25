package dev.revere.alley.feature.cosmetic.impl.projectiletrail;

import dev.revere.alley.feature.cosmetic.BaseCosmetic;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 6/23/2025
 */
public abstract class ProjectileTrail extends BaseCosmetic {
    /**
     * This method is called repeatedly by a trail-tracking task.
     * Implementations should define what particle to spawn at the projectile's location.
     *
     * @param location The current location of the projectile.
     */
    public abstract void spawnTrailParticle(Location location);

    @Override
    public void execute(Player player) {
    }
}