package dev.revere.alley.profile.menu.reset;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.tool.item.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 02/01/2025 - 21:08
 */
@AllArgsConstructor
public class ResetConfirmMenu extends Menu {
    private final UUID uuid;

    @Override
    public String getTitle(Player player) {
        return "&cReset stats?";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(11, new ConfirmButton(this.uuid));
        buttons.put(15, new CancelButton());

        this.addGlass(buttons, 15);

        return buttons;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

    @AllArgsConstructor
    private static class ConfirmButton extends Button {
        private final UUID uuid;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.EMERALD_BLOCK)
                    .name("&aConfirm")
                    .lore(
                            "",
                            "&7Click here to confirm the stat",
                            "&7reset of " + Bukkit.getOfflinePlayer(this.uuid).getName() + ".",
                            "",
                            "&c&lWARNING:",
                            "&fThis is &nbig responsibility&f.",
                            "&fYou cannot undo this action",
                            "&fand if you abuse this, you will be",
                            "&fpermanently &c&l&nBLACKLISTED&f!",
                            "",
                            "&4You may only reset stats with confirmation of an owner."
                    )
                    .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            if (clickType != ClickType.LEFT) return;

            Alley.getInstance().getService(ProfileService.class).resetStats(player, this.uuid);
            player.closeInventory();
        }
    }

    @AllArgsConstructor
    private static class CancelButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.REDSTONE_BLOCK)
                    .name("&cCancel")
                    .lore(
                            "",
                            "&7Click here to cancel the reset."
                    )
                    .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            player.closeInventory();
        }
    }
}