package dev.revere.alley.tool.visual;

import dev.revere.alley.Alley;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 21/04/2025
 */
@UtilityClass
public class LoreHelper {
    /**
     * Returns a string representation of a boolean value.
     *
     * @param value The boolean value to represent.
     */
    public String enabledOrDisabled(boolean value) {
        String returnValue = value ? "&bEnabled" : "&cDisabled";
        return ChatColor.translateAlternateColorCodes('&', "&f&l┃ " + returnValue);
    }

    /**
     * Returns a string representation of a boolean value indicating visibility.
     *
     * @param value The boolean value to represent.
     */
    public String shownOrHidden(boolean value) {
        String returnValue = value ? "&bShown" : "&cHidden";
        return ChatColor.translateAlternateColorCodes('&', "&f&l┃ " + returnValue);
    }

    /**
     * Returns a string representation of a boolean value indicating status.
     *
     * @param value The boolean value to represent.
     */
    public String status(boolean value) {
        String returnValue = value ? "&aEnabled" : "&cDisabled";
        return ChatColor.translateAlternateColorCodes('&', "&f● &bStatus: &f" + returnValue);
    }

    /**
     * Returns a string representation of a boolean value indicating a tick or cross.
     *
     * @param value The boolean value to represent.
     */
    public String tickOrCross(boolean value) {
        String returnValue = value ? "&a&l✔" : "&c&l✘";
        return ChatColor.translateAlternateColorCodes('&', "&f&l┃ " + returnValue);
    }

    /**
     * Represents equipment selection lore for a player based on a permission.
     *
     * @param player        The player to check.
     * @param permission    The permission required to select.
     * @param inUse         Whether the item is in use or not.
     * @param clickToAction The action to perform when clicked.
     */
    public String selectionLoreWithPermission(Player player, String permission, boolean inUse, String clickToAction) {
        if (player.hasPermission(permission) && inUse) {
            return "&a&lSELECTED";
        } else if (player.hasPermission(permission) && !inUse) {
            return "&aClick to " + clickToAction + "!";
        } else {
            return Alley.getInstance().getPluginConstant().getPermissionLackMessage();
        }
    }

    /**
     * Represents equipment selection lore for a player.
     *
     * @param inUse         Whether the item is in use or not.
     * @param clickToAction The action to perform when clicked.
     */
    public String selectionLore(boolean inUse, String clickToAction) {
        if (inUse) {
            return "&a&lSELECTED";
        } else {
            return "&aClick to " + clickToAction + "!";
        }
    }
}