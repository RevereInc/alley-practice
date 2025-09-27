package dev.revere.alley.feature.explosives;

import dev.revere.alley.bootstrap.lifecycle.Service;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface ExplosiveService extends Service {
    /**
     * Gets the configured knockback range of TNT explosions.
     *
     * @return The configured knockback range of explosions.
     */
    double getTntExplosionRange();

    /**
     * Gets the configured horizontal knockback strength for fireballs.
     *
     * @return The configured horizontal knockback strength.
     */
    double getHorizontalFireballKnockback();

    /**
     * Gets the configured vertical knockback strength for fireballs.
     *
     * @return The configured vertical knockback strength.
     */
    double getVerticalFireballKnockback();

    /**
     * Gets the configured range value, possibly for explosion radius or effect distance.
     *
     * @return The configured range value (purpose may vary).
     */
    double getFireballExplosionRange();

    /**
     * Gets the configured speed value, likely for projectiles.
     *
     * @return The configured speed value, likely for projectiles.
     */
    double getFireballThrowSpeed();

    /**
     * Gets the configured fuse time for TNT in ticks.
     *
     * @return The configured fuse time for TNT in ticks.
     */
    int getTntFuseTicks();

    /**
     * Method to update the horizontal knockback value for fireballs.
     *
     * @param horizontalFireballKnockback The new horizontal knockback strength.
     */
    void setHorizontalFireballKnockback(double horizontalFireballKnockback);

    /**
     * Method to update the vertical knockback value for fireballs.
     *
     * @param verticalFireballKnockback The new vertical knockback strength.
     */
    void setVerticalFireballKnockback(double verticalFireballKnockback);

    /**
     * Method to update the explosion range value for fireballs.
     *
     * @param fireballExplosionRange The new explosion range for fireballs.
     */
    void setFireballExplosionRange(double fireballExplosionRange);

    /**
     * Method to update the throw speed value for fireballs.
     *
     * @param fireballThrowSpeed The new throw speed for fireballs.
     */
    void setFireballThrowSpeed(double fireballThrowSpeed);

    /**
     * Method to update the affected range value for TNT explosions.
     *
     * @param tntExplosionRange The new knockback range for explosions.
     */
    void setTntExplosionRange(double tntExplosionRange);

    /**
     * Method to update the fuse time for TNT in ticks.
     *
     * @param tntFuseTicks The new fuse time for TNT in ticks.
     */
    void setTntFuseTicks(int tntFuseTicks);

    /**
     * Saves the current explosive settings to the configuration file.
     */
    void save();
}