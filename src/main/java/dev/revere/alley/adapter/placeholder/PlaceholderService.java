package dev.revere.alley.adapter.placeholder;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.bootstrap.lifecycle.Service;

/**
 * @author Emmy
 * @project alley-practice
 * @since 17/07/2025
 */
public interface PlaceholderService extends Service {
    /**
     * Registers a papi expansion bootstrap with the Alley bootstrap.
     *
     * @param plugin The Alley plugin instance to register.
     */
    void registerExpansion(AlleyPlugin plugin);
}