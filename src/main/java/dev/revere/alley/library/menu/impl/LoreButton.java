package dev.revere.alley.library.menu.impl;

import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Emmy
 * @project alley-practice
 * @since 28/09/2025
 */
@AllArgsConstructor
public class LoreButton extends Button {
    private final Material material;
    private final String name;
    private final List<String> lore;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.material)
                .name(this.name)
                .lore(this.lore)
                .build();
    }
}
