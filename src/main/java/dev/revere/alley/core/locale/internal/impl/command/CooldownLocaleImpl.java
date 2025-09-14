package dev.revere.alley.core.locale.internal.impl.command;

import dev.revere.alley.core.locale.LocaleEntry;
import lombok.Getter;

/**
 * An enum class that contains all locale entries for cooldown commands.
 *
 * @author Emmy
 * @project alley-practice
 * @since 10/09/2025
 */
@Getter
public enum CooldownLocaleImpl implements LocaleEntry {
    NO_COOLDOWN_FOUND("messages.yml", "cooldown.error.no-cooldown-found", "&cNo cooldown found for &6{player-name} &cof type &6{cooldown-type}&c."),
    COOLDOWN_RESET("messages.yml", "cooldown.reset", "&aCooldown for &6{player-name} &aof type &6{cooldown-type} &ahas been reset."),

    ;

    private final String configName;
    private final String configPath;
    private final Object defaultValue;

    /**
     * Constructor for the CooldownLocale enum.
     *
     * @param configName   The name of the configuration file.
     * @param configPath   The path to the specific string within the configuration file.
     * @param defaultValue The default value for the locale entry.
     */
    CooldownLocaleImpl(String configName, String configPath, Object defaultValue) {
        this.configName = configName;
        this.configPath = "command-messages." + configPath;
        this.defaultValue = defaultValue;
    }
}
