package dev.revere.alley.feature.arena.menu;

import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.internal.types.FreeForAllArena;
import dev.revere.alley.feature.arena.internal.types.StandAloneArena;
import dev.revere.alley.feature.arena.menu.button.ArenaPositionButton;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.library.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project alley-practice
 * @since 28/09/2025
 */
@AllArgsConstructor
public class ArenaPositionMenu extends Menu {
    private final Arena arena;

    @Override
    public String getTitle(Player player) {
        return "&6&lTeleport to...";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        Map<String, Location> positions = new LinkedHashMap<>();
        positions.put("Blue", this.arena.getPos1());
        positions.put("Red", this.arena.getPos2());
        positions.put("Center", this.arena.getCenter());
        positions.put("Maximum", this.arena.getMaximum());
        positions.put("Minimum", this.arena.getMinimum());

        if (this.arena instanceof FreeForAllArena) {
            FreeForAllArena ffaArena = (FreeForAllArena) this.arena;
            positions.put("Spawn", ffaArena.getPos1());
            positions.put("Safe Zone 1", ffaArena.getMinimum());
            positions.put("Safe Zone 2", ffaArena.getMaximum());
        } else if (this.arena instanceof StandAloneArena) {
            StandAloneArena standAloneArena = (StandAloneArena) this.arena;
            positions.put("Portal 1", standAloneArena.getTeam1Portal());
            positions.put("Portal 2", standAloneArena.getTeam2Portal());
        }

        positions.forEach((name, location) -> {
            buttons.put(buttons.size(), new ArenaPositionButton(name, location));
        });

        return buttons;
    }
}