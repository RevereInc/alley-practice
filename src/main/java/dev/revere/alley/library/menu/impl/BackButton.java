package dev.revere.alley.library.menu.impl;

import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.library.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class BackButton extends Button {
    private Menu back;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.ARROW)
                .name("&c&lBack")
                .durability(0)
                .lore(
                        CC.MENU_BAR,
                        " &cClick here to return to",
                        " &cthe previous menu.",
                        CC.MENU_BAR
                )
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        this.playNeutral(player);
        this.back.openMenu(player);
    }
}