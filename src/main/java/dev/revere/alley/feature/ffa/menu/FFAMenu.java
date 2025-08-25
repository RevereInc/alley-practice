package dev.revere.alley.feature.ffa.menu;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.feature.ffa.FFAMatch;
import dev.revere.alley.feature.ffa.FFAService;
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
 * @date 23/05/2024 - 01:28
 */
@AllArgsConstructor
public class FFAMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&6&lSelect a FFA Queue";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new BackButton(new QueuesMenuDefault()));

        for (FFAMatch match : AlleyPlugin.getInstance().getService(FFAService.class).getMatches()) {
            buttons.put(match.getKit().getFfaSlot(), new FFAButton(match));
        }

        this.addBorder(buttons, 15, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }
}