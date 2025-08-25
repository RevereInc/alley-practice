package dev.revere.alley.feature.queue.menu.sub;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.feature.kit.KitCategory;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.feature.queue.QueueService;
import dev.revere.alley.feature.queue.QueueType;
import dev.revere.alley.feature.queue.menu.QueuesMenuDefault;
import dev.revere.alley.feature.queue.menu.button.UnrankedButton;
import dev.revere.alley.feature.queue.menu.extra.button.QueueModeSwitcherButton;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.library.menu.Menu;
import dev.revere.alley.library.menu.impl.BackButton;
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
        for (Queue queue : AlleyPlugin.getInstance().getService(QueueService.class).getQueues()) {
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