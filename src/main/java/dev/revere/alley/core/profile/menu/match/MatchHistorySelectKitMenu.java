package dev.revere.alley.core.profile.menu.match;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.menu.match.button.MatchHistorySelectKitButton;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.feature.match.data.MatchData;
import dev.revere.alley.library.menu.Button;
import dev.revere.alley.library.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Alley
 * @since 04/06/2025
 */
public class MatchHistorySelectKitMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&6&lMatch History";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        Profile profile = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());

        List<MatchData> matchDataList = profile.getProfileData().getPreviousMatches();

        List<Kit> kits = this.plugin.getService(KitService.class).getKits();
        List<Kit> matchedKitsWithData = kits.stream()
                .filter(kit -> matchDataList.stream().anyMatch(matchData -> matchData.getKit().equals(kit.getName())))
                .collect(Collectors.toList());

        matchedKitsWithData.forEach(kit -> buttons.put(buttons.size(), new MatchHistorySelectKitButton(kit)));

        return buttons;
    }
}
