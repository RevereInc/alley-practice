package dev.revere.alley.feature.explosives;

import dev.revere.alley.plugin.lifecycle.Service;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface ExplosiveService extends Service {
    /**
     * @return The configured knockback range of explosions.
     */
    double getExplosionRange();

    /**
     * @return The configured horizontal knockback strength.
     */
    double getHorizontal();

    /**
     * @return The configured vertical knockback strength.
     */
    double getVertical();

    /**
     * @return The configured range value (purpose may vary).
     */
    double getRange();

    /**
     * @return The configured speed value, likely for projectiles.
     */
    double getSpeed();

    /**
     * @return The configured fuse time for TNT in ticks.
     */
    int getTntFuseTicks();

    void setExplosionRange(double explosionRange);

    void setHorizontal(double horizontal);

    void setVertical(double vertical);

    void setRange(double range);

    void setSpeed(double speed);

    void setTntFuseTicks(int tntFuseTicks);

    /**
     * Saves the current explosive settings to the configuration file.
     */
    void save();
}