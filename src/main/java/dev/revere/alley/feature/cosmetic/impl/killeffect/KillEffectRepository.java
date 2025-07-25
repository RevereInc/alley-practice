package dev.revere.alley.feature.cosmetic.impl.killeffect;

import dev.revere.alley.feature.cosmetic.BaseCosmetic;
import dev.revere.alley.feature.cosmetic.impl.killeffect.impl.*;
import dev.revere.alley.feature.cosmetic.repository.BaseCosmeticRepository;
import lombok.Getter;

/**
 * @author Remi
 * @project Alley
 * @date 01/06/2024
 */
@Getter
public class KillEffectRepository extends BaseCosmeticRepository<BaseCosmetic> {
    public KillEffectRepository() {
        this.registerCosmetic(BloodKillEffect.class);
        this.registerCosmetic(BodyFallKillEffect.class);
        this.registerCosmetic(ExplosionKillEffect.class);
        this.registerCosmetic(FireworkKillEffect.class);
        this.registerCosmetic(HeartKillEffect.class);
        this.registerCosmetic(NoneKillEffect.class);
        this.registerCosmetic(ThunderKillEffect.class);
    }
}