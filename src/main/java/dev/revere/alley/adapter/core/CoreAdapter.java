package dev.revere.alley.adapter.core;

import dev.revere.alley.plugin.lifecycle.Service;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface CoreAdapter extends Service {

    /**
     * Gets the active core implementation that was detected during startup.
     *
     * @return The Core implementation for the currently enabled core plugin.
     */
    Core getCore();
}