package dev.revere.alley.util;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @date 02/01/2025 - 19:13
 */
@UtilityClass
public class InventoryUtil {
    /**
     * Clone an array of ItemStacks to ensure deep copy.
     *
     * @param items the original array
     * @return a cloned array
     */
    public ItemStack[] cloneItemStackArray(ItemStack[] items) {
        if (items == null) return null;

        ItemStack[] cloned = new ItemStack[items.length];
        for (int i = 0; i < items.length; i++) {
            cloned[i] = items[i] != null ? items[i].clone() : null;
        }
        return cloned;
    }
}