package dev.revere.alley.feature.level.menu;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.pagination.PaginatedMenu;
import dev.revere.alley.feature.level.LevelService;
import dev.revere.alley.feature.level.data.LevelData;
import dev.revere.alley.profile.Profile;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @since 22/04/2025
 */
@AllArgsConstructor
public class LevelMenu extends PaginatedMenu {
    private final Profile profile;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&6&lLevels";
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        this.addGlassHeader(buttons, 15);

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int slot = 0;

        for (LevelData level : Alley.getInstance().getService(LevelService.class).getLevels()) {
            slot = this.validateSlot(slot);
            buttons.put(slot++, new LevelButton(this.profile, level));
        }

        this.addGlassToAvoidedSlots(buttons);

        return buttons;
    }
}