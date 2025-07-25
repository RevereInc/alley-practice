package dev.revere.alley.feature.level;

import dev.revere.alley.config.ConfigService;
import dev.revere.alley.feature.level.data.LevelData;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.util.visual.ProgressBarUtil;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 22/04/2025
 */
@Getter
@Service(provides = LevelService.class, priority = 160)
public class LevelServiceImpl implements LevelService {
    private final ConfigService configService;
    private final List<LevelData> levels = new ArrayList<>();

    /**
     * Constructor for DI.
     */
    public LevelServiceImpl(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.loadLevels();
    }

    @Override
    public List<LevelData> getLevels() {
        return Collections.unmodifiableList(this.levels);
    }

    private void loadLevels() {
        this.levels.clear();
        FileConfiguration config = this.configService.getLevelsConfig();
        ConfigurationSection levelsSection = config.getConfigurationSection("levels");
        if (levelsSection == null) {
            return;
        }

        for (String key : levelsSection.getKeys(false)) {
            String path = "levels." + key;
            String displayName = config.getString(path + ".display-name");

            Material material = Material.matchMaterial(config.getString(path + ".material"));
            int durability = config.getInt(path + ".durability");
            int minElo = config.getInt(path + ".min-elo");
            int maxElo = config.getInt(path + ".max-elo");

            LevelData level = new LevelData(key, displayName, material, durability, minElo, maxElo);
            this.levels.add(level);
        }

        this.levels.sort(Comparator.comparingInt(LevelData::getMinElo));
    }

    @Override
    public void createLevel(String name, int minElo, int maxElo) {
        LevelData level = new LevelData(name, "&6" + name, Material.DIAMOND_SWORD, 0, minElo, maxElo);
        this.levels.add(level);
        this.saveLevel(level);
    }

    @Override
    public void deleteLevel(LevelData level) {
        if (!this.levels.remove(level)) return;

        FileConfiguration config = this.configService.getLevelsConfig();
        File file = this.configService.getConfigFile("storage/levels.yml");

        config.set("levels." + level.getName(), null);
        this.configService.saveConfig(file, config);
    }

    @Override
    public void saveLevel(LevelData level) {
        FileConfiguration config = this.configService.getLevelsConfig();
        File file = this.configService.getConfigFile("storage/levels.yml");
        String path = "levels." + level.getName();

        config.set(path + ".display-name", level.getDisplayName());
        config.set(path + ".material", level.getMaterial().name());
        config.set(path + ".durability", level.getDurability());
        config.set(path + ".min-elo", level.getMinElo());
        config.set(path + ".max-elo", level.getMaxElo());

        this.configService.saveConfig(file, config);
    }

    @Override
    public LevelData getLevel(int elo) {
        if (this.levels.isEmpty()) {
            return null;
        }

        for (LevelData level : this.levels) {
            if (elo >= level.getMinElo() && elo <= level.getMaxElo()) {
                return level;
            }
        }

        LevelData lowestLevel = this.levels.get(0);
        if (elo < lowestLevel.getMinElo()) {
            return lowestLevel;
        }

        LevelData highestLevel = this.levels.get(this.levels.size() - 1);
        if (elo > highestLevel.getMaxElo()) {
            return highestLevel;
        }

        return null;
    }

    @Override
    public LevelData getLevel(String name) {
        return this.levels.stream()
                .filter(tier -> tier.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Helper method to determine the total ELO span for a given level's progression.
     * This is the ELO needed to get from the start of the current level to the start of the next one.
     */
    private int getEloRequiredForLevel(LevelData level) {
        int currentLevelIndex = this.levels.indexOf(level);
        int minElo = level.getMinElo();
        int maxEloForSpan;

        if (currentLevelIndex != -1 && currentLevelIndex < this.levels.size() - 1) {
            LevelData nextLevel = this.levels.get(currentLevelIndex + 1);
            maxEloForSpan = nextLevel.getMinElo();
        } else {
            maxEloForSpan = level.getMaxElo();
        }
        return maxEloForSpan - minElo;
    }

    @Override
    public String getProgressBar(int elo) {
        LevelData level = this.getLevel(elo);
        if (level == null) {
            return "";
        }

        int eloForNextLevel = getEloRequiredForLevel(level);
        int currentProgress = elo - level.getMinElo();

        if (currentProgress < 0) {
            currentProgress = 0;
        }

        if (eloForNextLevel <= 0 || currentProgress >= eloForNextLevel) {
            return ProgressBarUtil.generate(11, 12, 8, "■");
        }

        return ProgressBarUtil.generate(currentProgress, eloForNextLevel, 8, "■");
    }


    @Override
    public String getProgressDetails(int elo) {
        LevelData level = this.getLevel(elo);
        if (level == null) {
            return "0%";
        }

        int eloSpanForLevel = getEloRequiredForLevel(level);

        if (eloSpanForLevel <= 0) {
            return "99%";
        }

        int currentProgress = elo - level.getMinElo();

        if (currentProgress < 0) {
            currentProgress = 0;
        }

        double percentage = ((double) currentProgress / eloSpanForLevel) * 100;

        int finalPercentage = (int) Math.floor(percentage);

        if (finalPercentage >= 100) {
            return "99%";
        }

        return finalPercentage + "%";
    }
}