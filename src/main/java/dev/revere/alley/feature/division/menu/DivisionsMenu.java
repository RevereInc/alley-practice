package dev.revere.alley.feature.division.menu;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.feature.division.Division;
import dev.revere.alley.feature.division.DivisionService;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @since 25/01/2025
 */
public class DivisionsMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "&6&lDivisions";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        int slot = 10;
        for (Division division : Alley.getInstance().getService(DivisionService.class).getDivisions()) {
            buttons.put(slot++, new DivisionButton(division));
            if (slot == 17 || slot == 26 || slot == 35 || slot == 44 || slot == 53) {
                slot += 2;
            }
        }

        this.addBorder(buttons, 15, 6);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 6;
    }
}
