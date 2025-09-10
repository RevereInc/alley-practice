package dev.revere.alley.core.locale.internal.types;

import dev.revere.alley.core.locale.LocaleEntry;
import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 09/09/2025
 */
@Getter
public enum FFALocaleImpl implements LocaleEntry {
    ALREADY_EXISTS(
            "messages.yml",
            "ffa.error.already-exists",
            "&cAn FFA Match named &6{ffa-name} &calready exists!"
    ),
    NOT_FOUND(
            "messages.yml",
            "ffa.error.not-found",
            "&cAn FFA Match named &6{ffa-name} &cdoes not exist!"
    ),
    DISABLED(
            "messages.yml",
            "ffa.error.disabled",
            "&cFFA mode is disabled for the kit &6{kit-name}&c!"
    ),
    KIT_NOT_ELIGIBLE(
            "messages.yml",
            "ffa.error.kit-not-eligible",
            "&cThis kit is not eligible for FFA matches! Please disable the &6BUILD/BOXING SETTINGS &cand try again."
    ),
    PLAYER_NOT_IN_MATCH(
            "messages.yml",
            "ffa.error.player-not-in-match",
            "&cThat player is not in an FFA Match!"
    ),
    CAN_ONLY_SETUP_IN_FFA_ARENA(
            "messages.yml",
            "ffa.error.can-only-setup-in-ffa-arena",
            "&cYou can only setup FFA matches in FFA arenas!"
    ),
    FFA_FULL(
            "messages.yml",
            "ffa.error.ffa-full",
            "&cThis FFA Match is full!"
    ),
    PLAYER_NOT_IN_FFA(
            "messages.yml",
            "ffa.error.player-not-in-ffa",
            "&cThat player is not in an FFA Match!"
    ),

    MATCH_CREATED(
            "messages.yml",
            "ffa.data.created",
            "&aSuccessfully created a new FFA Match for the kit &6{kit-name}&a!"
    ),
    TOGGLED(
            "messages.yml",
            "ffa.data.toggled",
            "&aFFA mode has been &6{status} &afor kit &6{kit-name}&a!"
    ),
    ARENA_SET(
            "messages.yml",
            "ffa.data.arena-set",
            "&aSuccessfully set the arena of the FFA Match for kit &6{kit-name} &ato &6{arena-name}&a!"
    ),
    SAFEZONE_SET(
            "messages.yml",
            "ffa.data.safe-zone-set",
            "&aSuccessfully set the &6{pos} &asafezone of the FFA Match for kit &6{kit-name}&a!"
    ),
    MAX_PLAYERS_SET(
            "messages.yml",
            "ffa.data.max-players-set",
            "&aSuccessfully set the max players of the FFA Match for kit &6{kit-name} &ato &6{max-players}&a!"
    ),
    SPAWN_SET(
            "messages.yml",
            "ffa.data.spawn-set",
            "&aFFA spawn position has been set for &6{arena-name}&a!"
    ),

    ADDED_PLAYER(
            "messages.yml",
            "ffa.added-player",
            "&a&lADDED! &6{player-color}{player} &7&l» &6FFA {ffa-name}"
    ),
    KICKED_PLAYER(
            "messages.yml",
            "ffa.kicked-player",
            "&c&lKICKED! &6{player-color}{player} &7&l» &6FFA {ffa-name}"
    ),

    ;

    private final String configName;
    private final String configPath;
    private final Object defaultValue;

    /**
     * Constructor for the FFALocale enum.
     *
     * @param configName   The name of the configuration file.
     * @param configPath   The path to the specific string within the configuration file.
     * @param defaultValue The default value for the locale entry.
     */
    FFALocaleImpl(String configName, String configPath, Object defaultValue) {
        this.configName = configName;
        this.configPath = configPath;
        this.defaultValue = defaultValue;
    }
}