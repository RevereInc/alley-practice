package dev.revere.alley.profile.menu.match;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.base.kit.KitService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.game.match.data.MatchData;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.menu.match.button.MatchHistorySelectKitButton;
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

        Profile profile = Alley.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());

        List<MatchData> matchDataList = profile.getProfileData().getPreviousMatches();

        List<Kit> kits = this.plugin.getService(KitService.class).getKits();
        List<Kit> matchedKitsWithData = kits.stream()
                .filter(kit -> matchDataList.stream().anyMatch(matchData -> matchData.getKit().equals(kit.getName())))
                .collect(Collectors.toList());

        matchedKitsWithData.forEach(kit -> buttons.put(buttons.size(), new MatchHistorySelectKitButton(kit)));

        return buttons;
    }
}
