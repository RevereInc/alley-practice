package dev.revere.alley.profile.menu.music.button;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.tool.visual.LoreHelper;
import dev.revere.alley.util.chat.CC;
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
        Profile profile = Alley.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
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
