package dev.revere.alley.common;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.EnumSet;
import java.util.Set;

/**
 * @author Emmy
 * @project Alley
 * @date 02/01/2025 - 19:13
 */
@UtilityClass
public class InventoryUtil {
    private final Set<Material> DYEABLE_BLOCKS = EnumSet.of(Material.WOOL, Material.STAINED_CLAY);
    private final Set<Material> LEATHER_ARMOR = EnumSet.of(
            Material.LEATHER_HELMET,
            Material.LEATHER_CHESTPLATE,
            Material.LEATHER_LEGGINGS,
            Material.LEATHER_BOOTS
    );

    /**
     * Applies a specified TeamColor to a player's inventory,
     * coloring any dyeable blocks and leather armor.
     *
     * @param player    The player whose inventory will be colored.
     * @param teamColor The TeamColor data to apply.
     */
    public void applyTeamColorToInventory(Player player, TeamColor teamColor) {
        if (player == null || !player.isOnline() || teamColor == null) {
            return;
        }

        PlayerInventory inventory = player.getInventory();

        colorItems(inventory.getContents(), teamColor);
        colorItems(inventory.getArmorContents(), teamColor);

        player.updateInventory();
    }

    /**
     * A private helper to iterate over an array of items and apply coloring.
     *
     * @param items     the array of items to color.
     * @param teamColor the TeamColor data to apply.
     */
    private void colorItems(ItemStack[] items, TeamColor teamColor) {
        for (ItemStack item : items) {
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }

            if (DYEABLE_BLOCKS.contains(item.getType())) {
                item.setDurability(teamColor.getBlockDataValue());
            }

            if (LEATHER_ARMOR.contains(item.getType())) {
                ItemMeta meta = item.getItemMeta();
                if (meta instanceof LeatherArmorMeta) {
                    LeatherArmorMeta leatherMeta = (LeatherArmorMeta) meta;
                    leatherMeta.setColor(teamColor.getArmorColor());
                    item.setItemMeta(leatherMeta);
                }
            }
        }
    }

    /**
     * Clone an array of ItemStacks to ensure deep copy.
     *
     * @param items the original array.
     * @return a cloned array.
     */
    public ItemStack[] cloneItemStackArray(ItemStack[] items) {
        if (items == null) return null;

        ItemStack[] cloned = new ItemStack[items.length];
        for (int i = 0; i < items.length; i++) {
            cloned[i] = items[i] != null ? items[i].clone() : null;
        }
        return cloned;
    }

    /**
     * Give a specific item to a player.
     *
     * @param player   the player to give the item to.
     * @param material the material of the item to give.
     */
    public void giveItem(Player player, Material material, int amount) {
        player.getInventory().addItem(new ItemStack(material, amount));
    }

    /**
     * Represents a set of color data used for team-based item coloring.
     * This is a public enum so it can be passed as a parameter from any class.
     */
    @Getter
    public enum TeamColor {
        BLUE(Color.fromRGB(0, 102, 255), (short) 11),
        RED(Color.fromRGB(255, 0, 0), (short) 14);

        private final Color armorColor;
        private final short blockDataValue;

        /**
         * Constructor for the TeamColor enum.
         *
         * @param armorColor     the color to apply to leather armor.
         * @param blockDataValue the durability value to apply to dyeable blocks.
         */
        TeamColor(Color armorColor, short blockDataValue) {
            this.armorColor = armorColor;
            this.blockDataValue = blockDataValue;
        }
    }
}