package dev.revere.alley.api.constant;

import dev.revere.alley.plugin.lifecycle.Service;
import org.bukkit.ChatColor;
import org.reflections.Reflections;

import java.util.List;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface PluginConstant extends Service {
    String getName();

    String getVersion();

    String getDescription();

    List<String> getAuthors();

    String getSpigotVersion();

    ChatColor getMainColor();

    String getPackageDirectory();

    String getAdminPermissionPrefix();

    String getPermissionLackMessage();

    /**
     * Gets the Reflections instance for classpath scanning.
     *
     * @return The Reflections object.
     */
    Reflections getReflections();
}