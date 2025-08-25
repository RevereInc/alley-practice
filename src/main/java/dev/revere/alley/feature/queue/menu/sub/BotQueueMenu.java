package dev.revere.alley.feature.queue.menu.sub;

import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.feature.queue.menu.QueuesMenuDefault;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.library.menu.Menu;
import dev.revere.alley.library.menu.impl.BackButton;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 14/09/2024 - 23:58
 */
@AllArgsConstructor
public class BotQueueMenu extends Menu {
    private final Queue queue;

    @Override
    public String getTitle(Player player) {
        return "&6&lBot Queue";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new BackButton(new QueuesMenuDefault()));

        this.addBorder(buttons, 15, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}