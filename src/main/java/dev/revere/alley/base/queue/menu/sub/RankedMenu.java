package dev.revere.alley.base.queue.menu.sub;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.base.queue.QueueService;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.base.queue.menu.button.RankedButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 23/05/2024 - 01:28
 */
public class RankedMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&6&lSolo Ranked Queue";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int slot = 10;

        for (Queue queue : Alley.getInstance().getService(QueueService.class).getQueues()) {
            if (queue.isRanked()) {
                slot = this.skipIfSlotCrossingBorder(slot);
                buttons.put(slot++, new RankedButton(queue));
            }
        }

        this.addBorder(buttons, 15, 5);

        return buttons;
    }


    @Override
    public int getSize() {
        return 9 * 5;
    }
}