package dev.revere.alley.feature.cosmetic.impl.projectiletrail;

import dev.revere.alley.feature.cosmetic.impl.projectiletrail.impl.FlameTrail;
import dev.revere.alley.feature.cosmetic.BaseCosmeticRepository;

/**
 * @author Remi
 * @project Alley
 * @date 6/23/2025
 */
public class ProjectileTrailRepository extends BaseCosmeticRepository<ProjectileTrail> {
    public ProjectileTrailRepository() {
        this.registerCosmetic(FlameTrail.class);
    }
}