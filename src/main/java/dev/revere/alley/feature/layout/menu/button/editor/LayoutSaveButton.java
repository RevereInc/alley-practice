package dev.revere.alley.feature.layout.menu.button.editor;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.layout.LayoutService;
import dev.revere.alley.feature.layout.data.LayoutData;
import dev.revere.alley.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @since 03/05/2025
 */
@AllArgsConstructor
public class LayoutSaveButton extends Button {
    private final Kit kit;
    private final LayoutData layout;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.WOOL)
                .name("&6&lSave")
                .durability(13)
                .lore(
                        CC.MENU_BAR,
                        "&7Save changes &",
                        "&7return to main menu.",
                        "",
                        "&aClick to save.",
                        CC.MENU_BAR
                )
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        Profile profile = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());

        LayoutData layout = profile.getProfileData().getLayoutData().getLayout(this.kit.getName(), this.layout.getName());
        layout.setDisplayName(this.layout.getDisplayName());
        layout.setItems(player.getInventory().getContents());

        AlleyPlugin.getInstance().getService(LayoutService.class).getLayoutMenu().openMenu(player);
    }
}