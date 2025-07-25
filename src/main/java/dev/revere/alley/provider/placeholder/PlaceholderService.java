package dev.revere.alley.provider.placeholder;

import dev.revere.alley.Alley;
import dev.revere.alley.plugin.lifecycle.Service;

/**
 * @author Emmy
 * @project alley-practice
 * @since 17/07/2025
 */
public interface PlaceholderService extends Service {
    /**
     * Registers a papi expansion plugin with the Alley plugin.
     *
     * @param plugin The Alley plugin instance to register.
     */
    void registerExpansion(Alley plugin);
}
