package dev.revere.alley.feature.title;

import dev.revere.alley.Alley;
import dev.revere.alley.base.kit.KitService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.feature.division.Division;
import dev.revere.alley.feature.division.DivisionService;
import dev.revere.alley.feature.title.record.TitleRecord;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.tool.visual.TextFormatter;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Emmy
 * @project Alley
 * @since 21/04/2025
 */
@Getter
@Service(provides = TitleService.class, priority = 170)
public class TitleServiceImpl implements TitleService {
    private final ConfigService configService;
    private final KitService kitService;
    private final DivisionService divisionService;

    private final Map<Kit, TitleRecord> titles = new HashMap<>();

    /**
     * Constructor for DI.
     */
    public TitleServiceImpl(ConfigService configService, KitService kitService, DivisionService divisionService) {
        this.configService = configService;
        this.kitService = kitService;
        this.divisionService = divisionService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.loadTitles();
    }

    private void loadTitles() {
        FileConfiguration config = this.configService.getTitlesConfig();
        String path = "titles.";
        AtomicInteger missingKits = new AtomicInteger(0);

        this.kitService.getKits().forEach(kit -> {
            if (!this.isKitPresentInConfig(config, kit)) {
                missingKits.getAndIncrement();
                config.set(path + kit.getName() + ".prefix", this.getPrefixBasedOnHighestDivision(kit));
                config.set(path + kit.getName() + ".required", this.divisionService.getHighestDivision().getName());
            }
        });

        if (missingKits.get() > 0) {
            File titlesFile = this.configService.getConfigFile("storage/titles.yml");
            this.configService.saveConfig(titlesFile, config);

            TextFormatter.centerText(
                    Arrays.asList(
                            "",
                            "&c&lINFO",
                            "&fMissing &c" + missingKits + " &fkits in titles.yml.",
                            "&fDefault values have been applied.",
                            "&fPlease check the &cfile &fand adjust as needed.",
                            ""
                    ),
                    60
            ).forEach(line -> Bukkit.getConsoleSender().sendMessage(CC.translate(line)));
        }

        this.titles.clear();
        this.kitService.getKits().forEach(kit -> {
            String prefix = config.getString(path + kit.getName() + ".prefix");
            String requiredDivisionName = config.getString(path + kit.getName() + ".required");

            if (prefix == null || requiredDivisionName == null) return;

            Division requiredDivision = this.divisionService.getDivision(requiredDivisionName);
            if (requiredDivision == null) {
                Logger.error("Division " + requiredDivisionName + " for kit " + kit.getName() + " does not exist.");
            } else {
                TitleRecord title = new TitleRecord(kit, prefix, requiredDivision);
                this.titles.put(kit, title);
            }
        });
    }

    /**
     * Checks if a kit is present in the configuration.
     *
     * @param config The configuration to check.
     * @param kit    The kit to check for.
     * @return True if the kit is present in the configuration, false otherwise.
     */
    private boolean isKitPresentInConfig(FileConfiguration config, Kit kit) {
        return config.contains("titles." + kit.getName());
    }

    /**
     * Gets the title for a specific kit.
     *
     * @param kit The kit to get the title for.
     * @return The title for the specified kit.
     */
    private String getPrefixBasedOnHighestDivision(Kit kit) {
        Division highestDivision = Alley.getInstance().getService(DivisionService.class).getHighestDivision();

        return "&6&l" + kit.getName().toUpperCase() + " " + highestDivision.getName().toUpperCase();
    }
}