package dev.revere.alley.feature.layout.menu.button.editor;

import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.library.menu.Button;
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
public class LayoutResetItemsButton extends Button {
    private final Kit kit;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.WOOL)
                .name("&6&lReset Items")
                .durability(4)
                .lore(
                        CC.MENU_BAR,
                        "&7Reset items to default.",
                        "",
                        "&aClick to reset.",
                        CC.MENU_BAR
                )
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        player.getInventory().clear();
        player.getInventory().setContents(this.kit.getItems());
        player.updateInventory();
    }
}