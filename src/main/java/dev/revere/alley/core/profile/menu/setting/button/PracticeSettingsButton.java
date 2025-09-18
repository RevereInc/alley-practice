package dev.revere.alley.core.profile.menu.setting.button;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.item.ItemBuilder;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.locale.LocaleService;
import dev.revere.alley.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.enums.WorldTime;
import dev.revere.alley.core.profile.menu.music.MusicDiscSelectorMenu;
import dev.revere.alley.core.profile.menu.setting.enums.PracticeSettingType;
import dev.revere.alley.feature.cosmetic.menu.CosmeticsMenu;
import dev.revere.alley.library.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 02/03/2025
 */
@AllArgsConstructor
public class PracticeSettingsButton extends Button {
    private final PracticeSettingType settingType;
    private String displayName;
    private Material material;
    private int durability;
    private List<String> lore;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.material)
                .name(this.displayName)
                .durability(this.durability)
                .lore(this.lore)
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        Profile profile = AlleyPlugin.getInstance().getService(ProfileService.class).getProfile(player.getUniqueId());

        if (this.settingType == PracticeSettingType.WORLD_TIME) {
            handleWorldTimeClick(player, profile, clickType);
            this.playNeutral(player);
            return;
        }

        if (clickType != ClickType.LEFT) {
            return;
        }
        switch (this.settingType) {
            case PARTY_MESSAGES:
                player.performCommand("togglepartymessages");
                break;
            case PARTY_INVITES:
                player.performCommand("togglepartyinvites");
                break;
            case SIDEBAR_VISIBILITY:
                player.performCommand("togglescoreboard");
                break;
            case SCOREBOARD_LINES:
                player.performCommand("togglescoreboardlines");
                break;
            case TAB_VISIBILITY:
                player.performCommand("toggletablist");
                break;
            case PROFANITY_FILTER:
                player.performCommand("toggleprofanityfilter");
                break;
            case DUEL_REQUESTS:
                player.performCommand("toggleduelrequests");
                break;
            case SERVER_TITLES:
                player.performCommand("toggleservertitles");
                break;
            case MATCH_SETTINGS:
                player.closeInventory();
                player.sendMessage(CC.translate("&cThis feature is currently in development."));
                break;
            case COSMETICS:
                new CosmeticsMenu().openMenu(player);
                break;
            case LOBBY_MUSIC:
                new MusicDiscSelectorMenu().openMenu(player);
                break;
        }

        this.playNeutral(player);
    }

    /**
     * Handles world time clicking with cycling logic.
     *
     * @param player    the player who clicked
     * @param profile   the player's profile
     * @param clickType the type of click
     */
    private void handleWorldTimeClick(Player player, Profile profile, ClickType clickType) {
        WorldTime newTime = getNextWorldTime(clickType, profile);
        profile.getProfileData().getSettingData().setTime(newTime.getName());
        LocaleService localeService = AlleyPlugin.getInstance().getService(LocaleService.class);

        switch (newTime) {
            case DEFAULT:
                profile.getProfileData().getSettingData().setTimeDefault(player);
                player.sendMessage(localeService.getMessage(GlobalMessagesLocaleImpl.PROFILE_WORLD_TIME_SET)
                        .replace("{time}", profile.getProfileData().getSettingData().getTime().toLowerCase())
                );
                break;
            case DAY:
                profile.getProfileData().getSettingData().setTimeDay(player);
                player.sendMessage(localeService.getMessage(GlobalMessagesLocaleImpl.PROFILE_WORLD_TIME_SET)
                        .replace("{time}", profile.getProfileData().getSettingData().getTime().toLowerCase())
                );
                break;
            case SUNSET:
                profile.getProfileData().getSettingData().setTimeSunset(player);
                player.sendMessage(localeService.getMessage(GlobalMessagesLocaleImpl.PROFILE_WORLD_TIME_SET)
                        .replace("{time}", profile.getProfileData().getSettingData().getTime().toLowerCase())
                );
                break;
            case NIGHT:
                profile.getProfileData().getSettingData().setTimeNight(player);
                player.sendMessage(localeService.getMessage(GlobalMessagesLocaleImpl.PROFILE_WORLD_TIME_SET)
                        .replace("{time}", profile.getProfileData().getSettingData().getTime().toLowerCase())
                );
                break;
        }
    }

    /**
     * Gets the next world time based on the click type.
     *
     * @param clickType the type of click
     * @param profile   the player's profile
     * @return the next world time
     */
    private WorldTime getNextWorldTime(ClickType clickType, Profile profile) {
        WorldTime[] timeStates = WorldTime.values();
        int currentIndex = profile.getProfileData().getSettingData().getWorldTime().ordinal();

        if (clickType == ClickType.LEFT) {
            currentIndex = (currentIndex + 1) % timeStates.length;
        } else if (clickType == ClickType.RIGHT) {
            currentIndex = (currentIndex - 1 + timeStates.length) % timeStates.length;
        }

        return timeStates[currentIndex];
    }
}