package dev.revere.alley.feature.title.menu;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.pagination.PaginatedMenu;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.feature.title.TitleService;
import dev.revere.alley.feature.title.record.TitleRecord;
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
public class TitleMenu extends PaginatedMenu {
    private final Profile profile;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&6&lYour Titles";
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

        Map<Kit, TitleRecord> titleMap = Alley.getInstance().getService(TitleService.class).getTitles();
        for (TitleRecord title : titleMap.values()) {
            slot = this.validateSlot(slot);
            buttons.put(slot++, new TitleButton(this.profile, title));
        }

        this.addGlassToAvoidedSlots(buttons);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}