package dev.revere.alley.feature.cosmetic.internal.repository;

import dev.revere.alley.common.logger.Logger;
import dev.revere.alley.feature.cosmetic.model.BaseCosmetic;
import dev.revere.alley.feature.cosmetic.model.CosmeticRepository;
import dev.revere.alley.feature.cosmetic.model.CosmeticType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
        } catch (Exception exception) {
            Logger.error("Failed to register cosmetic class " + clazz.getSimpleName() + ": " + exception.getMessage());
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