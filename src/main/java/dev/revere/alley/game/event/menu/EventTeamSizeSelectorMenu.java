package dev.revere.alley.game.event.menu;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.game.event.EventService;
import dev.revere.alley.game.event.enums.EventTeamSize;
import dev.revere.alley.game.event.enums.EventType;
import dev.revere.alley.tool.item.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project alley-practice
 * @since 31/07/2025
 */
@AllArgsConstructor
public class EventTeamSizeSelectorMenu extends Menu {
    private EventType eventType;

    @Override
    public String getTitle(Player player) {
        return "&6&lSelect Team Size";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (EventTeamSize eventTeamSize : EventTeamSize.values()) {
//            switch (eventTeamSize) {
//                case SOLO:
//                    buttons.put(0, new TeamSizeButton(this.eventType, eventTeamSize));
//                case DUO:
//                    buttons.put(1, new TeamSizeButton(this.eventType, eventTeamSize));
//                case TRIO:
//                    buttons.put(2, new TeamSizeButton(this.eventType, eventTeamSize));
//            }
            buttons.put(eventTeamSize.ordinal(), new TeamSizeButton(this.eventType, eventTeamSize));
        }

        return buttons;
    }

    @AllArgsConstructor
    private static class TeamSizeButton extends Button {
        private final EventType eventType;
        private final EventTeamSize eventTeamSize;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.PAPER)
                    .name("&6&l" + this.eventTeamSize.name() + " &7(" + this.eventTeamSize.getDisplayName() + ")")
                    .lore(
                            "&aClick to host!"
                    )
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            EventService eventService = Alley.getInstance().getService(EventService.class);
            eventService.startEvent(player, this.eventType, this.eventTeamSize);

            player.closeInventory();
        }
    }
}
