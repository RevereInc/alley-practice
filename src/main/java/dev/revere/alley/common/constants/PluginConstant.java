package dev.revere.alley.common.constants;

import dev.revere.alley.bootstrap.lifecycle.Service;
import org.bukkit.ChatColor;
import org.reflections.Reflections;

import java.util.List;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface PluginConstant extends Service {
    /**
     * Gets the constant plugin name defined in the plugin's description.
     *
     * @return The plugin name.
     */
    String getName();

    /**
     * Retrieves the constant plugin version from the plugin's description.
     *
     * @return The plugin version.
     */
    String getVersion();

    /**
     * Method to access the plugin metadata description.
     *
     * @return The plugin description.
     */
    String getDescription();

    /**
     * Gets the list of authors defined in the plugin's metadata.
     *
     * @return A list of author names.
     */
    List<String> getAuthors();

    /**
     * Gets the Spigot API version the server is running.
     *
     * @return The Spigot version string.
     */
    String getSpigotVersion();

    /**
     * Gets the main color defined for the plugin.
     *
     * @return The main ChatColor.
     */
    ChatColor getMainColor();

    /**
     * Method to retrieve the package directory of the plugin.
     *
     * @return The package directory as a string.
     */
    String getPackageDirectory();

    /**
     * Gets the admin permission prefix for the plugin.
     *
     * @return The admin permission prefix.
     */
    String getAdminPermissionPrefix();

    /**
     * Gets the message to display when a player lacks the required permission.
     *
     * @return The permission lack message.
     */
    String getPermissionLackMessage();

    /**
     * Gets the Reflections instance for classpath scanning.
     *
     * @return The Reflections object.
     */
    Reflections getReflections();
}