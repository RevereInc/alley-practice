package dev.revere.alley.base.queue.menu.extra;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.base.kit.enums.EnumKitCategory;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.base.queue.enums.EnumQueueType;
import dev.revere.alley.base.queue.menu.button.UnrankedButton;
import dev.revere.alley.base.queue.menu.extra.button.QueueModeSwitcherButton;
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
    private final EnumQueueType queueType;

    @Override
    public String getTitle(Player player) {
        return "&b&l" + this.queueType.getMenuTitle();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(4, new QueueModeSwitcherButton(this.queueType, EnumKitCategory.NORMAL));

        for (Queue queue : Alley.getInstance().getQueueService().getQueues()) {
            if (!queue.isRanked() && queue.getKit().getCategory() == EnumKitCategory.EXTRA) {
                buttons.put(queue.getKit().getUnrankedSlot(), new UnrankedButton(queue));
            }
        }

        this.addGlass(buttons, (byte) 15);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 4;
    }
}