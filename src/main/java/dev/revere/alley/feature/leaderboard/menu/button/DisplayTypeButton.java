package dev.revere.alley.feature.leaderboard.menu.button;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.feature.leaderboard.enums.LeaderboardType;
import dev.revere.alley.feature.leaderboard.menu.LeaderboardMenu;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class DisplayTypeButton extends Button {
    protected final Alley plugin = Alley.getInstance();

    /**
     * Gets the item to display in the menu.
     *
     * @param player the player viewing the menu
     * @return the item to display
     */
    @Override
    public ItemStack getButtonItem(Player player) {
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        LeaderboardType currentType = profile.getLeaderboardType();

        List<String> lore = new ArrayList<>();
        for (LeaderboardType type : LeaderboardType.values()) {
            lore.add((currentType == type ? "&f● &6" : "&f● &7") + type.getName());
        }
        lore.add("");
        lore.add("&aClick to change the display type.");

        return new ItemBuilder(Material.EYE_OF_ENDER)
                .name("&6&lDisplay Type")
                .lore(lore)
                .build();
    }

    /**
     * Handles the click event for the button.
     *
     * @param player    the player who clicked the button
     * @param clickType the type of click
     */
    @Override
    public void clicked(Player player, ClickType clickType) {
        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        LeaderboardType currentType = profile.getLeaderboardType();
        LeaderboardType[] types = LeaderboardType.values();
        int currentIndex = currentType.ordinal();

        switch (clickType) {
            case LEFT:
                currentIndex = (currentIndex + 1) % types.length;
                break;
            case RIGHT:
                currentIndex = (currentIndex - 1 + types.length) % types.length;
                break;
        }

        LeaderboardType newType = types[currentIndex];
        profile.setLeaderboardType(newType);
        new LeaderboardMenu().openMenu(player);
        this.playNeutral(player);
    }
}