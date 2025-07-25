package dev.revere.alley.profile.menu.match.button;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.game.match.data.MatchData;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.menu.match.MatchHistoryViewMenu;
import dev.revere.alley.tool.item.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 04/06/2025
 */
@AllArgsConstructor
public class MatchHistorySelectKitButton extends Button {
    protected final Kit kit;

    @Override
    public ItemStack getButtonItem(Player player) {
        Profile profile = Alley.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());

        List<MatchData> matchDataList = profile.getProfileData().getPreviousMatches();

        int count = (int) matchDataList.stream()
                .filter(matchData -> matchData.getKit().equals(this.kit.getName()))
                .count();

        int unrankedCount = (int) matchDataList.stream()
                .filter(matchData -> matchData.getKit().equals(this.kit.getName()) && !matchData.isRanked())
                .count();

        int rankedCount = count - unrankedCount;

        return new ItemBuilder(this.kit.getIcon())
                .name("&6&l" + this.kit.getDisplayName())
                .lore(
                        " &f● Total: &6" + count,
                        " &f● Ranked: &6" + rankedCount,
                        " &f● Unranked: &6" + unrankedCount,
                        "",
                        "&aClick to view!"
                )
                .durability(this.kit.getDurability())
                .hideMeta()
                .amount(count > 0 ? count : 1)
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        new MatchHistoryViewMenu(this.kit).openMenu(player);
    }
}
