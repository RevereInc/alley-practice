package dev.revere.alley.base.hotbar;

import dev.revere.alley.base.hotbar.data.HotbarActionData;
import dev.revere.alley.base.hotbar.data.HotbarTypeData;
import dev.revere.alley.base.hotbar.enums.HotbarAction;
import dev.revere.alley.base.hotbar.enums.HotbarType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project alley-practice
 * @since 21/07/2025
 */
@Getter
@Setter
public class HotbarItem {
    private String name;
    private String displayName;

    private List<String> lore;

    private Material material;
    private int durability;

    private List<HotbarTypeData> typeData;
    private HotbarActionData actionData;

    /**
     * Constructor for the HotbarItem class.
     *
     * @param name The name of the hotbar item.
     */
    public HotbarItem(String name) {
        this.name = name;
        this.displayName = "&6&l" + name + " &7(Right-Click)";
        this.lore = Collections.singletonList("&f" + name + " hotbar item.");

        this.material = Material.STONE;
        this.durability = 0;

        this.typeData = this.feedTypeData();
        this.actionData = new HotbarActionData(HotbarAction.RUN_COMMAND);
    }

    /**
     * Feeds the type data class with the default hotbar types.
     *
     * @return A list of HotbarTypeData objects representing the default hotbar types.
     */
    private List<HotbarTypeData> feedTypeData() {
        return Arrays.stream(HotbarType.values()).map(type -> new HotbarTypeData(type, -1)).collect(Collectors.toList());
    }
}