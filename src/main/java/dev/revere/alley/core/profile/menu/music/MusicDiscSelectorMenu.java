package dev.revere.alley.core.profile.menu.music;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.menu.music.button.MusicDiscSelectorButton;
import dev.revere.alley.core.profile.menu.music.button.ToggleLobbyMusicButton;
import dev.revere.alley.core.profile.menu.setting.PracticeSettingsMenu;
import dev.revere.alley.feature.music.MusicDisc;
import dev.revere.alley.feature.music.MusicService;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.library.menu.Menu;
import dev.revere.alley.library.menu.impl.BackButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Emmy
 * @project alley-practice
 * @since 19/07/2025
 */
public class MusicDiscSelectorMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "&6&lSongs";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        Profile profile = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());

        MusicService musicService = AlleyPlugin.getInstance().getService(MusicService.class);
        List<MusicDisc> musicDiscs = musicService.getMusicDiscs();

        int slot = 10;
        for (MusicDisc disc : musicDiscs) {
            slot = this.skipIfSlotCrossingBorder(slot);
            buttons.put(slot, new MusicDiscSelectorButton(profile, disc));
            slot++;
        }

        this.addBorder(buttons, (short) 15, 4);

        buttons.put(4, new ToggleLobbyMusicButton());
        buttons.put(0, new BackButton(new PracticeSettingsMenu()));

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 4;
    }
}