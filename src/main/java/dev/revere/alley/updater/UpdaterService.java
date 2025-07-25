package dev.revere.alley.updater;

import dev.revere.alley.plugin.lifecycle.Service;

import java.io.IOException;

/**
 * @author Remi
 * @project alley-practice
 * @date 24/07/2025
 */
public interface UpdaterService extends Service {
    /**
     * Checks for updates to the plugin by comparing the current version with the latest version available on GitHub.
     * If an update is available, it logs a warning message.
     */
    void checkForUpdates();

    /**
     * Downloads the latest version of the plugin and updates it.
     *
     * @param version The version to download and update to.
     */
    void downloadAndUpdate(String version);

    /**
     * Fetches the latest version of the plugin from GitHub.
     *
     * @return The latest version as a String.
     * @throws IOException If there is an error fetching the version.
     */
    String getLatestVersion() throws IOException;
}
