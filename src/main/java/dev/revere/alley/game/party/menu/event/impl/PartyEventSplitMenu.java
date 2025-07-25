package dev.revere.alley.game.party.menu.event.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.base.queue.QueueService;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.game.party.menu.event.impl.button.PartyEventSplitButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 08/10/2024 - 18:38
 */
public class PartyEventSplitMenu extends Menu {
    protected final Alley plugin = Alley.getInstance();

    @Override
    public String getTitle(Player player) {
        return "&6&lSelect a kit";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        int slot = 10;
        for (Queue queue : Alley.getInstance().getService(QueueService.class).getQueues()) {
            if (!queue.isRanked() && !queue.isDuos() && queue.getKit().isEnabled()) {
                slot = this.skipIfSlotCrossingBorder(slot);
                buttons.put(slot++, new PartyEventSplitButton(queue.getKit()));
            }
        }

        this.addGlass(buttons, 15);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 6;
    }
}