package dev.revere.alley.feature.server.menu.button;

import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.library.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @since 09/03/2025
 */
public class ServiceClearLagButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.FEATHER)
                .name("&c&lClear Lag")
                .lore(
                        "&fThis will clear the lag",
                        "&ffor all players on the server.",
                        "",
                        "&cClick to clear!"
                )
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        // Clear lag code here
    }
}
