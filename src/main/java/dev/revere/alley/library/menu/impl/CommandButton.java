package dev.revere.alley.library.menu.impl;

import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 15/06/2024 - 23:57
 */
@AllArgsConstructor
public class CommandButton extends Button {
    private String command;
    private ItemStack itemStack;
    private List<String> lore;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.itemStack)
                .lore(this.lore)
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        player.performCommand(this.command);
    }
}