package dev.revere.alley.library.menu.pagination.impl.button;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.library.menu.pagination.PaginatedMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class JumpToPageButton extends Button {
    private int page;
    private PaginatedMenu menu;
    private boolean current;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemStack itemStack = new ItemStack(this.current ? Material.ENCHANTED_BOOK : Material.BOOK, this.page);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(CC.translate("&6&lPage " + this.page));

        List<String> lore = new ArrayList<>();
        lore.add(CC.MENU_BAR);
        lore.add("");
        if (this.current) {
            lore.add(CC.translate("&aSelected."));
        } else {
            lore.add(CC.translate("&aClick to open."));
        }
        lore.add(CC.MENU_BAR);

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        this.menu.modPage(player, this.page - this.menu.getPage());
        this.playNeutral(player);
    }

}
