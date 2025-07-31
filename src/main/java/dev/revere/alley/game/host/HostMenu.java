package dev.revere.alley.game.host;

import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.game.event.menu.EventMenu;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 08/06/2024 - 21:19
 */
public class HostMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "&6&lHost Event";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(11, new HostButton("&6&lEvent", new ItemStack(Material.EMPTY_MAP), Arrays.asList(
                CC.MENU_BAR,
                "&fHost an event with different",
                "&funique implementations.",
                "",
                " &f● &6Types: &7Sumo",
                " &f● &cMore soon...",
                "",
                "&aClick to view!",
                CC.MENU_BAR
        )));

        buttons.put(15, new HostButton("&6&lTournament", new ItemStack(Material.BOW), Arrays.asList(
                CC.MENU_BAR,
                "&fHost a tournament to",
                "&fcompete in a number",
                "&fof duels to win.",
                "",
                "&aClick to host!",
                CC.MENU_BAR
        )));

        this.addGlass(buttons, 15);
        return buttons;
    }

    @AllArgsConstructor
    public static class HostButton extends Button {
        private String displayName;
        private ItemStack itemStack;
        private List<String> lore;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(this.itemStack)
                    .name(this.displayName)
                    .lore(this.lore)
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) {
                return;
            }

            switch (this.itemStack.getType()) {
                case BOW:
                    // Open the tournament menu
                    break;
                case EMPTY_MAP:
                    new EventMenu().openMenu(player);
                    break;
            }

            this.playNeutral(player);
        }
    }


    @Override
    public int getSize() {
        return 9 * 3;
    }
}