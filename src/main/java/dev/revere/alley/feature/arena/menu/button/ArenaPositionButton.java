package dev.revere.alley.feature.arena.menu.button;

import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

/**
 * @author Emmy
 * @project alley-practice
 * @since 28/09/2025
 */
@AllArgsConstructor
public class ArenaPositionButton extends Button {
    private final String locationName;
    private final Location location;

    @Override
    public ItemStack getButtonItem(Player player) {
        if (location == null) {
            return new ItemBuilder(Material.BARRIER)
                    .name("&c&l" + this.locationName)
                    .lore(
                            CC.MENU_BAR,
                            "&c&lLocation",
                            " &c&l│ &fThis location is not set.",
                            "",
                            "&cArena setup incomplete.",
                            CC.MENU_BAR
                    )
                    .build();
        } else {
            return new ItemBuilder(Material.PAPER)
                    .name("&6&l" + this.locationName)
                    .lore(
                            CC.MENU_BAR,
                            "&6&lLocation",
                            " &6&l│ &fWorld: &6" + Objects.requireNonNull(this.location.getWorld()).getName(),
                            " &6&l│ &fX: &6" + this.location.getBlockX(),
                            " &6&l│ &fY: &6" + this.location.getBlockY(),
                            " &6&l│ &fZ: &6" + this.location.getBlockZ(),
                            " &6&l│ &fYaw: &6" + this.location.getYaw(),
                            " &6&l│ &fPitch: &6" + this.location.getPitch(),
                            "",
                            "&aClick to teleport!",
                            CC.MENU_BAR
                    )
                    .build();
        }
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        if (this.location == null) {
            player.sendMessage(this.getString(GlobalMessagesLocaleImpl.ERROR_INVALID_LOCATION));
            return;
        }

        player.teleport(this.location);
    }
}
