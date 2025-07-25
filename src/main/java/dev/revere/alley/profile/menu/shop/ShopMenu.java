package dev.revere.alley.profile.menu.shop;

import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.feature.cosmetic.CosmeticType;
import dev.revere.alley.profile.menu.shop.button.ShopCategoryButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Alley
 * @date 6/2/2024
 */
public class ShopMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "&6&lShop Menu";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(11, new ShopCategoryButton(CosmeticType.KILL_EFFECT, Material.DIAMOND_SWORD));
        buttons.put(12, new ShopCategoryButton(CosmeticType.SOUND_EFFECT, Material.NOTE_BLOCK));
        buttons.put(14, new ShopCategoryButton(CosmeticType.PROJECTILE_TRAIL, Material.ARROW));
        buttons.put(15, new ShopCategoryButton(CosmeticType.KILL_MESSAGE, Material.BOOK_AND_QUILL));

        this.addBorder(buttons, 15, 3);

        return buttons;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }
}
