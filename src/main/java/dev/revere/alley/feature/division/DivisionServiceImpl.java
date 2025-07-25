package dev.revere.alley.feature.division;

import dev.revere.alley.config.ConfigService;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.feature.division.tier.DivisionTier;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

/**
 * @author Emmy
 * @project Alley
 * @since 25/01/2025
 */
@Getter
@Service(provides = DivisionService.class, priority = 150)
public class DivisionServiceImpl implements DivisionService {
    private final ConfigService configService;
    private final List<Division> divisions = new ArrayList<>();

    /**
     * Constructor for DI.
     */
    public DivisionServiceImpl(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.loadDivisions();
    }

    @Override
    public void shutdown(AlleyContext context) {
        this.saveDivisions();
        Logger.info("Saved all divisions.");
    }

    @Override
    public List<Division> getDivisions() {
        return Collections.unmodifiableList(this.divisions);
    }

    /**
     * Method to load all divisions from the divisions.yml file.
     */
    private void loadDivisions() {
        FileConfiguration config = this.configService.getDivisionsConfig();
        ConfigurationSection divisionsSection = config.getConfigurationSection("divisions");

        if (divisionsSection == null) {
            Logger.error("No divisions found in the configuration file.");
            return;
        }

        List<Division> loadedDivisions = new ArrayList<>();
        for (String key : divisionsSection.getKeys(false)) {
            if (key == null || key.trim().isEmpty()) {
                Logger.error("Encountered a null or empty division key. Skipping.");
                continue;
            }

            String path = "divisions." + key;

            String displayName = config.getString(path + ".display-name", key);
            String description = config.getString(path + ".description", "");
            int durability = config.getInt(path + ".durability", 0);
            String iconName = config.getString(path + ".icon");
            Material icon = Material.getMaterial(iconName.toUpperCase());

            List<DivisionTier> tiers = new ArrayList<>();
            ConfigurationSection tierLevels = config.getConfigurationSection(path + ".tier-levels");
            if (tierLevels != null) {
                for (String tierKey : tierLevels.getKeys(false)) {
                    int requiredWins = tierLevels.getInt(tierKey + ".required-wins");
                    tiers.add(new DivisionTier(tierKey, requiredWins));
                }
            }

            Division division = new Division(
                    key,
                    displayName,
                    description,
                    durability,
                    icon,
                    tiers
            );

            loadedDivisions.add(division);
        }

        loadedDivisions.sort(Comparator.comparingInt(d -> d.getTiers().stream().mapToInt(DivisionTier::getRequiredWins).min().orElse(0)));
        this.divisions.addAll(loadedDivisions);
    }

    @Override
    public void saveDivision(Division division) {
        FileConfiguration config = this.configService.getDivisionsConfig();
        String path = "divisions." + division.getName();

        config.set(path + ".display-name", division.getDisplayName());
        config.set(path + ".description", division.getDescription());
        config.set(path + ".durability", division.getDurability());
        config.set(path + ".icon", division.getIcon().name());

        for (DivisionTier tier : division.getTiers()) {
            String tierPath = path + ".tier-levels." + tier.getName();
            config.set(tierPath + ".required-wins", tier.getRequiredWins());
        }

        this.configService.saveConfig(this.configService.getConfigFile("storage/divisions.yml"), config);
    }

    @Override
    public Division getDivision(String name) {
        return this.divisions.stream()
                .filter(division -> division.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void createDivision(String name, int requiredWins) {
        Division division = new Division(name, "&6&l" + name, "The " + name + " Division", 0, Material.DIRT, Arrays.asList(
                new DivisionTier("I", requiredWins),
                new DivisionTier("II", (int) (requiredWins * 1.25)),
                new DivisionTier("III", (int) (requiredWins * 1.5)),
                new DivisionTier("IV", (int) (requiredWins * 1.75)),
                new DivisionTier("V", (requiredWins * 2))
        ));

        this.divisions.add(division);
        this.saveDivision(division);
    }

    @Override
    public void deleteDivision(String name) {
        FileConfiguration config = this.configService.getDivisionsConfig();
        Division division = this.getDivision(name);
        if (division == null) return;

        this.divisions.remove(division);
        config.set("divisions." + name, null);

        this.configService.saveConfig(this.configService.getConfigFile("storage/divisions.yml"), config);
    }

    @Override
    public Division getHighestDivision() {
        return this.divisions.stream()
                .max(Comparator.comparingInt(d -> d.getTiers().stream().mapToInt(DivisionTier::getRequiredWins).max().orElse(0)))
                .orElse(null);
    }

    public void saveDivisions() {
        this.divisions.forEach(this::saveDivision);
    }
}