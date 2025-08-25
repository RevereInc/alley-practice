package dev.revere.alley.adapter.core;

import dev.revere.alley.bootstrap.lifecycle.Service;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface CoreAdapter extends Service {
    /**
     * Gets the name of the currently active core implementation.
     *
     * @return The Core implementation for the currently enabled core bootstrap.
     */
    Core getCore();
}