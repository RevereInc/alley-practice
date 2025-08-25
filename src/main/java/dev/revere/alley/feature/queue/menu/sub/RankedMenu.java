package dev.revere.alley.feature.queue.menu.sub;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.feature.queue.QueueService;
import dev.revere.alley.feature.queue.menu.button.RankedButton;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.library.menu.Menu;
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

        for (Queue queue : AlleyPlugin.getInstance().getService(QueueService.class).getQueues()) {
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