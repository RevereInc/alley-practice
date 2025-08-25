package dev.revere.alley.library.command;

import dev.revere.alley.bootstrap.lifecycle.Service;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface CommandFramework extends Service {
    /**
     * Manually registers all command methods within a given object instance.
     *
     * @param commandContainerObject The object instance containing @CommandData methods.
     */
    void registerCommands(Object commandContainerObject);

    /**
     * Manually unregisters all command methods from a given object instance.
     *
     * @param commandContainerObject The object instance to unregister.
     */
    void unregisterCommands(Object commandContainerObject);

    /**
     * Generates and registers the main help topic for the bootstrap.
     */
    void registerHelp();
}