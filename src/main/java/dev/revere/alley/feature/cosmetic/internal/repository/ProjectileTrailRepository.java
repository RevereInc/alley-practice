package dev.revere.alley.feature.cosmetic.internal.repository;

import dev.revere.alley.feature.cosmetic.internal.repository.impl.projectiletrail.FlameTrail;
import dev.revere.alley.feature.cosmetic.internal.repository.impl.projectiletrail.ProjectileTrail;

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