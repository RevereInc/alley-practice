package dev.revere.alley.core.profile.data.types;

import dev.revere.alley.core.profile.enums.ChatChannel;
import dev.revere.alley.core.profile.enums.WorldTime;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 15:22
 */
@Getter
@Setter
public class ProfileSettingData {

    //TODO: Clean this class up a bit, make generic methods for toggling settings, use an enum map to store settings, etc.

    /**
     * public class ProfileSettingData {
     * private final EnumMap<ProfileSetting, ToggleState> settings = new EnumMap<>(ProfileSetting.class);
     * private ChatChannel chatChannel = ChatChannel.GLOBAL;
     * private WorldTime worldTime = WorldTime.DEFAULT;
     * <p>
     * public ProfileSettingData() {
     * settings.put(ProfileSetting.PARTY_MESSAGES, ToggleState.ENABLED);
     * settings.put(ProfileSetting.PARTY_INVITES, ToggleState.ENABLED);
     * settings.put(ProfileSetting.SCOREBOARD, ToggleState.ENABLED);
     * settings.put(ProfileSetting.TABLIST, ToggleState.ENABLED);
     * settings.put(ProfileSetting.SCOREBOARD_LINES, ToggleState.ENABLED);
     * settings.put(ProfileSetting.PROFANITY_FILTER, ToggleState.DISABLED);
     * settings.put(ProfileSetting.DUEL_REQUESTS, ToggleState.ENABLED);
     * settings.put(ProfileSetting.LOBBY_MUSIC, ToggleState.ENABLED);
     * settings.put(ProfileSetting.SERVER_TITLES, ToggleState.ENABLED);
     * }
     * <p>
     * public boolean isEnabled(ProfileSetting setting) {
     * return settings.get(setting).asBoolean();
     * }
     * <p>
     * public void set(ProfileSetting setting, ToggleState state) {
     * settings.put(setting, state);
     * }
     * <p>
     * public void applyWorldTime(Player player) {
     * worldTime.apply(player);
     * }
     * }
     * <p>
     * public enum WorldTime {
     * DEFAULT("Default", (player) -> player.resetPlayerTime()),
     * DAY("Day", (player) -> player.setPlayerTime(6000L, false)),
     * SUNSET("Sunset", (player) -> player.setPlayerTime(12000L, false)),
     * NIGHT("Night", (player) -> player.setPlayerTime(18000L, false));
     * <p>
     * private final String name;
     * private final Consumer<Player> applier;
     * <p>
     * WorldTime(String name, Consumer<Player> applier) {
     * this.name = name;
     * this.applier = applier;
     * }
     * <p>
     * public String getName() {
     * return name;
     * }
     * <p>
     * public void apply(Player player) {
     * applier.accept(player);
     * }
     * }
     * <p>
     * public enum ToggleState {
     * ENABLED, DISABLED;
     * <p>
     * public boolean asBoolean() {
     * return this == ENABLED;
     * }
     * <p>
     * public static ToggleState fromBoolean(boolean value) {
     * return value ? ENABLED : DISABLED;
     * }
     * }
     */

    private boolean partyMessagesEnabled;
    private boolean partyInvitesEnabled;
    private boolean scoreboardEnabled;
    private boolean tablistEnabled;
    private boolean showScoreboardLines;
    private boolean isProfanityFilterEnabled;
    private boolean receiveDuelRequestsEnabled;
    private boolean lobbyMusicEnabled;
    private boolean serverTitles;
    private String chatChannel;
    private String time;

    /**
     * Constructor for the ProfileSettingData class.
     */
    public ProfileSettingData() {
        this.partyMessagesEnabled = true;
        this.partyInvitesEnabled = true;
        this.scoreboardEnabled = true;
        this.tablistEnabled = true;
        this.showScoreboardLines = true;
        this.isProfanityFilterEnabled = false;
        this.receiveDuelRequestsEnabled = true;
        this.lobbyMusicEnabled = true;
        this.serverTitles = true;
        this.chatChannel = ChatChannel.GLOBAL.toString();
        this.time = WorldTime.DEFAULT.getName();
    }

    /**
     * Set the world time for a player to the default time.
     *
     * @param player The player to set the world time for.
     */
    public void setTimeDefault(Player player) {
        this.time = WorldTime.DEFAULT.getName();
        player.resetPlayerTime();
    }

    /**
     * Set the world time for a player to day.
     *
     * @param player The player to set the world time for.
     */
    public void setTimeDay(Player player) {
        this.time = WorldTime.DAY.getName();
        player.setPlayerTime(6000L, false);
    }

    /**
     * Set the world time for a player to sunset.
     *
     * @param player The player to set the world time for.
     */
    public void setTimeSunset(Player player) {
        this.time = WorldTime.SUNSET.getName();
        player.setPlayerTime(12000, false);
    }

    /**
     * Set the world time for a player to night.
     *
     * @param player The player to set the player time for.
     */
    public void setTimeNight(Player player) {
        this.time = WorldTime.NIGHT.getName();
        player.setPlayerTime(18000L, false);
    }

    /**
     * Get the world time based on the profile setting.
     *
     * @return The world time based on the profile setting.
     */
    public WorldTime getWorldTime() {
        return WorldTime.getByName(this.time);
    }

    /**
     * Set the world time based on the profile setting.
     *
     * @param player The player to set the world time for.
     */
    public void setTimeBasedOnProfileSetting(Player player) {
        switch (this.getWorldTime()) {
            case DEFAULT:
                this.setTimeDefault(player);
                break;
            case DAY:
                this.setTimeDay(player);
                break;
            case SUNSET:
                this.setTimeSunset(player);
                break;
            case NIGHT:
                this.setTimeNight(player);
                break;
        }
    }

    /**
     * Check if the player is in day time.
     *
     * @return True if the player is in day time.
     */
    public boolean isDayTime() {
        return this.time.equals(WorldTime.DAY.getName());
    }

    /**
     * Check if the player is in sunset time.
     *
     * @return True if the player is in sunset time.
     */
    public boolean isSunsetTime() {
        return this.time.equals(WorldTime.SUNSET.getName());
    }

    /**
     * Check if the player is in night time.
     *
     * @return True if the player is in night time.
     */
    public boolean isNightTime() {
        return this.time.equals(WorldTime.NIGHT.getName());
    }

    /**
     * Check if the player is in default time.
     *
     * @return True if the player is in default time.
     */
    public boolean isDefaultTime() {
        return this.time.equals(WorldTime.DEFAULT.getName());
    }
}