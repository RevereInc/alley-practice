package dev.revere.alley.base.queue.menu.extra.button;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.base.kit.enums.KitCategory;
import dev.revere.alley.base.queue.QueueService;
import dev.revere.alley.base.queue.enums.QueueType;
import dev.revere.alley.base.queue.menu.extra.ExtraModesMenu;
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

        Alley.getInstance().getService(QueueService.class).getQueueMenu().openMenu(player);
    }
}