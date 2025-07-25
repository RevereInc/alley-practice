package dev.revere.alley.profile.data.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.base.kit.KitService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.feature.layout.data.LayoutData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Alley
 * @since 02/05/2025
 */
@Getter
@Setter
public class ProfileLayoutData {
    private Map<String, List<LayoutData>> layouts;

    public ProfileLayoutData() {
        this.layouts = Alley.getInstance().getService(KitService.class).getKits().stream()
                .collect(Collectors.toMap(Kit::getName, kit -> {
                    List<LayoutData> defaultLayoutList = new ArrayList<>();
                    defaultLayoutList.add(new LayoutData("Layout1", "Layout 1", kit.getItems()));
                    return defaultLayoutList;
                }));
    }

    /**
     * Adds a new layout to the list.
     *
     * @param name        the name of the layout.
     * @param displayName the display name of the layout.
     * @param items       the items in the layout.
     */
    public void addLayout(String kitName, String name, String displayName, ItemStack[] items) {
        LayoutData newLayout = new LayoutData(name, displayName, items);

        if (this.layouts.containsKey(kitName)) {
            this.layouts.get(kitName).add(newLayout);
        } else {
            List<LayoutData> newList = new ArrayList<>();
            newList.add(newLayout);
            this.layouts.put(kitName, newList);
        }
    }

    /**
     * Accessor method to get the layout by name.
     *
     * @param kitName    the name of the kit.
     * @param layoutName the name of the layout.
     * @return the layout record if found, null otherwise.
     */
    public LayoutData getLayout(String kitName, String layoutName) {
        if (this.layouts.containsKey(kitName)) {
            for (LayoutData layout : this.layouts.get(kitName)) {
                if (layout.getName().equalsIgnoreCase(layoutName)) {
                    return layout;
                }
            }
        }
        return null;
    }
}