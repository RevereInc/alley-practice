package dev.revere.alley.base.hotbar.data;

import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.base.hotbar.enums.HotbarAction;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Emmy
 * @project alley-practice
 * @since 21/07/2025
 */
@Getter
@Setter
public class HotbarActionData {
    private HotbarAction action;
    private String command;
    private Menu menu;

    /**
     * Constructor for the HotbarActionData class.
     *
     * @param action The action to be performed by the hotbar item.
     */
    public HotbarActionData(HotbarAction action) {
        this.action = action;
        this.command = null;
        this.menu = null;
    }
}
