package dev.revere.alley.feature.cosmetic.repository;

import lombok.Getter;
import dev.revere.alley.feature.cosmetic.impl.soundeffect.SoundEffectRepository;
import dev.revere.alley.feature.cosmetic.impl.killeffects.KillEffectRepository;
import dev.revere.alley.feature.cosmetic.interfaces.ICosmeticRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
@Getter
public class CosmeticRepository {
    private final Map<String, ICosmeticRepository<?>> cosmeticRepositories;

    public CosmeticRepository() {
        this.cosmeticRepositories = new HashMap<>();
        this.registerCosmeticRepository("KillEffect", new KillEffectRepository());
        this.registerCosmeticRepository("SoundEffect", new SoundEffectRepository());
    }

    /**
     * Register a cosmetic repository
     *
     * @param name       the name of the repository
     * @param repository the repository
     */
    private void registerCosmeticRepository(String name, ICosmeticRepository<?> repository) {
        this.cosmeticRepositories.put(name, repository);
    }
}