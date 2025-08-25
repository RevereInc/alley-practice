package dev.revere.alley.feature.match.snapshot.menu.button;

import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 27/06/2025
 */
@AllArgsConstructor
public class SnapshotDataButton extends Button {
    private final String name;
    private final List<String> lore;
    private final Material material;
    private final int durability;
    private final int amount;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.material)
                .name(this.name)
                .lore(this.lore)
                .durability(this.durability)
                .hideMeta()
                .amount(this.amount)
                .build();
    }
}