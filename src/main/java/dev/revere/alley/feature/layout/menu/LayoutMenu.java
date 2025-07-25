package dev.revere.alley.feature.layout.menu;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.base.kit.enums.KitCategory;
import dev.revere.alley.base.queue.QueueService;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.feature.layout.menu.button.LayoutButton;
import dev.revere.alley.feature.layout.menu.button.LayoutModeSwitcherButton;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @since 02/05/2025
 */
@AllArgsConstructor
public class LayoutMenu extends Menu {
    private KitCategory kitCategory;

    @Override
    public String getTitle(Player player) {
        return "&6&lLayout Editor";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int slot = 10;

        for (Queue queue : Alley.getInstance().getService(QueueService.class).getQueues()) {
            if (!queue.isRanked() && !queue.isDuos() && queue.getKit().getCategory() == this.kitCategory && queue.getKit().isEditable()) {
                slot = this.skipIfSlotCrossingBorder(slot);
                buttons.put(slot++, new LayoutButton(queue.getKit()));
            }
        }

        if (this.kitCategory == KitCategory.NORMAL) {
            buttons.put(40, new LayoutModeSwitcherButton(KitCategory.EXTRA));
        } else {
            buttons.put(4, new LayoutModeSwitcherButton(KitCategory.NORMAL));
        }

        this.addGlass(buttons, 15);

        return buttons;
    }

    @Override
    public int getSize() {
        if (this.kitCategory == KitCategory.EXTRA) {
            return 9 * 4;
        }

        return 9 * 5;
    }
}