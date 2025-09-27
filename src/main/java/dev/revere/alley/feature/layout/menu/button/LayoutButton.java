package dev.revere.alley.feature.layout.menu.button;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.constants.MessageConstant;
import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.common.text.Symbol;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.setting.types.mode.KitSettingRaiding;
import dev.revere.alley.feature.layout.data.LayoutData;
import dev.revere.alley.feature.layout.menu.LayoutEditorMenu;
import dev.revere.alley.feature.layout.menu.LayoutSelectRoleKitMenu;
import dev.revere.alley.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @since 03/05/2025
 */
@AllArgsConstructor
public class LayoutButton extends Button {
    private final Kit kit;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.kit.getIcon())
                .name("&6&l" + this.kit.getDisplayName())
                .durability(this.kit.getDurability())
                .lore(
                        CC.MENU_BAR,
                        "&7Shift-Click: &c(Not implemented)",
                        " &7" + Symbol.SINGULAR_ARROW_R_2 + " More Layouts",
                        "",
                        "&aClick to edit.",
                        CC.MENU_BAR
                )
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Profile profile = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());

        if (clickType == ClickType.LEFT) {
            if (this.kit.isSettingEnabled(KitSettingRaiding.class)) {
                new LayoutSelectRoleKitMenu(this.kit).openMenu(player);
                return;
            }

            LayoutData layout = profile.getProfileData().getLayoutData().getLayouts().get(this.kit.getName()).get(0);
            new LayoutEditorMenu(this.kit, layout).openMenu(player);
        } else if (clickType == ClickType.SHIFT_LEFT) {
            player.sendMessage(MessageConstant.IN_DEVELOPMENT);
        }
    }
}