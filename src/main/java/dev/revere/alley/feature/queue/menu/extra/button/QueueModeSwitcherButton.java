package dev.revere.alley.feature.queue.menu.extra.button;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.feature.kit.KitCategory;
import dev.revere.alley.feature.queue.QueueService;
import dev.revere.alley.feature.queue.QueueType;
import dev.revere.alley.feature.queue.menu.extra.ExtraModesMenu;
import dev.revere.alley.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @since 01/05/2025
 */
@AllArgsConstructor
public class QueueModeSwitcherButton extends Button {
    private final QueueType queueType;
    private final KitCategory kitCategory;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.ARROW)
                .name("&6&l" + this.kitCategory.getName() + " Modes")
                .lore(
                        CC.MENU_BAR,
                        ("&f " + this.kitCategory.getDescription()),
                        "",
                        "&aClick to view.",
                        CC.MENU_BAR
                )
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        if (this.kitCategory == KitCategory.EXTRA) {
            new ExtraModesMenu(this.queueType).openMenu(player);
            return;
        }

        AlleyPlugin.getInstance().getService(QueueService.class).getQueueMenu().openMenu(player);
    }
}