package dev.revere.alley.feature.tip;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.logger.Logger;
import dev.revere.alley.core.config.ConfigService;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Emmy
 * @project Alley
 * @since 25/04/2025
 */
@Getter
public class Tip {
    private final List<String> tips = new ArrayList<>();

    public Tip() {
        FileConfiguration config = AlleyPlugin.getInstance().getService(ConfigService.class).getMessagesConfig();

        if (!config.contains("tips")) {
            Logger.info("Tips section not found in messages config.");
            return;
        }

        if (config.getStringList("tips").isEmpty()) {
            Logger.info("No tips found in messages config.");
            return;
        }

        this.tips.addAll(config.getStringList("tips"));
    }

    /**
     * Returns a random tip from the list of tips.
     *
     * @return A random tip as a String.
     */
    public String getRandomTip() {
        if (this.tips.isEmpty()) {
            return "&cNo tips found in config.";
        }

        return this.tips.get(ThreadLocalRandom.current().nextInt(this.tips.size()));
    }
}