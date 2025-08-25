package dev.revere.alley.feature.queue.menu.extra;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.feature.kit.KitCategory;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.feature.queue.QueueService;
import dev.revere.alley.feature.queue.QueueType;
import dev.revere.alley.feature.queue.menu.button.UnrankedButton;
import dev.revere.alley.feature.queue.menu.extra.button.QueueModeSwitcherButton;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.library.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @since 01/05/2025
 */
@AllArgsConstructor
public class ExtraModesMenu extends Menu {
    private final QueueType queueType;

    @Override
    public String getTitle(Player player) {
        return "&6&l" + this.queueType.getMenuTitle();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(4, new QueueModeSwitcherButton(this.queueType, KitCategory.NORMAL));

        int slot = 10;
        for (Queue queue : AlleyPlugin.getInstance().getService(QueueService.class).getQueues()) {
            if (shouldAddQueue(queue, queueType)) {
                slot = this.skipIfSlotCrossingBorder(slot);
                buttons.put(slot++, new UnrankedButton(queue));
            }
        }

        this.addGlass(buttons, 15);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 4;
    }

    private boolean shouldAddQueue(Queue queue, QueueType queueType) {
        if (queue.isRanked() || queue.getKit().getCategory() != KitCategory.EXTRA) {
            return false;
        }

        return (queueType == QueueType.DUOS) == queue.isDuos();
    }
}