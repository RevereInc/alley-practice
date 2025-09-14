package dev.revere.alley.core.profile.menu.music.button;

import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.common.text.LoreHelper;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.data.types.ProfileMusicData;
import dev.revere.alley.feature.music.MusicDisc;
import dev.revere.alley.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project alley-practice
 * @since 19/07/2025
 */
@AllArgsConstructor
public class MusicDiscSelectorButton extends Button {
    private final Profile profile;
    private final MusicDisc disc;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.disc.getMaterial())
                .name("&6&l" + this.disc.getTitle())
                .lore(
                        CC.MENU_BAR,
                        LoreHelper.displayToggled(this.profile.getProfileData().getMusicData().isDiscSelected(this.disc.name())),
                        "",
                        "&aClick to toggle.",
                        CC.MENU_BAR
                )
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        //TODO: Locale

        ProfileMusicData musicData = this.profile.getProfileData().getMusicData();

        if (musicData.getSelectedDiscs().isEmpty()) {
            player.sendMessage(CC.translate("&cYou must at least select one music disc before you can toggle them!"));
            return;
        }

        if (musicData.isDiscSelected(this.disc.name())) {
            musicData.removeDisc(this.disc.name());
            player.sendMessage(CC.translate("&cYou have removed &6" + this.disc.getTitle() + "&c from your music selection."));
        } else {
            musicData.addDisc(this.disc.name());
            player.sendMessage(CC.translate("&aYou have added &6" + this.disc.getTitle() + "&a to your music selection."));
        }

        this.playNeutral(player);
    }
}