package dev.revere.alley.game.match.snapshot.menu;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.game.match.snapshot.Snapshot;
import dev.revere.alley.game.match.snapshot.menu.button.SnapshotDataButton;
import dev.revere.alley.game.match.snapshot.menu.button.SnapshotOpponentButton;
import dev.revere.alley.game.match.snapshot.menu.button.items.SnapshotArmorButton;
import dev.revere.alley.game.match.snapshot.menu.button.items.SnapshotInventoryButton;
import dev.revere.alley.util.chat.Symbol;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Alley
 * @since 27/06/2025
 */
@AllArgsConstructor
public class SnapshotMenu extends Menu {
    private final Snapshot snapshot;

    @Override
    public String getTitle(Player player) {
        String title = Alley.getInstance().getService(ConfigService.class).getMenusConfig().getString("menus.snapshot-menu.title", "&6&l{name}'s Inventory");
        return title.replace("{name}", this.snapshot.getUsername());
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(53, new SnapshotOpponentButton(this.snapshot));

        FileConfiguration config = Alley.getInstance().getService(ConfigService.class).getMenusConfig();
        String path = "menus.snapshot-menu.buttons";

        this.getInventoryContents(buttons);
        this.getArmorContents(config, path, buttons);
        this.getHitsDataButton(buttons, config, path);
        this.getPotionsDataButton(buttons, config, path);
        this.getPotionEffectsDataButton(config, path, buttons);
        this.getFoodLevelDataButton(buttons, config, path);
        this.getHealthDataButton(buttons, config, path);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 6;
    }

    /**
     * Gets the inventory contents for the snapshot menu.
     *
     * @param buttons The map of buttons to add the inventory contents to.
     */
    private void getInventoryContents(Map<Integer, Button> buttons) {
        ItemStack[] inventory = this.snapshot.getInventory();
        for (int i = 0; i < inventory.length; i++) {
            buttons.put(i, new SnapshotInventoryButton(inventory[i]));
        }
    }

    /**
     * Gets the armor contents for the snapshot menu.
     *
     * @param config  The configuration file containing the button settings.
     * @param path    The path in the configuration file where the button settings are located.
     * @param buttons The map of buttons to add the armor contents to.
     */
    private void getArmorContents(FileConfiguration config, String path, Map<Integer, Button> buttons) {
        List<Integer> armorSlots = config.getIntegerList(path + ".armor.slots");
        for (int i = 0; i < armorSlots.size(); i++) {
            buttons.put(armorSlots.get(i), new SnapshotArmorButton(this.snapshot, i));
        }
    }

    /**
     * Gets the hits data button for the snapshot menu.
     *
     * @param buttons The map of buttons to add the hits data button to.
     * @param config  The configuration file containing the button settings.
     * @param path    The path in the configuration file where the button settings are located.
     */
    private void getHitsDataButton(Map<Integer, Button> buttons, FileConfiguration config, String path) {
        buttons.put(51, new SnapshotDataButton(
                config.getString(path + ".hits.name").replace("{hits}", String.valueOf(this.snapshot.getTotalHits())),

                config.getStringList(path + ".hits.lore").stream().map(line -> line
                        .replaceAll("\\{critical_hits}", String.valueOf(this.snapshot.getCriticalHits()))
                        .replaceAll("\\{blocked_hits}", String.valueOf(this.snapshot.getBlockedHits()))
                        .replaceAll("\\{longest_combo}", String.valueOf(this.snapshot.getLongestCombo()))
                        .replaceAll("\\{w_taps}", String.valueOf(this.snapshot.getWTaps()))
                ).collect(Collectors.toList()),

                Material.getMaterial(config.getString(path + ".hits.item", "DIAMOND_SWORD")),
                config.getInt(path + ".hits.durability", 0),
                1
        ));
    }

    /**
     * Gets the potions data button for the snapshot menu.
     *
     * @param buttons The map of buttons to add the potions data button to.
     * @param config  The configuration file containing the button settings.
     * @param path    The path in the configuration file where the button settings are located.
     */
    private void getPotionsDataButton(Map<Integer, Button> buttons, FileConfiguration config, String path) {
        buttons.put(50, new SnapshotDataButton(
                config.getString(path + ".potions.name"),

                config.getStringList(path + ".potions.lore").stream().map(line -> line
                        .replaceAll("\\{potions_remaining}", String.valueOf(this.snapshot.getAmountOfPotionsInInventory()))
                        .replaceAll("\\{potions_thrown}", String.valueOf(this.snapshot.getThrownPotions()))
                        .replaceAll("\\{potions_wasted}", "N/A")
                        .replaceAll("\\{potions_missed}", String.valueOf(this.snapshot.getMissedPotions()))
                        .replaceAll("\\{potion_accuracy}", String.valueOf(this.snapshot.getPotionAccuracy()))
                        .replaceAll("\\{heart}", Symbol.HEART)
                ).collect(Collectors.toList()),

                Material.getMaterial(config.getString(path + ".potions.item", "POTION")),
                config.getInt(path + ".potions.durability", 16421),
                this.snapshot.getAmountOfPotionsInInventory()
        ));
    }

    /**
     * Gets the potions data button for the snapshot menu.
     *
     * @param config  The configuration file containing the button settings.
     * @param path    The path in the configuration file where the button settings are located.
     * @param buttons The map of buttons to add the potions data button to.
     */
    private void getPotionEffectsDataButton(FileConfiguration config, String path, Map<Integer, Button> buttons) {
        List<String> effectsLore = new ArrayList<>();
        if (this.snapshot.getPotionEffects().isEmpty()) {
            effectsLore.addAll(config.getStringList(path + ".potion_effects.empty-lore"));
        } else {
            effectsLore.add(config.getString(path + ".potion_effects.lore-header").replace("{count}", String.valueOf(this.snapshot.getPotionEffects().size())));
            effectsLore.add("");
            for (String effect : this.snapshot.getPotionEffects()) {
                effectsLore.add(config.getString(path + ".potion_effects.effect-line").replace("{effect}", effect));
            }
        }

        buttons.put(49, new SnapshotDataButton(
                config.getString(path + ".potion_effects.name"),
                effectsLore,
                Material.getMaterial(config.getString(path + ".potion_effects.item", "BREWING_STAND_ITEM")),
                config.getInt(path + ".potion_effects.durability", 0),
                1
        ));
    }

    /**
     * Gets the food level data button for the snapshot menu.
     *
     * @param buttons The map of buttons to add the food level button to.
     * @param config  The configuration file containing the button settings.
     * @param path    The path in the configuration file where the button settings are located.
     */
    private void getFoodLevelDataButton(Map<Integer, Button> buttons, FileConfiguration config, String path) {
        buttons.put(48, new SnapshotDataButton(
                config.getString(path + ".food.name"),

                config.getStringList(path + ".food.lore").stream().map(line -> line
                        .replaceAll("\\{food_level}", String.valueOf(this.snapshot.getFoodLevel()))
                        .replaceAll("\\{saturation}", "N/A")
                ).collect(Collectors.toList()),

                Material.getMaterial(config.getString(path + ".food.item", "COOKED_BEEF")),
                config.getInt(path + ".food.durability", 0),
                1
        ));
    }

    /**
     * Gets the health data button for the snapshot menu.
     *
     * @param buttons The map of buttons to add the health button to.
     * @param config  The configuration file containing the button settings.
     * @param path    The path in the configuration file where the button settings are located.
     */
    private void getHealthDataButton(Map<Integer, Button> buttons, FileConfiguration config, String path) {
        buttons.put(47, new SnapshotDataButton(
                config.getString(path + ".health.name"),

                config.getStringList(path + ".health.lore").stream().map(line -> line
                        .replaceAll("\\{health}", String.valueOf(this.snapshot.getHealth()))
                        .replaceAll("\\{absorption}", "N/A")
                        .replaceAll("\\{heart}", Symbol.HEART)
                ).collect(Collectors.toList()),

                Material.getMaterial(config.getString(path + ".health.item", "GOLDEN_APPLE")),
                config.getInt(path + ".health.durability", 0),
                1
        ));
    }
}