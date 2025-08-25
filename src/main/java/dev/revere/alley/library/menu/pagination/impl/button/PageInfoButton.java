package dev.revere.alley.library.menu.pagination.impl.button;

import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.library.menu.pagination.PaginatedMenu;
import dev.revere.alley.library.menu.pagination.impl.menu.ViewAllPagesMenu;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class PageInfoButton extends Button {
    private PaginatedMenu menu;

    @Override
    public ItemStack getButtonItem(Player player) {
        int pages = menu.getPages(player);

        return new ItemBuilder(Material.PAPER)
                .name(ChatColor.GOLD + "Page Info")
                .lore(
                        ChatColor.YELLOW + "You are viewing page #" + menu.getPage() + ".",
                        ChatColor.YELLOW + (pages == 1 ? "There is 1 page." : "There are " + pages + " pages."),
                        "",
                        ChatColor.YELLOW + "Middle click here to",
                        ChatColor.YELLOW + "view all pages."
                )
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType == ClickType.RIGHT) {
            new ViewAllPagesMenu(this.menu).openMenu(player);
            this.playNeutral(player);
        }
    }

}
