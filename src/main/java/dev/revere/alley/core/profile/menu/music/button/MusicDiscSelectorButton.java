package dev.revere.alley.core.profile.menu.music.button;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.common.text.LoreHelper;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
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

        ProfileMusicData musicData = this.profile.getProfileData().getMusicData();
        LocaleService localeService = AlleyPlugin.getInstance().getService(LocaleService.class);

        if (musicData.getSelectedDiscs().isEmpty()) {
            player.sendMessage(localeService.getMessage(GlobalMessagesLocaleImpl.ERROR_MUST_SELECT_MUSIC));
            return;
        }

        if (musicData.isDiscSelected(this.disc.name())) {
            musicData.removeDisc(this.disc.name());
            player.sendMessage(localeService.getMessage(GlobalMessagesLocaleImpl.MUSIC_DISC_DESELECTED).replace("{disc}", this.disc.getTitle()));
        } else {
            musicData.addDisc(this.disc.name());
           player.sendMessage(localeService.getMessage(GlobalMessagesLocaleImpl.MUSIC_DISC_SELECTED).replace("{disc}", this.disc.getTitle()));
        }

        this.playNeutral(player);
    }
}