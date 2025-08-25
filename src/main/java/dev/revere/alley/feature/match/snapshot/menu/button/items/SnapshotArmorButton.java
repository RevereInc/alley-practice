package dev.revere.alley.feature.match.snapshot.menu.button.items;

import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.feature.match.snapshot.Snapshot;
import dev.revere.alley.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @date 07/10/2024
 */
@AllArgsConstructor
public class SnapshotArmorButton extends Button {
    private final Snapshot snapshot;
    private int armorPart;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.snapshot.getArmor()[this.armorPart]).build();
    }
}