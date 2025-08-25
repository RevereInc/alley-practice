package dev.revere.alley.core.profile.menu.setting;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.data.types.ProfileSettingData;
import dev.revere.alley.core.profile.menu.setting.button.PracticeSettingsButton;
import dev.revere.alley.core.profile.menu.setting.enums.PracticeSettingType;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.library.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @since 21/04/2025
 */
public class PracticeSettingsMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&6&lPractice Settings";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        Profile profile = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());
        ProfileSettingData settings = profile.getProfileData().getSettingData();

        for (PracticeSettingType type : PracticeSettingType.values()) {
            buttons.put(type.slot, new PracticeSettingsButton(
                    type,
                    type.displayName,
                    type.material,
                    type.durability,
                    type.loreProvider.apply(settings)
            ));
        }

        this.addBorder(buttons, 15, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}