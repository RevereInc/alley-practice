package dev.revere.alley.common.item;

import dev.revere.alley.common.reflect.utility.ReflectionUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder implements Listener {
    private final ItemStack itemStack;
    private String command;
    private boolean commandEnabled = true;

    /**
     * Constructor for the ItemBuilder class.
     *
     * @param material The material type of the item.
     */
    public ItemBuilder(Material material) {
        itemStack = new ItemStack(material);
    }

    /**
     * Constructor for the ItemBuilder class.
     *
     * @param itemStack The ItemStack to be modified.
     */
    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Sets the amount of the item stack.
     *
     * @param amount The amount to set.
     * @return The current ItemBuilder instance for method chaining.
     */
    public ItemBuilder amount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    /**
     * Sets the display name of the item.
     *
     * @param name The display name to set.
     * @return The current ItemBuilder instance for method chaining.
     */
    public ItemBuilder name(String name) {
        ItemMeta meta = itemStack.getItemMeta();

        // Check if meta is null and create a new one if needed
        if (meta == null) {
            meta = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
        }

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        itemStack.setItemMeta(meta);

        return this;
    }

    /**
     * Adds a line to the item's lore.
     *
     * @param name The lore line to add.
     * @return The current ItemBuilder instance for method chaining.
     */
    public ItemBuilder lore(String name) {
        ItemMeta meta = itemStack.getItemMeta();
        List<String> lore = meta.getLore();

        if (lore == null) {
            lore = new ArrayList<>();
        }

        lore.add(ChatColor.translateAlternateColorCodes('&', name));
        meta.setLore(lore);

        itemStack.setItemMeta(meta);

        return this;
    }

    /**
     * Sets the lore of the item.
     *
     * @param lore The lore lines to set.
     * @return The current ItemBuilder instance for method chaining.
     */
    public ItemBuilder lore(String... lore) {
        List<String> toSet = new ArrayList<>();
        ItemMeta meta = itemStack.getItemMeta();

        for (String string : lore) {
            toSet.add(ChatColor.translateAlternateColorCodes('&', string));
        }

        meta.setLore(toSet);
        itemStack.setItemMeta(meta);

        return this;
    }

    /**
     * Sets the lore of the item.
     *
     * @param lore The lore lines to set.
     * @return The current ItemBuilder instance for method chaining.
     */
    public ItemBuilder lore(List<String> lore) {
        List<String> toSet = new ArrayList<>();
        ItemMeta meta = itemStack.getItemMeta();

        for (String string : lore) {
            toSet.add(ChatColor.translateAlternateColorCodes('&', string));
        }

        meta.setLore(toSet);
        itemStack.setItemMeta(meta);

        return this;
    }

    /**
     * Sets the durability of the item.
     *
     * @param durability The durability to set.
     * @return The current ItemBuilder instance for method chaining.
     */
    public ItemBuilder durability(int durability) {
        itemStack.setDurability((short) durability);
        return this;
    }

    /**
     * Adds an enchantment to the item.
     *
     * @param enchantment The enchantment to add.
     * @param level       The level of the enchantment.
     * @return The current ItemBuilder instance for method chaining.
     */
    public ItemBuilder enchantment(Enchantment enchantment, int level) {
        itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    /**
     * Adds an enchantment to the item with level 1.
     *
     * @param enchantment The enchantment to add.
     * @return The current ItemBuilder instance for method chaining.
     */
    public ItemBuilder enchantment(Enchantment enchantment) {
        itemStack.addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    /**
     * Sets the material type of the item.
     *
     * @param material The material type to set.
     * @return The current ItemBuilder instance for method chaining.
     */
    public ItemBuilder type(Material material) {
        itemStack.setType(material);
        return this;
    }

    /**
     * Clears the lore of the item.
     *
     * @return The current ItemBuilder instance for method chaining.
     */
    public ItemBuilder clearLore() {
        ItemMeta meta = itemStack.getItemMeta();

        meta.setLore(new ArrayList<>());
        itemStack.setItemMeta(meta);

        return this;
    }

    /**
     * Adds or removes a glowing effect to the item.
     *
     * @param glow True to add glow, false to remove.
     * @return The current ItemBuilder instance for method chaining.
     */
    public ItemBuilder glow(boolean glow) {
        ItemMeta meta = itemStack.getItemMeta();

        if (glow) {
            meta.addEnchant(Enchantment.LURE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            meta.removeEnchant(Enchantment.LURE);
            meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        itemStack.setItemMeta(meta);

        return this;
    }

    /**
     * Clears all enchantments from the item.
     *
     * @return The current ItemBuilder instance for method chaining.
     */
    public ItemBuilder clearEnchantments() {
        for (Enchantment e : itemStack.getEnchantments().keySet()) {
            itemStack.removeEnchantment(e);
        }

        return this;
    }

    /**
     * Hides all item meta-information (attributes from the item.
     *
     * @return The current ItemBuilder instance for method chaining.
     */
    public ItemBuilder hideMeta() {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.values());

        itemStack.setItemMeta(itemMeta);

        return this;
    }

    /**
     * Sets a command to be associated with the item.
     *
     * @param command The command to set.
     * @return The current ItemBuilder instance for method chaining.
     */
    public ItemBuilder command(String command) {
        this.command = command;
        return this;
    }

    /**
     * Enables or disables the command functionality for the item.
     *
     * @param enabled True to enable, false to disable.
     * @return The current ItemBuilder instance for method chaining.
     */
    public ItemBuilder commandEnabled(boolean enabled) {
        this.commandEnabled = enabled;
        return this;
    }

    /**
     * Sets the skull's texture based on a Base64 string.
     * This method uses reflect to apply custom textures.
     *
     * @param base64Texture The Base64 encoded texture string.
     * @return The ItemBuilder instance.
     * @throws IllegalArgumentException if the ItemStack is not a player skull.
     */
    public ItemBuilder setSkullTexture(String base64Texture) {
        if (itemStack.getType() != Material.SKULL_ITEM) {
            throw new IllegalArgumentException("ItemStack must be a skull to set a custom texture.");
        }

        if (this.itemStack.getDurability() != 3) {
            throw new IllegalArgumentException("ItemStack must be a player skull (durability 3) to set a custom texture.");
        }

        SkullMeta meta = ReflectionUtility.createSkullMeta(this.itemStack, base64Texture);

        this.itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the owner of the skull item.
     *
     * @param owner The name of the player whose skin will be used for the skull.
     * @return The current ItemBuilder instance for method chaining.
     * @throws IllegalArgumentException if the ItemStack is not a skull.
     */
    public ItemBuilder setSkull(String owner) {
        if (itemStack.getType() != Material.SKULL_ITEM) {
            throw new IllegalArgumentException("ItemStack must be a skull to set an owner.");
        }

        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwner(Bukkit.getPlayer(owner) != null ? Bukkit.getPlayer(owner).getName() : owner);
        itemStack.setItemMeta(meta);

        this.durability(3);

        return this;
    }

    /**
     * Constructs and returns the final ItemStack.
     *
     * @return The constructed ItemStack.
     */
    public ItemStack build() {
        ItemMeta meta = itemStack.getItemMeta();

        if (commandEnabled && command != null) {
            List<String> lore = meta.getLore();

            if (lore == null) {
                lore = new ArrayList<>();
            }

            lore.add("command:" + command);
            meta.setLore(lore);
        }

        itemStack.setItemMeta(meta);
        return itemStack;
    }
}