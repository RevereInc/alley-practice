package dev.revere.alley.plugin.lifecycle;

import dev.revere.alley.plugin.AlleyContext;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface Service {
    /**
     * Called by the AlleyContext *after* the service instance has been created
     * and its server dependencies are available, but not necessarily fully initialized.
     *
     * @param context The application context, for access to the plugin instance or other services.
     */
    default void setup(AlleyContext context) {
        // Default implementation: no-op
    }

    /**
     * Called by the AlleyContext *after* all services have been created and setup.
     * Use this for logic that requires other services to be fully operational,
     * such as registering listeners or loading data from other services.
     *
     * @param context The application context.
     */
    default void initialize(AlleyContext context) {
        // Default implementation: no-op
    }

    /**
     * Called by the AlleyContext during plugin shutdown.
     * Should be used to release resources, save data, etc.
     *
     * @param context The application context.
     */
    default void shutdown(AlleyContext context) {
        // Default implementation: no-op
    }
}