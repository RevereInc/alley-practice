package dev.revere.alley.base.queue.menu.sub;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.api.menu.impl.BackButton;
import dev.revere.alley.base.kit.enums.KitCategory;
import dev.revere.alley.base.queue.QueueService;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.base.queue.enums.QueueType;
import dev.revere.alley.base.queue.menu.QueuesMenuDefault;
import dev.revere.alley.base.queue.menu.button.UnrankedButton;
import dev.revere.alley.base.queue.menu.extra.button.QueueModeSwitcherButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 23/05/2024 - 10:28
 */
public class UnrankedMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&6&lUnranked Queue";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new BackButton(new QueuesMenuDefault()));

        int slot = 10;
        for (Queue queue : Alley.getInstance().getService(QueueService.class).getQueues()) {
            if (!queue.isRanked() && queue.getKit().getCategory() == KitCategory.NORMAL) {
                slot = this.skipIfSlotCrossingBorder(slot);
                buttons.put(slot++, new UnrankedButton(queue));
            }
        }

        buttons.put(40, new QueueModeSwitcherButton(QueueType.UNRANKED, KitCategory.EXTRA));

        this.addBorder(buttons, 15, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}