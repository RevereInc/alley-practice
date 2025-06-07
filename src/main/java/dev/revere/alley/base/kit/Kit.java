package dev.revere.alley.base.kit;

import dev.revere.alley.base.kit.enums.EnumKitCategory;
import dev.revere.alley.base.kit.setting.KitSetting;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 28/04/2024 - 21:49
 */
@Getter
@Setter
public class Kit {
    private final String name;
    private String displayName;
    private String description;
    private String disclaimer;

    private boolean enabled;
    private boolean editable;

    private EnumKitCategory category;

    private Material icon;
    private int durability;

    private int unrankedSlot;
    private int rankedSlot;
    private int editorSlot;
    private int extraSlot;

    private String ffaArenaName;
    private boolean ffaEnabled;
    private int ffaSlot;
    private int maxFfaPlayers;

    private ItemStack[] items;
    private ItemStack[] armor;
    private ItemStack[] editorItems;

    private List<PotionEffect> potionEffects;
    private final List<KitSetting> kitSettings;

    /**
     * Constructor for the Kit class.
     *
     * @param name         The name of the kit.
     * @param displayName  The display name of the kit.
     * @param description  The description of the kit.
     * @param disclaimer   The disclaimer of the kit.
     * @param enabled      Whether the kit is enabled.
     * @param editable     Whether the kit is editable.
     * @param category     The category of the kit.
     * @param icon         The icon of the kit.
     * @param durability   The durability of the icon.
     * @param unrankedSlot The slot for unranked games.
     * @param rankedSlot   The slot for ranked games.
     * @param editorSlot   The slot for the editor.
     * @param ffaSlot      The slot for FFA games.
     * @param items        The items in the kit.
     * @param armor        The armor in the kit.
     * @param editorItems  The items in the editor.
     */
    public Kit(String name, String displayName, String description, String disclaimer, boolean enabled, boolean editable, EnumKitCategory category, Material icon, int durability, int unrankedSlot, int rankedSlot, int editorSlot, int extraSlot, int ffaSlot, ItemStack[] items, ItemStack[] armor, ItemStack[] editorItems) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.disclaimer = disclaimer;

        this.enabled = enabled;
        this.editable = editable;

        this.category = category;

        this.icon = icon;
        this.durability = durability;

        this.unrankedSlot = unrankedSlot;
        this.rankedSlot = rankedSlot;
        this.editorSlot = editorSlot;
        this.extraSlot = extraSlot;

        this.ffaArenaName = "";
        this.ffaEnabled = false;
        this.maxFfaPlayers = 20;
        this.ffaSlot = ffaSlot;

        this.items = items;
        this.armor = armor;
        this.editorItems = editorItems;

        this.kitSettings = new ArrayList<>();
        this.potionEffects = new ArrayList<>();
    }

    /**
     * Method to add a kit setting.
     *
     * @param kitSetting The kit setting to add.
     */
    public void addKitSetting(KitSetting kitSetting) {
        this.kitSettings.add(kitSetting);
    }

    /**
     * Method to check if a setting is enabled.
     *
     * @param name The name of the setting.
     * @return Whether the setting is enabled.
     */
    public boolean isSettingEnabled(String name) {
        KitSetting kitSetting = this.kitSettings.stream()
                .filter(setting -> setting.getName().equals(name))
                .findFirst()
                .orElse(null);

        return kitSetting != null && kitSetting.isEnabled();
    }

    /**
     * Method to check if a setting is enabled.
     *
     * @param clazz The class of the setting.
     * @return Whether the setting is enabled.
     */
    public boolean isSettingEnabled(Class<? extends KitSetting> clazz) {
        KitSetting kitSetting = this.kitSettings.stream()
                .filter(setting -> setting.getClass().equals(clazz))
                .findFirst()
                .orElse(null);

        return kitSetting != null && kitSetting.isEnabled();
    }

    /**
     * Method to apply the potion effects of the kit to a player.
     *
     * @param player The player to apply the potion effects to.
     */
    public void applyPotionEffects(Player player) {
        for (PotionEffect effect : this.potionEffects) {
            player.addPotionEffect(effect);
        }
    }
}