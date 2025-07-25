package dev.revere.alley.game.match.snapshot.menu.button;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.game.match.snapshot.SnapshotService;
import dev.revere.alley.game.match.snapshot.Snapshot;
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
 * @date 07/10/2024
 */
@AllArgsConstructor
public class SnapshotOpponentButton extends Button {
    private final Snapshot snapshot;

    @Override
    public ItemStack getButtonItem(Player player) {
        Snapshot opponentSnapshot = Alley.getInstance().getService(SnapshotService.class).getSnapshot(this.snapshot.getOpponent());
        if (opponentSnapshot == null) {
            return new ItemBuilder(Material.BARRIER)
                    .name(CC.translate("&cOpponent Not Found"))
                    .lore("&7The opponent's snapshot could not be found.")
                    .hideMeta()
                    .build();
        }

        return new ItemBuilder(Material.PAPER)
                .name(CC.translate("&6View Opponent"))
                .lore("&7Click to view &6" + opponentSnapshot.getUsername() + "'s &7inventory.")
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        player.performCommand("inventory " + this.snapshot.getOpponent());
    }
}