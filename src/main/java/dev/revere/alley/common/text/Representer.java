package dev.revere.alley.common.text;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

/**
 * A utility class for representing various elements with colors and styles.
 *
 * @author Emmy
 * @project alley-practice
 * @since 26/09/2025
 */
@UtilityClass
public class Representer {
    /**
     * Get a colored representation of a stage countdown.
     * Assumes stages are from 0 to 5, where 5 is the highest (green) and 0 is the lowest (red).
     *
     * @param stage the stage to represent (0-5)
     * @param bold  whether the representation should be bold or not.
     * @return the colored representation of the stage countdown.
     */
    public String colorizeCountdown(int stage, boolean bold) {
        ChatColor color;

        if (stage >= 5) {
            color = ChatColor.GREEN;
        } else if (stage == 4) {
            color = ChatColor.GREEN;
        } else if (stage == 3) {
            color = ChatColor.YELLOW;
        } else if (stage == 2) {
            color = ChatColor.YELLOW;
        } else {
            color = ChatColor.RED;
        }

        return CC.translate(color + (bold ? "&l" : "") + stage);
    }
}