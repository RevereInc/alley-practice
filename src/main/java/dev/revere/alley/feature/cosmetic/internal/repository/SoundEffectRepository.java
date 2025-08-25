package dev.revere.alley.feature.cosmetic.internal.repository;

import dev.revere.alley.feature.cosmetic.internal.repository.impl.soundeffect.BaseSoundEffect;
import dev.revere.alley.feature.cosmetic.internal.repository.impl.soundeffect.ExplosionSoundEffect;
import dev.revere.alley.feature.cosmetic.internal.repository.impl.soundeffect.NoneSoundEffect;
import dev.revere.alley.feature.cosmetic.internal.repository.impl.soundeffect.StepSoundEffect;
import lombok.Getter;

/**
 * @author Remi
 * @project Alley
 * @date 01/06/2024
 */
@Getter
public class SoundEffectRepository extends BaseCosmeticRepository<BaseSoundEffect> {
    public SoundEffectRepository() {
        this.registerCosmetic(ExplosionSoundEffect.class);
        this.registerCosmetic(NoneSoundEffect.class);
        this.registerCosmetic(StepSoundEffect.class);
    }
}