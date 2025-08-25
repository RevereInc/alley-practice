package dev.revere.alley.common.reflect;

import dev.revere.alley.bootstrap.lifecycle.Service;

import java.util.List;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface ReflectionService extends Service {
    /**
     * Gets a list of all discovered reflect service instances.
     *
     * @return An unmodifiable list of reflect services.
     */
    List<Reflection> getReflectionServices();

    /**
     * Retrieves a specific reflect service by its class type.
     *
     * @param serviceClass The class type of the service to retrieve.
     * @param <T>          The type of the reflect service.
     * @return The service instance, or null if not found.
     */
    <T extends Reflection> T getReflectionService(Class<T> serviceClass);
}