package dev.revere.alley.locale;

import lombok.Getter;
import dev.revere.alley.Alley;
import dev.revere.alley.util.chat.CC;

/**
 * @author Emmy
 * @project Alley
 * @date 19/04/2024 - 17:41
 */
@Getter
public enum Locale {
    NO_PERM("messages.yml", "no-permission"),

    ;

    private final String configName, configString;

    /**
     * Constructor for the Locale class.
     *
     * @param configName   The name of the config.
     * @param configString The string of the config.
     */
    Locale(String configName, String configString) {
        this.configName = configName;
        this.configString = configString;
    }

    public String getMessage() {
        return CC.translate(Alley.getInstance().getConfigService().getConfig(this.configName).getString(this.configString));
    }
}