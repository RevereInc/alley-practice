package dev.revere.alley.feature.layout.menu.button.editor;

import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.layout.data.LayoutData;
import dev.revere.alley.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @since 03/05/2025
 */
@AllArgsConstructor
public class LayoutDeleteButton extends Button {
    private final LayoutData layout;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.INK_SACK)
                .name("&c&lDelete Layout")
                .durability(1)
                .lore(
                        CC.MENU_BAR,
                        "&7Warning: Permanent!",
                        "",
                        "&aClick to delete.",
                        CC.MENU_BAR
                )
                .hideMeta()
                .build();
    }
}
