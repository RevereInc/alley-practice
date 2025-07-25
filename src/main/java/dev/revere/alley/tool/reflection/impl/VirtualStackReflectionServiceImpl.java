package dev.revere.alley.tool.reflection.impl;

import dev.revere.alley.tool.reflection.Reflection;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;

/**
 * @author Emmy
 * @project alley-practice
 * @since 14/07/2025
 */
public class VirtualStackReflectionServiceImpl implements Reflection {
    /**
     * Sets the amount of the item in the player's hand,
     * even beyond normal max stack size using reflection.
     *
     * @param player the player whose held item to modify
     * @param amount the amount to set (can exceed normal stack max)
     * @throws Exception if reflection fails
     */
    public void setVirtualStackAmount(Player player, int amount) throws Exception {
        ItemStack item = player.getItemInHand();

        if (item == null || item.getType() == Material.AIR) {
            throw new IllegalArgumentException("Player must be holding an item.");
        }

        net.minecraft.server.v1_8_R3.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(item);

        Field countField = this.getField(nmsItemStack.getClass(), "count");
        this.setField(countField, nmsItemStack, amount);

        ItemStack newItemStack = CraftItemStack.asBukkitCopy(nmsItemStack);

        player.setItemInHand(newItemStack);
    }
}