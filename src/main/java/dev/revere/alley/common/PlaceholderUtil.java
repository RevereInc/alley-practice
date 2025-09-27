package dev.revere.alley.common;

import dev.revere.alley.AlleyPlugin;
import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Emmy
 * @project alley-practice
 * @since 26/09/2025
 */
@UtilityClass
public class PlaceholderUtil {
    /**
     * Safely sets PlaceholderAPI placeholders if the plugin is enabled.
     * If PlaceholderAPI is not enabled, it returns the original messages.
     *
     * @param player   the player for whom to set the placeholders
     * @param messages the list of messages to set placeholders in
     * @return a list of messages with placeholders set, or the original messages if PlaceholderAPI is not enabled
     */
    public List<String> setPapiSafe(Player player, List<String> messages) {
        AlleyPlugin plugin = AlleyPlugin.getInstance();
        if (plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return PlaceholderAPI.setPlaceholders(player, messages);
        }

        return messages;
    }

    /**
     * Safely sets PlaceholderAPI placeholders if the plugin is enabled.
     * If PlaceholderAPI is not enabled, it returns the original message.
     *
     * @param player  the player for whom to set the placeholders
     * @param message the message to set placeholders in
     * @return a message with placeholders set, or the original message if PlaceholderAPI is not enabled
     */
    public String setPapiSafe(Player player, String message) {
        AlleyPlugin plugin = AlleyPlugin.getInstance();
        if (plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return PlaceholderAPI.setPlaceholders(player, message);
        }

        return message;
    }
}