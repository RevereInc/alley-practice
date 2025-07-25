package dev.revere.alley.feature.layout.menu.button;

import dev.revere.alley.api.menu.Button;
import dev.revere.alley.base.kit.enums.KitCategory;
import dev.revere.alley.feature.layout.menu.LayoutMenu;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @since 03/05/2025
 */
@AllArgsConstructor
public class LayoutModeSwitcherButton extends Button {
    private KitCategory kitCategory;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.ARROW)
                .name("&6&l" + this.kitCategory.getName() + " Modes")
                .lore(
                        CC.MENU_BAR,
                        ("&f " + this.kitCategory.getDescription()),
                        "",
                        "&aClick to view!",
                        CC.MENU_BAR
                )
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        if (this.kitCategory == KitCategory.EXTRA) {
            new LayoutMenu(KitCategory.EXTRA).openMenu(player);
            return;
        }

        new LayoutMenu(KitCategory.NORMAL).openMenu(player);
    }
}