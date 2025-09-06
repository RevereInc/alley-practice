package dev.revere.alley.core.config.internal.locale.impl;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.text.CC;
import dev.revere.alley.core.config.ConfigService;
import dev.revere.alley.core.config.internal.locale.Locale;

/**
 * @author Emmy
 * @project alley-practice
 * @since 04/09/2025
 */
public enum FFALocale implements Locale {
    ALREADY_EXISTS("messages.yml", "ffa.error.already-exists"),
    NOT_FOUND("messages.yml", "ffa.error.not-found"),
    DISABLED("messages.yml", "ffa.error.disabled"),
    KIT_NOT_ELIGIBLE("messages.yml", "ffa.error.kit-not-eligible"),
    PLAYER_NOT_IN_MATCH("messages.yml", "ffa.error.player-not-in-match"),
    CAN_ONLY_SETUP_IN_FFA_ARENA("messages.yml", "ffa.error.can-only-setup-in-ffa-arena"),
    FFA_FULL("messages.yml", "ffa.error.ffa-full"),
    PLAYER_NOT_IN_FFA("messages.yml", "ffa.error.player-not-in-ffa"),

    MATCH_CREATED("messages.yml", "ffa.data.created"),
    TOGGLED("messages.yml", "ffa.data.toggled"),
    ARENA_SET("messages.yml", "ffa.data.arena-set"),
    SAFEZONE_SET("messages.yml", "ffa.data.safe-zone-set"),
    MAX_PLAYERS_SET("messages.yml", "ffa.data.max-players-set"),
    SPAWN_SET("messages.yml", "ffa.data.spawn-set"),

    ADDED_PLAYER("messages.yml", "ffa.added-player"),
    KICKED_PLAYER("messages.yml", "ffa.kicked-player")

    ;

    private final String configName;
    private final String configString;

    /**
     * Constructor for the FFALocale enum.
     *
     * @param configName   The name of the config.
     * @param configString The string of the config.
     */
    FFALocale(String configName, String configString) {
        this.configName = configName;
        this.configString = configString;
    }

    @Override
    public String getMessage() {
        ConfigService configService = AlleyPlugin.getInstance().getService(ConfigService.class);
        return CC.translate(configService.getConfig(this.configName).getString(this.configString));
    }
}
