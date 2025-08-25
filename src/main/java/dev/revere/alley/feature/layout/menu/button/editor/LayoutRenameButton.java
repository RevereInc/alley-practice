package dev.revere.alley.feature.layout.menu.button.editor;

import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.layout.data.LayoutData;
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
public class LayoutRenameButton extends Button {
    private final LayoutData layout;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.NAME_TAG)
                .name("&6&lRename Layout")
                .lore(
                        CC.MENU_BAR,
                        "&7Change the display",
                        "&7name of the layout.",
                        "",
                        "&aClick to rename",
                        CC.MENU_BAR
                )
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        player.sendMessage("&c&lThis feature is not yet implemented!");
    }
}
