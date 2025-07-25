package dev.revere.alley.profile.data;

import com.google.common.collect.Maps;
import dev.revere.alley.Alley;
import dev.revere.alley.base.kit.KitService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingRanked;
import dev.revere.alley.feature.level.LevelService;
import dev.revere.alley.feature.title.TitleService;
import dev.revere.alley.feature.title.record.TitleRecord;
import dev.revere.alley.game.match.data.MatchData;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.data.impl.*;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Alley
 * @date 21/05/2024 - 22:03
 */
@Getter
@Setter
public class ProfileData {
    private Map<String, ProfileUnrankedKitData> unrankedKitData;
    private Map<String, ProfileRankedKitData> rankedKitData;
    private Map<String, ProfileFFAData> ffaData;

    private ProfileLayoutData layoutData;
    private ProfileSettingData settingData;
    private ProfileCosmeticData cosmeticData;
    private ProfilePlayTimeData playTimeData;
    private ProfileMusicData musicData;

    private List<MatchData> previousMatches;

    private List<String> unlockedTitles;

    private String selectedTitle = "";
    private String globalLevel = "";

    private int elo = 1000;
    private int coins = 100;
    private int unrankedWins = 0;
    private int unrankedLosses = 0;
    private int rankedWins = 0;
    private int rankedLosses = 0;

    private boolean rankedBanned = false;

    public ProfileData() {
        this.initializeMaps();
        this.feedDataClasses();
        this.initializeDataClasses();
        this.previousMatches = new ArrayList<>();
        this.unlockedTitles = new ArrayList<>();
    }

    private void initializeDataClasses() {
        this.settingData = new ProfileSettingData();
        this.cosmeticData = new ProfileCosmeticData();
        this.playTimeData = new ProfilePlayTimeData();
        this.layoutData = new ProfileLayoutData();
        this.musicData = new ProfileMusicData();
    }

    private void feedDataClasses() {
        KitService kitService = Alley.getInstance().getService(KitService.class);
        for (Kit kit : kitService.getKits()) {
            this.rankedKitData.put(kit.getName(), new ProfileRankedKitData());
            this.unrankedKitData.put(kit.getName(), new ProfileUnrankedKitData());
            this.ffaData.put(kit.getName(), new ProfileFFAData());
        }
    }

    private void initializeMaps() {
        this.unrankedKitData = Maps.newHashMap();
        this.rankedKitData = Maps.newHashMap();
        this.ffaData = Maps.newHashMap();
    }

    /**
     * Calculates the global elo of the player
     *
     * @param profile the profile of the player
     * @return the global elo of the player
     */
    private int calculateGlobalElo(Profile profile) {
        KitService kitService = Alley.getInstance().getService(KitService.class);
        List<Kit> rankedKits = kitService.getKits().stream()
                .filter(kit -> kit.isSettingEnabled(KitSettingRanked.class))
                .collect(Collectors.toList());

        if (rankedKits.isEmpty()) {
            return 0;
        }

        int totalElo = rankedKits.stream()
                .mapToInt(kit -> {
                    ProfileRankedKitData kitData = profile.getProfileData().getRankedKitData().get(kit.getName());
                    return kitData != null ? kitData.getElo() : 0;
                })
                .sum();

        return totalElo / rankedKits.size();
    }

    public void determineTitles() {
        TitleService titleService = Alley.getInstance().getService(TitleService.class);

        for (TitleRecord title : titleService.getTitles().values()) {
            if (this.unrankedKitData.get(title.getKit().getName()).getDivision() == title.getRequiredDivision()) {
                if (!this.unlockedTitles.contains(title.getKit().getName())) {
                    this.unlockedTitles.add(title.getKit().getName());
                }
            }
        }
    }

    public void determineLevel() {
        LevelService levelService = Alley.getInstance().getService(LevelService.class);
        this.globalLevel = levelService.getLevel(this.elo).getName();
    }

    /**
     * Updates the elo and division of the player
     *
     * @param profile the profile of the player
     */
    public void updateElo(Profile profile) {
        int previousElo = this.elo;
        LevelService levelService = Alley.getInstance().getService(LevelService.class);
        String previousLevel = levelService.getLevel(previousElo).getName();

        this.elo = this.calculateGlobalElo(profile);
        String newLevel = levelService.getLevel(this.elo).getName();

        if (!newLevel.equals(previousLevel)) {
            this.sendLevelUpMessage(profile, newLevel);
        }
    }

    /**
     * Sends a level up message to the player
     *
     * @param profile  the profile of the player
     * @param newLevel the new level of the player
     */
    private void sendLevelUpMessage(Profile profile, String newLevel) {
        Arrays.asList(
                "",
                "&6&lNEW LEVEL &f| &a&lCONGRATULATIONS!",
                " &fYou have reached &6" + newLevel + " &fin the global ranking system.",
                ""
        ).forEach(line -> Bukkit.getPlayer(profile.getUuid()).sendMessage(CC.translate(line)));
    }

    /**
     * Get the total amount of wins
     *
     * @return The total amount of wins
     */
    public int getTotalWins() {
        return this.rankedWins + this.unrankedWins;
    }

    /**
     * Get the total amount of losses
     *
     * @return The total amount of losses
     */
    public int getTotalLosses() {
        return this.rankedLosses + this.unrankedLosses;
    }

    /**
     * Get the total amount of kills of the player ffa data.
     *
     * @return The total amount of kills
     */
    public int getTotalFFAKills() {
        return this.ffaData.values().stream().mapToInt(ProfileFFAData::getKills).sum();
    }

    /**
     * Get the total amount of deaths of the player ffa data.
     *
     * @return The total amount of deaths
     */
    public int getTotalFFADeaths() {
        return this.ffaData.values().stream().mapToInt(ProfileFFAData::getDeaths).sum();
    }

    public void incrementUnrankedWins() {
        this.unrankedWins++;
    }

    public void incrementUnrankedLosses() {
        this.unrankedLosses++;
    }

    public void incrementRankedWins() {
        this.rankedWins++;
    }

    public void incrementRankedLosses() {
        this.rankedLosses++;
    }

    /**
     * Increments the player's coins by the specified amount.
     *
     * @param amount The amount of coins to add.
     */
    public void incrementCoins(int amount) {
        this.coins += amount;
    }
}