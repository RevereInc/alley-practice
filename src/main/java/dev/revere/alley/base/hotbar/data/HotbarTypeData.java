package dev.revere.alley.base.hotbar.data;

import dev.revere.alley.base.hotbar.enums.HotbarType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Emmy
 * @project alley-practice
 * @since 21/07/2025
 */
@Getter
@Setter
public class HotbarTypeData {
    private HotbarType type;
    private int slot;

    private boolean enabled = false;

    /**
     * Constructor for the HotbarState class.
     *
     * @param type The name of the hotbar item type.
     * @param slot The hotbar slot of the type in the hotbar.
     */
    public HotbarTypeData(HotbarType type, int slot) {
        this.type = type;
        this.slot = slot;
    }
}