package dev.revere.alley.feature.cosmetic.repository;

import dev.revere.alley.feature.cosmetic.BaseCosmetic;
import dev.revere.alley.feature.cosmetic.CosmeticType;
import dev.revere.alley.feature.cosmetic.interfaces.CosmeticRepository;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;

import java.util.*;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
@Getter
public abstract class BaseCosmeticRepository<T extends BaseCosmetic> implements CosmeticRepository<T> {
    private final Map<String, T> cosmeticsByName;

    public BaseCosmeticRepository() {
        this.cosmeticsByName = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }

    /**
     * Register a cosmetic class to the repository
     *
     * @param clazz The class to register
     */
    protected void registerCosmetic(Class<? extends T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            this.cosmeticsByName.put(instance.getName(), instance);
        } catch (Exception e) {
            Logger.error("Failed to register cosmetic class " + clazz.getSimpleName() + ": " + e.getMessage());
        }
    }

    @Override
    public CosmeticType getRepositoryType() {
        if (cosmeticsByName.isEmpty()) {
            return null;
        }
        return cosmeticsByName.values().iterator().next().getType();
    }

    @Override
    public List<T> getCosmetics() {
        return new ArrayList<>(this.cosmeticsByName.values());
    }

    @Override
    public T getCosmetic(String name) {
        return this.cosmeticsByName.get(name);
    }
}