package dev.revere.alley.core.profile.menu.music.button;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.common.text.LoreHelper;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.library.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project alley-practice
 * @since 20/07/2025
 */
public class ToggleLobbyMusicButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        Profile profile = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        return new ItemBuilder(Material.EMERALD)
                .name("&6&lLobby Music")
                .lore(
                        CC.MENU_BAR,
                        LoreHelper.displayEnabled(profile.getProfileData().getSettingData().isLobbyMusicEnabled()),
                        "",
                        "&aClick to toggle.",
                        CC.MENU_BAR
                )
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        player.performCommand("togglelobbymusic");

        this.playNeutral(player);
    }
}
