package dev.revere.alley.library.menu.pagination.impl.button;

import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.library.menu.MenuUtil;
import dev.revere.alley.library.menu.pagination.PaginatedMenu;
import dev.revere.alley.library.menu.pagination.impl.menu.ViewAllPagesMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @since 24/01/2025
 */
@AllArgsConstructor
public class PageButton extends Button {
    private PaginatedMenu menu;
    private int offset;

    @Override
    public ItemStack getButtonItem(Player player) {
        if (this.offset > 0) {
            if (MenuUtil.hasNext(player, offset, this.menu)) {
                return new ItemBuilder(Material.MELON)
                        .name("&6&lNext Page &7" + (this.menu.getPage() + "/" + this.menu.getPages(player)))
                        .lore(
                                CC.MENU_BAR,
                                "&7Right-Click:",
                                " &7▶ View all",
                                "",
                                "&aClick to view.",
                                CC.MENU_BAR
                        )
                        .hideMeta()
                        .build();
            } else {
                return new ItemBuilder(Material.MELON)
                        .name(CC.translate("&c&lNext Page"))
                        .lore(
                                CC.MENU_BAR,
                                " &cThere is no available",
                                " &cnext page.",
                                CC.MENU_BAR
                        )
                        .hideMeta()
                        .build();
            }
        } else {
            if (MenuUtil.hasPrevious(offset, this.menu)) {
                return new ItemBuilder(Material.SPECKLED_MELON)
                        .name("&6&lLast Page &7" + (this.menu.getPage() + "/" + this.menu.getPages(player)))
                        .lore(
                                CC.MENU_BAR,
                                "&7Right-Click:",
                                " &7▶ View all",
                                "",
                                "&aClick to view.",
                                CC.MENU_BAR
                        )
                        .hideMeta()
                        .build();
            } else {
                return new ItemBuilder(Material.SPECKLED_MELON)
                        .name(CC.translate("&c&lLast Page"))
                        .lore(
                                CC.MENU_BAR,
                                " &cThere is no available",
                                " &clast page.",
                                CC.MENU_BAR
                        )
                        .hideMeta()
                        .build();
            }
        }
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType == ClickType.RIGHT) {
            new ViewAllPagesMenu(this.menu).openMenu(player);
        } else if (clickType != ClickType.LEFT) {
            if (this.offset > 0) {
                if (MenuUtil.hasNext(player, offset, this.menu)) {
                    this.menu.modPage(player, this.offset);
                    this.playNeutral(player);
                } else {
                    this.playFail(player);
                }
            } else {
                if (MenuUtil.hasPrevious(offset, this.menu)) {
                    this.menu.modPage(player, this.offset);
                    this.playNeutral(player);
                } else {
                    this.playFail(player);
                }
            }
        }
    }
}