package dev.revere.alley.feature.main.menu.button;

import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Function;

/**
 * @author Emmy
 * @project alley-practice
 * @since 28/09/2025
 */
@AllArgsConstructor
public class AlleyMainPanelFunctionButton extends Button {
    private final Material material;
    private final int durability;
    private final String name;
    private final List<String> lore;
    private final Function<Player, Void> action;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.material)
                .name(this.name)
                .lore(this.lore)
                .durability(this.durability)
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (this.action != null) {
            this.action.apply(player);
        }
    }
}