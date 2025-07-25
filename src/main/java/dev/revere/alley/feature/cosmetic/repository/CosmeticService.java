package dev.revere.alley.feature.cosmetic.repository;

import dev.revere.alley.plugin.lifecycle.Service;
import dev.revere.alley.feature.cosmetic.CosmeticType;

import java.util.Map;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface CosmeticService extends Service {
    Map<CosmeticType, BaseCosmeticRepository<?>> getRepositories();

    /**
     * Gets a specific cosmetic repository by its type.
     *
     * @param type The CosmeticType of the repository to retrieve.
     * @return The repository instance, or null if it's not registered.
     */
    BaseCosmeticRepository<?> getRepository(CosmeticType type);

    /**
     * A type-safe helper to get a specific cosmetic repository.
     *
     * @param type The CosmeticType of the repository.
     * @param repositoryClass The class of the repository for type casting.
     * @return The repository cast to its specific type, or null.
     */
    <T extends BaseCosmeticRepository<?>> T getRepository(CosmeticType type, Class<T> repositoryClass);
}