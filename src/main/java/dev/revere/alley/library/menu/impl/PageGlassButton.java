package dev.revere.alley.library.menu.impl;

import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class PageGlassButton extends Button {
    private int durability;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.STAINED_GLASS_PANE)
                .durability(this.durability)
                .build();
    }
}
