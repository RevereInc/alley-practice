package dev.revere.alley.base.kit;

import dev.revere.alley.base.kit.enums.KitCategory;
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
    private String menuTitle;

    private boolean enabled;
    private boolean editable;

    private Material icon;
    private int durability;

    private ItemStack[] items;
    private ItemStack[] armor;
    private ItemStack[] editorItems;

    private String ffaArenaName;
    private boolean ffaEnabled;
    private int ffaSlot;
    private int maxFfaPlayers;

    private KitCategory category;

    private String knockbackProfile;
    private List<PotionEffect> potionEffects;
    private final List<KitSetting> kitSettings;

    /**
     * Constructor for the Kit class.
     *
     * @param name        The name of the kit.
     * @param displayName The display name of the kit.
     * @param description The description of the kit.
     * @param disclaimer  The disclaimer of the kit.
     * @param menuTitle   The title of the kit in the menu.
     * @param category    The category of the kit.
     * @param icon        The icon of the kit.
     * @param durability  The durability of the kit's icon.
     * @param items       The items in the kit.
     * @param armor       The armor in the kit.
     * @param editorItems The items used in the editor for this kit.
     */
    public Kit(String name, String displayName, String description, String disclaimer, String menuTitle, KitCategory category, Material icon, int durability, ItemStack[] items, ItemStack[] armor, ItemStack[] editorItems) {
        this.name = name;

        this.displayName = displayName;
        this.description = description;
        this.disclaimer = disclaimer;
        this.menuTitle = menuTitle;

        this.enabled = false;
        this.editable = true;

        this.category = category;

        this.icon = icon;
        this.durability = durability;

        this.items = items;
        this.armor = armor;
        this.editorItems = editorItems;

        this.ffaEnabled = false;
        this.ffaArenaName = "";
        this.maxFfaPlayers = 20;
        this.ffaSlot = 0;

        this.kitSettings = new ArrayList<>();
        this.potionEffects = new ArrayList<>();
        this.knockbackProfile = "";
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