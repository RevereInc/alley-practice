package dev.revere.alley.profile.menu.match;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.impl.BackButton;
import dev.revere.alley.api.menu.pagination.PaginatedMenu;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.game.match.data.MatchData;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.menu.match.button.MatchHistoryViewButton;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class MatchHistoryViewMenu extends PaginatedMenu {
    protected final Kit kit;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&6&l" + this.kit.getName() + " Match History";
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        this.addGlassHeader(buttons, 15);
        buttons.put(4, new BackButton(new MatchHistorySelectKitMenu()));

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        Profile profile = Alley.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());

        List<MatchData> matchDataList = profile.getProfileData().getPreviousMatches().stream()
                .sorted((m1, m2) -> Long.compare(m2.getCreationTime(), m1.getCreationTime()))
                .collect(Collectors.toList());

        List<MatchData> filteredMatches = matchDataList.stream()
                .filter(matchData -> matchData.getKit().equals(this.kit.getName()))
                .collect(Collectors.toList());

        filteredMatches.forEach(
                matchData -> buttons.put(buttons.size(), new MatchHistoryViewButton(matchData))
        );

        return buttons;
    }
}