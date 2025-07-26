package dev.revere.alley.feature.cosmetic.impl.soundeffect;

import dev.revere.alley.feature.cosmetic.BaseCosmetic;
import dev.revere.alley.feature.cosmetic.impl.soundeffect.impl.ExplosionSoundEffect;
import dev.revere.alley.feature.cosmetic.impl.soundeffect.impl.NoneSoundEffect;
import dev.revere.alley.feature.cosmetic.impl.soundeffect.impl.StepSoundEffect;
import dev.revere.alley.feature.cosmetic.BaseCosmeticRepository;
import lombok.Getter;

/**
 * @author Remi
 * @project Alley
 * @date 01/06/2024
 */
@Getter
public class SoundEffectRepository extends BaseCosmeticRepository<BaseCosmetic> {
    public SoundEffectRepository() {
        this.registerCosmetic(ExplosionSoundEffect.class);
        this.registerCosmetic(NoneSoundEffect.class);
        this.registerCosmetic(StepSoundEffect.class);
    }
}