package dev.revere.alley.game.match.snapshot.menu.button;

import lombok.AllArgsConstructor;
import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.util.item.ItemBuilder;
import dev.revere.alley.game.match.snapshot.Snapshot;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 07/10/2024
 */
@AllArgsConstructor
public class InventorySnapshotArmorButton extends Button {
    private final UUID target;
    private int armorPart;

    @Override
    public ItemStack getButtonItem(Player player) {
        Snapshot snapshot = Alley.getInstance().getSnapshotRepository().getSnapshot(target);
        return new ItemBuilder(snapshot.getArmor()[armorPart])
                .build();
    }
}
