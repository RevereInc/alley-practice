package dev.revere.alley.game.event.menu;

import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.game.event.enums.EventType;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 23/12/2024 - 19:31
 */
@AllArgsConstructor
public class EventMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&6&lChoose event type";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (EventType eventType : EventType.values()) {
            switch (eventType) {
                case SUMO:
                    buttons.put(12, new EventButton(EventType.SUMO));
                    break;
                default:
                    break;
            }
        }

        this.addGlass(buttons, 15);
        return buttons;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

    @AllArgsConstructor
    private static class EventButton extends Button {
        private EventType eventType;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(this.eventType.getIcon())
                    .name("&6&l" + this.eventType.getName())
                    .lore(
                            CC.MENU_BAR,
                            "&fClick to host a &6" + this.eventType.getName() + " &fevent.",
                            "",
                            " &f● &6Select a map",
                            " &f● &6Choose if solo or team",
                            CC.MENU_BAR
                    )
                    .durability(this.eventType.getDurability())
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            switch (this.eventType) {
                case SUMO:
                    new EventTeamSizeSelectorMenu(this.eventType).openMenu(player);
                    break;
                default:
                    player.sendMessage(CC.translate("&cThis event type is not implemented yet."));
                    break;
            }
        }
    }
}