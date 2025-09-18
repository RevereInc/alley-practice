package dev.revere.alley.core.locale.internal.impl.message;

import dev.revere.alley.common.text.Symbol;
import dev.revere.alley.core.locale.LocaleEntry;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 09/09/2025
 */
@Getter
public enum GlobalMessagesLocaleImpl implements LocaleEntry {
    ARENA_NOT_FOUND("messages/global-messages.yml", "arena.error.not-found", "&cAn arena named &6{arena-name} &cdoes not exist!"),
    ARENA_ALREADY_EXISTS("messages/global-messages.yml", "arena.error.already-exists", "&cAn arena with that name already exists!"),
    ARENA_NO_SELECTION("messages/global-messages.yml", "arena.error.no-selection", "&cYou don't have the bounds selected for the arena! Use &b/arena wand&c!"),
    ARENA_KIT_ALREADY_ADDED("messages/global-messages.yml", "arena.error.kit-already-added-to-arena", "&cThe kit &6{kit-name} &cis already added to the arena &6{arena-name}&c!"),
    ARENA_ARENA_DOES_NOT_HAVE_KIT("messages/global-messages.yml", "arena.error.arena-does-not-have-kit", "&cThe arena &6{arena-name} &cdoes not have the kit &6{kit-name}&c!"),
    ARENA_CAN_NOT_SET_CUBOID_FFA("messages/global-messages.yml", "arena.error.cannot-set-cuboid-ffa", "&cYou cannot set cuboids for Free-For-All arenas! You must use: &4/arena setsafezone pos1/pos2&c."),
    ARENA_MUST_BE_STANDALONE("messages/global-messages.yml", "arena.error.must-be-standalone", "&cThe arena &6{arena-name} &cmust be standalone to do this!"),
    ARENA_INVALID_PORTAL("messages/global-messages.yml", "arena.error.invalid-portal", "&cInvalid portal. Please use 'red' or 'blue'."),
    ARENA_HEIGHT_LIMIT_OUT_OF_BOUNDS("messages/global-messages.yml", "arena.error.height-limit-out-of-bounds", "&cHeight limit must be between 0 and 256!"),
    ARENA_VOID_LEVEL_OUT_OF_BOUNDS("messages/global-messages.yml", "arena.error.void-level-out-of-bounds", "&cVoid level must be between 0 and 256!"),
    ARENA_FFA_ARENAS_NO_SPAWNS("messages/global-messages.yml", "arena.error.ffa-arenas-no-spawns", "&cFFA Arenas do not need spawn positions. Use &4/ffa setspawn&c."),
    ARENA_CANNOT_TOGGLE_FFA("messages/global-messages.yml", "arena.error.cannot-toggle-ffa", "&cYou cannot toggle Free-For-All arenas!"),
    ARENA_CANNOT_ADD_KITS_TO_FFA("messages/global-messages.yml", "arena.error.cannot-add-kits-to-ffa", "&cYou cannot add kits to Free-For-All arenas!"),

    ARENA_SELECTION_TOOL_ADDED("messages/global-messages.yml", "arena.command.selection-tool.added", "&aSuccessfully added the selection tool to your inventory!"),
    ARENA_SELECTION_TOOL_REMOVED("messages/global-messages.yml", "arena.command.selection-tool.removed", "&cSuccessfully removed the selection tool from your inventory!"),
    ARENA_SELECTED_BOUNDARY("messages/global-messages.yml", "arena.command.selected-boundary", "&aSet &6{boundary-type} &aboundary to &6{x}, &6{y}, &6{z}&a!"),
    ARENA_DISPLAY_NAME_SET("messages/global-messages.yml", "arena.command.displayname-set", "&aSuccessfully set the display name of the arena &6{arena-name} &ato &r{display-name}&a!"),
    ARENA_CENTER_SET("messages/global-messages.yml", "arena.command.center-set", "&aSuccessfully set the center of the arena &6{arena-name}&a!"),
    ARENA_CUBOID_SET("messages/global-messages.yml", "arena.command.cuboid-set", "&aSuccessfully set the geom of the arena &6{arena-name}&a!"),
    ARENA_HEIGHT_LIMIT_SET("messages/global-messages.yml", "arena.command.height-limit-set", "&aSuccessfully set the height limit of the arena &6{arena-name}&a to &6{height-limit}&a!"),
    ARENA_PORTAL_SET("messages/global-messages.yml", "arena.command.portal-set", "&aSuccessfully set portal &b{portal} &aof arena &6{arena-name}&a!"),
    ARENA_VOID_LEVEL_SET("messages/global-messages.yml", "arena.command.void-level-set", "&aSuccessfully set the void level of arena &6{arena-name}&a to &6{void-level}&a!"),
    ARENA_TOGGLED("messages/global-messages.yml", "arena.command.toggled", "&aSuccessfully toggled the arena &6{arena-name}&a to &6{status}&a!"),
    ARENA_SPAWN_SET("messages/global-messages.yml", "arena.command.spawn-set", "&aSuccessfully set the &6{position} &fspawn of arena &6{arena-name}&a!"),
    ARENA_FFA_SPAWN_SET("messages/global-messages.yml", "arena.command.ffa-spawn-set", "&aSuccessfully set the FFA spawn of arena &6{arena-name}&a!"),
    ARENA_KIT_ADDED("messages/global-messages.yml", "arena.command.kit-added", "&aSuccessfully added the kit &6{kit-name}&a to arena &6{arena-name}&a!"),
    ARENA_KIT_REMOVED("messages/global-messages.yml", "arena.command.kit-removed", "&cSuccessfully removed the kit &6{kit-name}&c from arena &6{arena-name}&c!"),
    ARENA_CREATED("messages/global-messages.yml", "arena.command.created", "&aSuccessfully created a new arena named &6{arena-name}&a with type &6{arena-type}&a!"),
    ARENA_DELETED("messages/global-messages.yml", "arena.command.deleted", "&cSuccessfully deleted the arena named &6{arena-name}&c!"),
    ARENA_SAVED("messages/global-messages.yml", "arena.command.saved", "&aSuccessfully saved the arena &6{arena-name}&a!"),
    ARENA_SAVED_ALL("messages/global-messages.yml", "arena.command.saved-all", "&aSuccessfully saved all arenas!"),

    COOLDOWN_NOT_FOUND("messages/global-messages.yml", "cooldown.error.not-found", "&cNo cooldown found for &6{player-name} &cof type &6{cooldown-type}&c."),
    COOLDOWN_RESET("messages/global-messages.yml", "cooldown.command.reset", "&aCooldown for &6{player-name} &aof type &6{cooldown-type} &ahas been reset."),

    COSMETIC_NOT_OWNED("messages/global-messages.yml", "cosmetics.not-owned", "&cYou do not own the &6{cosmetic-name}&c cosmetic!"),
    COSMETIC_SELECTED("messages/global-messages.yml", "cosmetics.selected", "&aSuccessfully selected the &6{cosmetic-name} &acosmetic!"),
    COSMETIC_ALREADY_SELECTED("messages/global-messages.yml", "cosmetics.already-selected", "&cYou already selected the &6{cosmetic-name}&c cosmetic!"),

    COSMETIC_SET_FOR_PLAYER("messages/global-messages.yml", "cosmetics.command.set", "&aSuccessfully set &6{cosmetic} &a{type} as the active cosmetic for &6{player}&a!"),
    COSMETICS_NONE_REGISTERED("messages/global-messages.yml", "cosmetics.error.none-registered", "&cThere are no cosmetics registered on the server!"),
    COSMETIC_TYPE_NOT_SUPPORTED("messages/global-messages.yml", "cosmetics.error.type-not-supported", "&cThe cosmetic type &6{type} &cis not supported!"),
    COSMETIC_NOT_FOUND("messages/global-messages.yml", "cosmetics.error.not-found", "&cA cosmetic named &6{input} &cdoes not exist!"),

    CRAFTING_TOGGLED("messages/global-messages.yml", "crafting-operations.command.toggled", "&aCrafting operations for &6{item} &aare now &6{status}&a."),
    CRAFTING_MUST_HOLD_CRAFTABLE_ITEM("messages/global-messages.yml", "crafting-operations.error.must-hold-craftable-item", "&cYou must be holding a craftable item to manage crafting operations."),

    DIVISION_NOT_FOUND("messages/global-messages.yml", "division.error.not-found", "&cA division named &6{division-name} &cdoes not exist!"),
    DIVISION_TIER_NOT_FOUND("messages/global-messages.yml", "division.error.tier-not-found", "&cA tier named &6{tier-name} &cdoes not exist in the &6{division-name} &cdivision!"),
    DIVISION_ALREADY_EXISTS("messages/global-messages.yml", "division.error.already-exists", "&cThe name &6{division-name} &cis already taken by another division!"),

    DIVISION_DESCRIPTION_SET("messages/global-messages.yml", "division.command.description-set", "&aSuccessfully set the description of the &6{division-name} &adivision to &r{description}&a!"),
    DIVISION_DISPLAY_NAME_SET("messages/global-messages.yml", "division.command.display-name-set", "&aSuccessfully set the display name of the &6{division-name} &adivision to &r{display-name}&a!"),
    DIVISION_ICON_SET("messages/global-messages.yml", "division.command.icon-set", "&aSuccessfully set the icon for the division &6{division-name} &ato &6{item-type}:{item-durability}&a!"),
    DIVISION_WINS_SET("messages/global-messages.yml", "division.command.wins-set", "&aSuccessfully set the required wins for the &6{division-name} &adivision's &6{tier-name} &atier to &6{required-wins}&a!"),
    DIVISION_CREATED("messages/global-messages.yml", "division.command.created", "&aSuccessfully created a new division named &6{division-name} &awith &6{required-wins} &awins!"),
    DIVISION_DELETED("messages/global-messages.yml", "division.command.deleted", "&cSuccessfully deleted the division named &6{division-name}&c!"),

    DUEL_REQUEST_ACCEPTED("messages/global-messages.yml", "duel-requests.accepted", "&aYou have accepted the duel request from &6{color}{player}&a!"),
    DUEL_REQUEST_EXPIRED("messages/global-messages.yml", "duel-requests.error.expired", "&cThat duel request has expired."),
    DUEL_REQUEST_NO_ARENA("messages/global-messages.yml", "duel-requests.error.no-arenas", "&cThere is no available arena for that kit."),
    DUEL_REQUEST_CANT_DUEL_SELF("messages/global-messages.yml", "duel-requests.error.cant-duel-self", "&cYou can't duel yourself!"),
    DUEL_REQUEST_ALREADY_PENDING("messages/global-messages.yml", "duel-requests.error.already-pending", "&cYou already have a pending duel request from that player."),
    DUEL_REQUEST_INVALID_FROM_PLAYER("messages/global-messages.yml", "duel-requests.error.no-pending-request", "&cYou do not have a pending duel request from that player."),
    DUEL_REQUEST_REQUESTS_DISABLED_PLAYER("messages/global-messages.yml", "duel-requests.error.player-requests-disabled", "&c{color}{player} has duel requests disabled."),

    EXPLOSIVE_SETTING_UPDATED("messages/global-messages.yml", "explosive.command.setting-updated", "&aSuccessfully set the explosive {setting-name} value to &6{setting-value}&a."),

    ERROR_MUST_LEAVE_PARTY("messages/global-messages.yml", "error-messages.must-leave-party", "&cYou must leave your party before doing that!"),
    ERROR_MUST_BE_IN_LOBBY("messages/global-messages.yml", "error-messages.must-be-in-lobby", "&cYou must be in the lobby to do that!"),
    ERROR_MUST_HOLD_ITEM("messages/global-messages.yml", "error-messages.must-hold-item", "&cYou need to be holding an item!"),

    ERROR_INVALID_NUMBER("messages/global-messages.yml", "error-messages.invalid.number", "&c'{input}' is not a valid number! Please enter a valid number."),
    ERROR_INVALID_PLAYER("messages/global-messages.yml", "error-messages.invalid.player", "&cThat player could not be found!"),
    ERROR_INVALID_TYPE("messages/global-messages.yml", "error-messages.invalid.type", "&cInvalid {type}. Available types: &6{types}&c."),

    ERROR_PLAYER_IS_BUSY("messages/global-messages.yml", "error-messages.player.is-busy", "&6{color}{player} &cis busy."),

    FFA_ADDED_PLAYER("messages/global-messages.yml", "ffa.added-player", "&a&lADDED! &6{player-color}{player} &7&l» &6FFA {ffa-name}"),
    FFA_KICKED_PLAYER("messages/global-messages.yml", "ffa.kicked-player", "&c&lKICKED! &6{player-color}{player} &7&l» &6FFA {ffa-name}"),
    FFA_SPAWN_ENTERED("messages/global-messages.yml", "ffa.entered-spawn", "&aYou have entered the FFA spawn."),
    FFA_SPAWN_LEFT("messages/global-messages.yml", "ffa.left-spawn", "&aYou have left the FFA spawn."),
    FFA_NOT_IN_A_MATCH("messages/global-messages.yml", "ffa.not-in-match", "&cYou are not in an FFA match!"),
    FFA_ALREADY_SPECTATING("messages/global-messages.yml", "ffa.already-spectating", "&cYou are already spectating FFA!"),

    FFA_ALREADY_EXISTS("messages/global-messages.yml", "ffa.error.already-exists", "&cAn FFA Match named &6{ffa-name} &calready exists!"),
    FFA_NOT_FOUND("messages/global-messages.yml", "ffa.error.not-found", "&cAn FFA Match named &6{ffa-name} &cdoes not exist!"),
    FFA_DISABLED("messages/global-messages.yml", "ffa.error.disabled", "&cFFA mode is disabled for the kit &6{kit-name}&c!"),
    FFA_KIT_NOT_ELIGIBLE("messages/global-messages.yml", "ffa.error.kit-not-eligible", "&cThis kit is not eligible for FFA matches! Please disable the &6BUILD/BOXING SETTINGS &cand try again."),
    FFA_PLAYER_NOT_IN_MATCH("messages/global-messages.yml", "ffa.error.player-not-in-match", "&cThat player is not in an FFA Match!"),
    FFA_CAN_ONLY_SETUP_IN_FFA_ARENA("messages/global-messages.yml", "ffa.error.can-only-setup-in-ffa-arena", "&cYou can only setup FFA matches in FFA arenas!"),
    FFA_FULL("messages/global-messages.yml", "ffa.error.ffa-full", "&cThis FFA Match is full!"),
    PLAYER_NOT_IN_FFA("messages/global-messages.yml", "ffa.error.player-not-in-ffa", "&cThat player is not in an FFA Match!"),
    FFA_INVALID_SPAWN_TYPE("messages/global-messages.yml", "ffa.error.invalid-spawn-type", "&cInvalid spawn type! Valid types: pos1, pos2"),

    FFA_MATCH_CREATED("messages/global-messages.yml", "ffa.command.created", "&aSuccessfully created a new FFA Match for the kit &6{kit-name}&a!"),
    FFA_TOGGLED("messages/global-messages.yml", "ffa.command.toggled", "&aFFA mode has been &6{status} &afor kit &6{kit-name}&a!"),
    FFA_ARENA_SET("messages/global-messages.yml", "ffa.command.arena-set", "&aSuccessfully set the arena of the FFA Match for kit &6{kit-name} &ato &6{arena-name}&a!"),
    FFA_SAFE_ZONE_SET("messages/global-messages.yml", "ffa.command.safe-zone-set", "&aSuccessfully set the &6{pos} &asafezone of the FFA Match for kit &6{kit-name}&a!"),
    FFA_MAX_PLAYERS_SET("messages/global-messages.yml", "ffa.command.max-players-set", "&aSuccessfully set the max players of the FFA Match for kit &6{kit-name} &ato &6{max-players}&a!"),
    FFA_SPAWN_SET("messages/global-messages.yml", "ffa.command.spawn-set", "&aFFA spawn position has been set for &6{arena-name}&a!"),

    FFA_KITS_RELOADED("messages/global-messages.yml", "ffa.command.kits-reloaded", "&aSuccessfully reloaded all FFA kits!"),

    HOTBAR_NOT_FOUND("messages/global-messages.yml", "hotbar.error.not-found", "&cThe hotbar item named &e{hotbar-name} &cdoes not exist."),
    HOTBAR_NO_HOTBAR_ITEMS_CREATED("messages/global-messages.yml", "hotbar.error.no-hotbar-items-created", "&cNo hotbar items have been created yet."),

    HOTBAR_CREATED_ITEM("messages/global-messages.yml", "hotbar.command.created", "&aYou have created a new hotbar item named &e{hotbar-name}&a."),
    HOTBAR_DELETED_ITEM("messages/global-messages.yml", "hotbar.command.deleted", "&aYou have deleted the hotbar item named &e{hotbar-name}&a."),

    ITEM_GIVEN("messages/global-messages.yml", "item.command.given-item", "&aYou have been given &6{amount} &a{item-name}&a."),
    ITEM_NOT_CONFIGURED("messages/global-messages.yml", "item.error.item-not-configured", "&cThe item named &6{item-name} &cis not configured."),

    JOIN_MESSAGE_CHAT_ENABLED("messages/global-messages.yml", "join-message.enabled", true),
    JOIN_MESSAGE_CHAT_MESSAGE_LIST("messages/global-messages.yml", "join-message.message", Arrays.asList(
            "",
            "&6&lAlley Practice Core",
            " &6&l│ &rWebsite: &6revere.dev",
            " &6&l│ &rDiscord: &6discord.gg/p3R5qhfWAx",
            " &6&l│ &rGitHub: &6github.com/RevereInc/alley-practice",
            "",
            "&6&lMade by &f{author} &7(v{version})",
            ""
    )),

    KIT_NOT_FOUND("messages/global-messages.yml", "kit.error.not-found", "&cThere is no kit with that name!"),
    KIT_ALREADY_EXISTS("messages/global-messages.yml", "kit.error.already-exists", "&cA kit with that name already exists!"),
    KIT_SLOT_MUST_BE_NUMBER("messages/global-messages.yml", "kit.error.slot-must-be-number", "&cThe slot must be a number!"),

    KIT_INVENTORY_GIVEN("messages/global-messages.yml", "kit.command.inventory-given", "&aSuccessfully retrieved the inventory of the &6{kit-name} &akit!"),
    KIT_INVENTORY_SET("messages/global-messages.yml", "kit.command.inventory-set", "&aSuccessfully set the inventory of the &6{kit-name} &akit!"),
    KIT_FFA_SLOT_SET("messages/global-messages.yml", "kit.command.ffaslot-set", "&aSuccessfully set the FFA slot of the &6{kit-name} &akit to &6{slot}&a!"),
    KIT_DESCRIPTION_SET("messages/global-messages.yml", "kit.command.description-set", "&aSuccessfully set the description of the &6{kit-name} &akit: &r{description}"),
    KIT_DESCRIPTION_CLEARED("messages/global-messages.yml", "kit.command.description-cleared", "&aSuccessfully cleared the description of the &6{kit-name} &akit!"),
    KIT_DISCLAIMER_SET("messages/global-messages.yml", "kit.command.disclaimer-set", "&aSuccessfully set the disclaimer of the &6{kit-name} &akit: &r{disclaimer}"),
    KIT_DISPLAYNAME_SET("messages/global-messages.yml", "kit.command.displayname-set", "&aSuccessfully set the display name of the &6{kit-name} &akit: &r{display-name}"),
    KIT_ICON_SET("messages/global-messages.yml", "kit.command.icon-set", "&aSuccessfully set the icon of the &6{kit-name} &akit to &6{icon}&a!"),
    KIT_CATEGORY_SET("messages/global-messages.yml", "kit.command.category-set", "&aSuccessfully set the category of the &6{kit-name} &akit to &6{category}&a!"),
    KIT_MENU_TITLE_SET("messages/global-messages.yml", "kit.command.menu-title-set", "&aSuccessfully set the menu title of the &6{kit-name} &akit: &r{title}"),
    KIT_SET_EDITABLE("messages/global-messages.yml", "kit.command.set-editable", "&aSuccessfully set the editable status of the &6{kit-name} &akit to &6{editable}&a!"),

    KIT_POTION_EFFECTS_SET("messages/global-messages.yml", "kit.command.potion-effects-set", "&aSuccessfully set the potion effects of the &6{kit-name} &akit!"),
    KIT_POTION_EFFECTS_CLEARED("messages/global-messages.yml", "kit.command.potion-effects-cleared", "&aSuccessfully cleared the potion effects of the &6{kit-name} &akit!"),

    KIT_CREATED("messages/global-messages.yml", "kit.command.created", "&aSuccessfully created a new kit named &6{kit-name}&a!"),
    KIT_DELETED("messages/global-messages.yml", "kit.command.deleted", "&cSuccessfully deleted the kit named &6{kit-name}&c!"),

    KIT_SETTING_SET("messages/global-messages.yml", "kit.command.setting-set", "&aSuccessfully set the setting &6{setting-name} &ato &6{enabled} &afor the kit &6{kit-name}&a."),

    KIT_SAVED("messages/global-messages.yml", "kit.command.saved", "&aSuccessfully saved the &6{kit-name} &akit!"),
    KIT_SAVED_ALL("messages/global-messages.yml", "kit.command.saved-all", "&aSuccessfully saved all kits!"),

    MATCH_ALREADY_IN("messages/global-messages.yml", "match.already-in", "&cYou are already in a match!"),

    PARTY_CREATED("messages/global-messages.yml", "party.created",
            Arrays.asList(
                    "",
                    "&6&lParty Created &a" + Symbol.TICK,
                    " &7Type /p for help.",
                    ""
            )
    ),
    PARTY_LOOKUP("messages/global-messages.yml", "party.lookup", Arrays.asList(
            "",
            " &6&l{leader}'s Party",
            "  &6&l│ &rLeader: &6{leader}",
            "  &6&l│ &rMembers: &6{members}",
            "  &6&l│ &rStatus: &6{status}",
            "  &6&l│ &rPrivacy: &6{privacy}",
            ""
    )),
    PARTY_INFO_NO_MEMBERS_FORMAT("messages/global-messages.yml", "party.info.no-members-format", "&cNo Members"),
    PARTY_INFO("messages/global-messages.yml", "party.info.format", Arrays.asList(
            "",
            " &6&lParty Info",
            "  &6&l│ &rLeader: &6{leader}",
            "  &6&l│ &rMembers &7({members-amount}): &6{members}",
            "  &6&l│ &rPrivacy: &6{privacy}",
            "  &6&l│ &rSize: &6{size}",
            ""
    )),
    PARTY_DISBANDED("messages/global-messages.yml", "party.disbanded", "&cYour party has been disbanded!"),
    PARTY_NOT_IN("messages/global-messages.yml", "party.not-in-party", "&cYou are not in a party."),
    PARTY_NOT_LEADER("messages/global-messages.yml", "party.not-leader", "&cYou are not the leader of the party."),
    PARTY_ALREADY_IN("messages/global-messages.yml", "party.already-in", "&cYou are already in a party."),
    PARTY_LEFT("messages/global-messages.yml", "party.left", "&cYou've left the party!"),
    PLAYER_DISABLED_PARTY_INVITES("messages/global-messages.yml", "party.target-disabled-invites", "&c{player} has party invites disabled."),
    PARTY_CHAT_DISABLED("messages/global-messages.yml", "party.chat-disabled", "&cYou have party messages disabled. &7(To enable: /togglepartymessages)"),
    PARTY_NO_INVITE("messages/global-messages.yml", "party.no-invite", "&cYou do not have a party invitation from &6{player}&c."),
    PARTY_JOINED("messages/global-messages.yml", "party.joined", "&aYou have joined &6{player}&a's party."),

    PROFILE_TOGGLED_PARTY_INVITES("messages/global-messages.yml", "profile-messages.player-settings.party-invites", "&aYou've {status} &aparty invites."),
    PROFILE_TOGGLED_PARTY_MESSAGES("messages/global-messages.yml", "profile-messages.player-settings.party-messages", "&aYou've {status} &aparty messages."),
    PROFILE_TOGGLED_SCOREBOARD("messages/global-messages.yml", "profile-messages.player-settings.scoreboard", "&aYou've {status} &athe scoreboard."),
    PROFILE_TOGGLED_SCOREBOARD_LINES("messages/global-messages.yml", "profile-messages.player-settings.scoreboard-lines", "&aYou've {status} &athe scoreboard lines."),
    PROFILE_TOGGLED_TAB_LIST("messages/global-messages.yml", "profile-messages.player-settings.tab-list", "&aYou've {status} &athe tablist."),
    PROFILE_TOGGLED_PROFANITY_FILTER("messages/global-messages.yml", "profile-messages.player-settings.profanity-filter", "&aYou've {status} &athe profanity filter."),
    PROFILE_TOGGLED_DUEL_REQUESTS("messages/global-messages.yml", "profile-messages.player-settings.duel-requests", "&aYou've {status} &areceiving duel requests."),
    PROFILE_TOGGLED_LOBBY_MUSIC("messages/global-messages.yml", "profile-messages.player-settings.lobby-music", "&aYou've {status} &alobby music."),
    PROFILE_TOGGLED_SERVER_TITLES("messages/global-messages.yml", "profile-messages.player-settings.server-titles", "&aYou've {status} &aserver titles."),
    PROFILE_WORLD_TIME_SET("messages/global-messages.yml", "profile-messages.world-time-set", "&aSuccessfully set your personal world time to &6{time}&a."),
    PROFILE_WORLD_TIME_RESET("messages/global-messages.yml", "profile-messages.world-time-reset", "&aSuccessfully reset your personal world time to the server's time."),

    QUEUE_TEMPORARILY_DISABLED("messages/global-messages.yml", "queue.error.temporarily-disabled", "&cQueueing is temporarily disabled. Please try again later."),
    QUEUE_TOGGLED("messages/global-messages.yml", "queue.command.toggled", "&aYou've temporarily {status} &aqueueing for all players."),
    QUEUE_RELOADED("messages/global-messages.yml", "queue.command.reloaded", "&aSuccessfully reloaded all queues!"),
    QUEUE_FORCED_PLAYER("messages/global-messages.yml", "queue.command.forced-player", "&aSuccessfully forced &6{player} &ainto the &6{ranked} {kit} &aqueue!"),

    SPAWN_SET("messages/global-messages.yml", "spawn.command.set", "&aSuccessfully set the new spawn location of &6Alley Practice&a! \n &8- &7{world}: {x}, {y}, {z} (Yaw: {yaw}, Pitch: {pitch})"),
    SPAWN_TELEPORTED("messages/global-messages.yml", "spawn.command.teleported", "&6Teleported you to spawn!"),
    SPAWN_ITEMS_GIVEN("messages/global-messages.yml", "spawn.command.items-given", "&aSuccessfully received the spawn items!"),

    TIPS_LIST("messages/global-messages.yml", "tips", Arrays.asList(
            "&6Tip: &fUse F5 to look at your opponent one last time before they end you.",
            "&6Tip: &fW-tap like your life depends on it. It kinda does.",
            "&6Tip: &fKeep your crosshair at head level... unless you like tickling toes.",
            "&6Tip: &fPractice spacing — or just hug them and pray."
    )),

    ;

    private final String configName;
    private final String configPath;
    private final Object defaultValue;

    /**
     * Constructor for the MessagesLocale enum.
     *
     * @param configName   The name of the configuration file.
     * @param configPath   The path to the specific string within the configuration file.
     * @param defaultValue The default value for the locale entry.
     */
    GlobalMessagesLocaleImpl(String configName, String configPath, Object defaultValue) {
        this.configName = configName;
        this.configPath = configPath;
        this.defaultValue = defaultValue;
    }
}