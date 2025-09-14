package dev.revere.alley.core.locale.internal.impl.command;

import dev.revere.alley.core.locale.LocaleEntry;
import lombok.Getter;

/**
 * An enum class that contains all locale entries for arena commands.
 *
 * @author Emmy
 * @project Alley
 * @since 09/09/2025
 */
@Getter
public enum ArenaLocaleImpl implements LocaleEntry {
    NOT_FOUND("messages.yml", "arena.error.not-found", "&cAn arena named &6{arena-name} &cdoes not exist!"),
    ALREADY_EXISTS("messages.yml", "arena.error.already-exists", "&cAn arena with that name already exists!"),
    NO_SELECTION("messages.yml", "arena.error.no-selection", "&cYou don't have the bounds selected for the arena! Use &b/arena wand&c!"),
    KIT_ALREADY_ADDED_TO_ARENA("messages.yml", "arena.error.kit-already-added-to-arena", "&cThe kit &6{kit-name} &cis already added to the arena &6{arena-name}&c!"),
    ARENA_DOES_NOT_HAVE_KIT("messages.yml", "arena.error.arena-does-not-have-kit", "&cThe arena &6{arena-name} &cdoes not have the kit &6{kit-name}&c!"),
    CAN_NOT_SET_CUBOID_FFA("messages.yml", "arena.error.cannot-set-cuboid-ffa", "&cYou cannot set cuboids for Free-For-All arenas! You must use: &4/arena setsafezone pos1/pos2&c."),
    MUST_BE_STANDALONE("messages.yml", "arena.error.must-be-standalone", "&cThe arena &6{arena-name} &cmust be standalone to do this!"),
    INVALID_PORTAL("messages.yml", "arena.error.invalid-portal", "&cInvalid portal. Please use 'red' or 'blue'."),
    HEIGHT_LIMIT_OUT_OF_BOUNDS("messages.yml", "arena.error.height-limit-out-of-bounds", "&cHeight limit must be between 0 and 256!"),
    VOID_LEVEL_OUT_OF_BOUNDS("messages.yml", "arena.error.void-level-out-of-bounds", "&cVoid level must be between 0 and 256!"),
    FFA_ARENAS_NO_SPAWNS("messages.yml", "arena.error.ffa-arenas-no-spawns", "&cFFA Arenas do not need spawn positions. Use &4/ffa setspawn&c."),
    CANNOT_TOGGLE_FFA("messages.yml", "arena.error.cannot-toggle-ffa", "&cYou cannot toggle Free-For-All arenas!"),
    CANNOT_ADD_KITS_TO_FFA("messages.yml", "arena.error.cannot-add-kits-to-ffa", "&cYou cannot add kits to Free-For-All arenas!"),

    SELECTION_TOOL_ADDED("messages.yml", "arena.manage.selection-tool.added", "&aSuccessfully added the selection tool to your inventory!"),
    SELECTION_TOOL_REMOVED("messages.yml", "arena.manage.selection-tool.removed", "&cSuccessfully removed the selection tool from your inventory!"),
    SELECTED_BOUNDARY("messages.yml", "arena.data.selected-boundary", "&aSet &6{boundary-type} &aboundary to &6{x}, &6{y}, &6{z}&a!"),

    DISPLAY_NAME_SET("messages.yml", "arena.data.displayname-set", "&aSuccessfully set the display name of the arena &6{arena-name}&a to &r{display-name}&a!"),
    CENTER_SET("messages.yml", "arena.data.center-set", "&aSuccessfully set the center of the arena &6{arena-name}&a!"),
    CUBOID_SET("messages.yml", "arena.data.cuboid-set", "&aSuccessfully set the geom of the arena &6{arena-name}&a!"),
    HEIGHT_LIMIT_SET("messages.yml", "arena.data.height-limit-set", "&aSuccessfully set the height limit of the arena &6{arena-name}&a to &6{height-limit}&a!"),
    PORTAL_SET("messages.yml", "arena.data.portal-set", "&aSuccessfully set portal &b{portal} &aof arena &6{arena-name}&a!"),
    VOID_LEVEL_SET("messages.yml", "arena.data.void-level-set", "&aSuccessfully set the void level of arena &6{arena-name}&a to &6{void-level}&a!"),
    TOGGLED("messages.yml", "arena.data.toggled", "&aSuccessfully toggled the arena &6{arena-name}&a to &6{status}&a!"),
    SPAWN_SET("messages.yml", "arena.data.spawn-set", "&aSuccessfully set the &6{position} &fspawn of arena &6{arena-name}&a!"),
    FFA_SPAWN_SET("messages.yml", "arena.data.ffa-spawn-set", "&aSuccessfully set the FFA spawn of arena &6{arena-name}&a!"),
    KIT_ADDED("messages.yml", "arena.data.kit-added", "&aSuccessfully added the kit &6{kit-name}&a to arena &6{arena-name}&a!"),
    KIT_REMOVED("messages.yml", "arena.data.kit-removed", "&cSuccessfully removed the kit &6{kit-name}&c from arena &6{arena-name}&c!"),

    CREATED("messages.yml", "arena.manage.created", "&aSuccessfully created a new arena named &6{arena-name}&a with type &6{arena-type}&a!"),
    DELETED("messages.yml", "arena.manage.deleted", "&cSuccessfully deleted the arena named &6{arena-name}&c!"),

    SAVED("messages.yml", "arena.storage.saved", "&aSuccessfully saved the arena &6{arena-name}&a!"),
    SAVED_ALL("messages.yml", "arena.storage.saved-all", "&aSuccessfully saved all arenas!"),

    DEFAULT_DISPLAY_NAME_SHARED("messages.yml", "arena.default-display-name.shared", "&6{arena-name}"),
    DEFAULT_DISPLAY_NAME_STANDALONE("messages.yml", "arena.default-display-name.standalone", "&6{arena-name}"),
    DEFAULT_DISPLAY_NAME_FFA("messages.yml", "arena.default-display-name.ffa", "&6{arena-name}");

    private final String configName;
    private final String configPath;
    private final Object defaultValue;

    /**
     * Constructor for the ArenaLocale enum.
     *
     * @param configName   The name of the configuration file.
     * @param configPath   The path to the specific string within the configuration file.
     * @param defaultValue The default value for the locale entry.
     */
    ArenaLocaleImpl(String configName, String configPath, Object defaultValue) {
        this.configName = configName;
        this.configPath = "command-messages." + configPath;
        this.defaultValue = defaultValue;
    }
}