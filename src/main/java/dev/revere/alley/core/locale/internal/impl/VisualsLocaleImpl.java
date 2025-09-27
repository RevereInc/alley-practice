package dev.revere.alley.core.locale.internal.impl;

import dev.revere.alley.common.text.Symbol;
import dev.revere.alley.core.locale.LocaleEntry;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author Emmy
 * @project alley-practice
 * @since 13/09/2025
 */
@Getter
public enum VisualsLocaleImpl implements LocaleEntry {

    //TODO: Add booleans for all titles (option to disable or enable)

    ACTIONBAR_HEALTH_INDICATOR_MESSAGE_FORMAT("providers/visuals.yml", "action-bars.health-indicator.message-format", "{name-color}{target} {health-bar}"),
    ACTIONBAR_HEALTH_INDICATOR_SYMBOL_APPEARANCE("providers/visuals.yml", "action-bars.health-indicator.symbol.appearance", "❤"),
    ACTIONBAR_HEALTH_INDICATOR_SYMBOL_COLOR_FULL("providers/visuals.yml", "action-bars.health-indicator.symbol.color.full", "&4&l"),
    ACTIONBAR_HEALTH_INDICATOR_SYMBOL_COLOR_HALF("providers/visuals.yml", "action-bars.health-indicator.symbol.color.half", "&c&l"),
    ACTIONBAR_HEALTH_INDICATOR_SYMBOL_COLOR_EMPTY("providers/visuals.yml", "action-bars.health-indicator.symbol.color.empty", "&7&l"),

    ACTIONBAR_QUEUE_INDICATOR_ENABLED_BOOLEAN("providers/visuals.yml", "action-bars.queue-indicator.enabled", true),
    ACTIONBAR_QUEUE_INDICATOR_MESSAGE_FORMAT("providers/visuals.yml", "action-bars.queue-indicator.message-format", "&6{queue-type} {kit-name} &7│ &f{elapsed-time}"),

    ACTIONBAR_DEATH_MESSAGE_ENABLED_BOOLEAN("providers/visuals.yml", "action-bars.death-message.enabled", true),
    ACTIONBAR_DEATH_MESSAGE_FORMAT("providers/visuals.yml", "action-bars.death-message.format", "&c&lKILL! &f{victim-name-color}{victim}"),

    TAB_LIST_ENABLED_BOOLEAN("providers/tab-list.yml", "tab-list.enabled", true),
    TAB_LIST_HEADER("providers/tab-list.yml", "tab-list.header", Arrays.asList("", "&6&lAlley Network", "")),
    TAB_LIST_FOOTER("providers/tab-list.yml", "tab-list.footer", Arrays.asList("", "&fPlaying with &6&lAlley &fdeveloped by &6&lEmmy &f& &6&lRemi", "")),

    TITLE_JOIN_MESSAGE_ENABLED("providers/visuals.yml", "titles.join-message.enabled", true),
    TITLE_JOIN_MESSAGE_HEADER("providers/visuals.yml", "titles.join-message.header", "&6&lWelcome to Alley Practice"),
    TITLE_JOIN_MESSAGE_SUBHEADER("providers/visuals.yml", "titles.join-message.subheader", "&fMade by &6Emmy &f& &6Remi"),
    TITLE_JOIN_MESSAGE_FADE_IN("providers/visuals.yml", "titles.join-message.fade-in", 10),
    TITLE_JOIN_MESSAGE_STAY("providers/visuals.yml", "titles.join-message.stay", 40),
    TITLE_JOIN_MESSAGE_FADE_OUT("providers/visuals.yml", "titles.join-message.fade-out", 10),

    TITLE_MATCH_STARTING_ENABLED_BOOLEAN("providers/visuals.yml", "titles.match-starting.enabled", true),
    TITLE_MATCH_STARTING_HEADER("providers/visuals.yml", "titles.match-starting.header", "&a&l{colored-stage-bold}"),
    TITLE_MATCH_STARTING_FOOTER("providers/visuals.yml", "titles.match-starting.footer", ""),
    TITLE_MATCH_STARTING_FADE_IN("providers/visuals.yml", "titles.match-starting.fade-in", 0),
    TITLE_MATCH_STARTING_STAY("providers/visuals.yml", "titles.match-starting.stay", 23),
    TITLE_MATCH_STARTING_FADEOUT("providers/visuals.yml", "titles.match-starting.fade-out", 5),

    TITLE_MATCH_RESTARTING_ENABLED_BOOLEAN("providers/visuals.yml", "titles.match-restarting-round.enabled", true),
    TITLE_MATCH_RESTARTING_ROUND_HEADER("providers/visuals.yml", "titles.match-restarting-round.header", "&a&l{colored-stage-bold}"),
    TITLE_MATCH_RESTARTING_ROUND_FOOTER("providers/visuals.yml", "titles.match-restarting-round.footer", ""),
    TITLE_MATCH_RESTARTING_ROUND_FADE_IN("providers/visuals.yml", "titles.match-restarting-round.fade-in", 0),
    TITLE_MATCH_RESTARTING_ROUND_STAY("providers/visuals.yml", "titles.match-restarting-round.stay", 23),
    TITLE_MATCH_RESTARTING_ROUND_FADEOUT("providers/visuals.yml", "titles.match-restarting-round.fade-out", 20),

    TITLE_MATCH_STARTED_ENABLED_BOOLEAN("providers/visuals.yml", "titles.match-started.enabled", true),
    TITLE_MATCH_STARTED_HEADER("providers/visuals.yml", "titles.match-started.header", "&6&lMatch started"),
    TITLE_MATCH_STARTED_FOOTER("providers/visuals.yml", "titles.match-started.footer", "&fGood Luck!"),
    TITLE_MATCH_STARTED_FADE_IN("providers/visuals.yml", "titles.match-started.fade-in", 0),
    TITLE_MATCH_STARTED_STAY("providers/visuals.yml", "titles.match-started.stay", 25),
    TITLE_MATCH_STARTED_FADEOUT("providers/visuals.yml", "titles.match-started.fade-out", 20),

    TITLE_MATCH_RESPAWNING_ENABLED_BOOLEAN("providers/visuals.yml", "titles.match-respawning.enabled", true),
    TITLE_MATCH_RESPAWNING_HEADER("providers/visuals.yml", "titles.match-respawning.header", "&c&lYOU DIED!"),
    TITLE_MATCH_RESPAWNING_FOOTER("providers/visuals.yml", "titles.match-respawning.footer", "&fRespawning in &6{seconds}&f..."),
    TITLE_MATCH_RESPAWNING_FADE_IN("providers/visuals.yml", "titles.match-respawning.fade-in", 0),
    TITLE_MATCH_RESPAWNING_STAY("providers/visuals.yml", "titles.match-respawning.stay", 23),
    TITLE_MATCH_RESPAWNING_FADEOUT("providers/visuals.yml", "titles.match-respawning.fade-out", 0),

    TITLE_MATCH_RESPAWNED_ENABLED_BOOLEAN("providers/visuals.yml", "titles.match-respawned.enabled", true),
    TITLE_MATCH_VICTORY_HEADER("providers/visuals.yml", "titles.match-victory.header", "&a&lVICTORY!"),
    TITLE_MATCH_VICTORY_FOOTER("providers/visuals.yml", "titles.match-victory.footer", "&a{winner} &fwon the Match!"),
    TITLE_MATCH_VICTORY_FADE_IN("providers/visuals.yml", "titles.match-victory.fade-in", 0),
    TITLE_MATCH_VICTORY_STAY("providers/visuals.yml", "titles.match-victory.stay", 20),
    TITLE_MATCH_VICTORY_FADEOUT("providers/visuals.yml", "titles.match-victory.fade-out", 20),

    TITLE_MATCH_DEFEAT_ENABLED_BOOLEAN("providers/visuals.yml", "titles.match-defeat.enabled", true),
    TITLE_MATCH_DEFEAT_HEADER("providers/visuals.yml", "titles.match-defeat.header", "&c&lDEFEAT!"),
    TITLE_MATCH_DEFEAT_FOOTER("providers/visuals.yml", "titles.match-defeat.footer", "&c{winner} &fwon the Match!"),
    TITLE_MATCH_DEFEAT_FADE_IN("providers/visuals.yml", "titles.match-defeat.fade-in", 0),
    TITLE_MATCH_DEFEAT_STAY("providers/visuals.yml", "titles.match-defeat.stay", 20),
    TITLE_MATCH_DEFEAT_FADEOUT("providers/visuals.yml", "titles.match-defeat.fade-out", 20),

    TITLE_TEAM_SCORED_ENABLED_BOOLEAN("providers/visuals.yml", "titles.team-scored.enabled", true),
    TITLE_TEAM_SCORED_HEADER("providers/visuals.yml", "titles.team-scored.header", "{winner-color}&l{scorer} &fscored!"),
    TITLE_TEAM_SCORED_FOOTER("providers/visuals.yml", "titles.team-scored.footer", "&r{winner-color}{current-score} &7- &r{loser-color}{opponent-current-score}"),
    TITLE_TEAM_SCORED_FADE_IN("providers/visuals.yml", "titles.team-scored.fade-in", 0),
    TITLE_TEAM_SCORED_STAY("providers/visuals.yml", "titles.team-scored.stay", 15),
    TITLE_TEAM_SCORED_FADEOUT("providers/visuals.yml", "titles.team-scored.fade-out", 5),

    TITLE_CHECKPOINT_ENABLED_BOOLEAN("providers/visuals.yml", "titles.checkpoint.enabled", true),
    TITLE_CHECKPOINT_HEADER("providers/visuals.yml", "titles.checkpoint.header", "&aCHECKPOINT!"),
    TITLE_CHECKPOINT_FOOTER("providers/visuals.yml", "titles.checkpoint.footer", "&7({x}, {y}, {z})"),
    TITLE_CHECKPOINT_FADE_IN("providers/visuals.yml", "titles.checkpoint.fade-in", 10),
    TITLE_CHECKPOINT_STAY("providers/visuals.yml", "titles.checkpoint.stay", 20),
    TITLE_CHECKPOINT_FADEOUT("providers/visuals.yml", "titles.checkpoint.fade-out", 20),

    TITLE_MATCH_BED_DESTROYED_ENABLED_BOOLEAN("providers/visuals.yml", "titles.match-bed-destroyed.enabled", true),
    TITLE_MATCH_BED_DESTROYED_HEADER("providers/visuals.yml", "titles.match-bed-destroyed.header", "&c&lBED DESTROYED!"),
    TITLE_MATCH_BED_DESTROYED_FOOTER("providers/visuals.yml", "titles.match-bed-destroyed.footer", "&fYou will no longer respawn!"),
    TITLE_MATCH_BED_DESTROYED_FADE_IN("providers/visuals.yml", "titles.match-bed-destroyed.fade-in", 10),
    TITLE_MATCH_BED_DESTROYED_STAY("providers/visuals.yml", "titles.match-bed-destroyed.stay", 60),
    TITLE_MATCH_BED_DESTROYED_FADEOUT("providers/visuals.yml", "titles.match-bed-destroyed.fade-out", 10),

    TITLE_MATCH_SEEKERS_RELEASED_ENABLED_BOOLEAN("providers/visuals.yml", "titles.match-seekers-released.enabled", true),
    TITLE_MATCH_SEEKERS_RELEASED_HEADER("providers/visuals.yml", "titles.match-seekers-released.header", "&c&lSEEKERS RELEASED!"),
    TITLE_MATCH_SEEKERS_RELEASED_FOOTER("providers/visuals.yml", "titles.match-seekers-released.footer", "&fTHe hunt has begun!"),
    TITLE_MATCH_SEEKERS_RELEASED_FADE_IN("providers/visuals.yml", "titles.match-seekers-released.fade-in", 10),
    TITLE_MATCH_SEEKERS_RELEASED_STAY("providers/visuals.yml", "titles.match-seekers-released.stay", 40),
    TITLE_MATCH_SEEKERS_RELEASED_FADEOUT("providers/visuals.yml", "titles.match-seekers-released.fade-out", 20),

    TITLE_CAMP_PROTECTION_ENABLED_BOOLEAN("providers/visuals.yml", "titles.camp-protection.enabled", true),
    TITLE_CAMP_PROTECTION_HEADER("providers/visuals.yml", "titles.camp-protection.header", "&c&lCAMP PROTECTION"),
    TITLE_CAMP_PROTECTION_FOOTER("providers/visuals.yml", "titles.camp-protection.footer", "&fYou will take damage in &6{seconds} &fseconds!"),
    TITLE_CAMP_PROTECTION_FADE_IN("providers/visuals.yml", "titles.camp-protection.fade-in", 0),
    TITLE_CAMP_PROTECTION_STAY("providers/visuals.yml", "titles.camp-protection.stay", 23),
    TITLE_CAMP_PROTECTION_FADEOUT("providers/visuals.yml", "titles.camp-protection.fade-out", 20),

    TITLE_CAMP_PROTECTION_TAKING_DAMAGE_ENABLED_BOOLEAN("providers/visuals.yml", "titles.camp-protection-taking-damage.enabled", true),
    TITLE_CAMP_PROTECTION_TAKING_DAMAGE_HEADER("providers/visuals.yml", "titles.camp-protection-taking-damage.header", "&c&lTAKING DAMAGE!"),
    TITLE_CAMP_PROTECTION_TAKING_DAMAGE_FOOTER("providers/visuals.yml", "titles.camp-protection-taking-damage.footer", "&fMove down!"),
    TITLE_CAMP_PROTECTION_TAKING_DAMAGE_FADE_IN("providers/visuals.yml", "titles.camp-protection-taking-damage.fade-in", 0),
    TITLE_CAMP_PROTECTION_TAKING_DAMAGE_STAY("providers/visuals.yml", "titles.camp-protection-taking-damage.stay", 23),
    TITLE_CAMP_PROTECTION_TAKING_DAMAGE_FADEOUT("providers/visuals.yml", "titles.camp-protection-taking-damage.fade-out", 20),

    TITLE_PARTY_LEFT_ENABLED_BOOLEAN("providers/visuals.yml", "titles.party-left.enabled", true),
    TITLE_PARTY_LEFT_HEADER("providers/visuals.yml", "titles.party-left.header", "&c&l✖ Left {leader}'s Party"),
    TITLE_PARTY_LEFT_FOOTER("providers/visuals.yml", "titles.party-left.footer", "&7You've left your party."),
    TITLE_PARTY_LEFT_FADE_IN("providers/visuals.yml", "titles.party-left.fade-in", 5),
    TITLE_PARTY_LEFT_STAY("providers/visuals.yml", "titles.party-left.stay", 15),
    TITLE_PARTY_LEFT_FADEOUT("providers/visuals.yml", "titles.party-left.fade-out", 5),

    TITLE_PARTY_JOINED_ENABLED_BOOLEAN("providers/visuals.yml", "titles.party-joined.enabled", true),
    TITLE_PARTY_JOINED_HEADER("providers/visuals.yml", "titles.party-joined.header", "&a&l" + Symbol.CROSSED_SWORDS + " Joined {leader}'s Party"),
    TITLE_PARTY_JOINED_FOOTER("providers/visuals.yml", "titles.party-joined.footer", "&7Type /p for help."),
    TITLE_PARTY_JOINED_FADE_IN("providers/visuals.yml", "titles.party-joined.fade-in", 5),
    TITLE_PARTY_JOINED_STAY("providers/visuals.yml", "titles.party-joined.stay", 15),
    TITLE_PARTY_JOINED_FADEOUT("providers/visuals.yml", "titles.party-joined.fade-out", 5),

    TITLE_PARTY_DISBANDED_ENABLED_BOOLEAN("providers/visuals.yml", "titles.party-disbanded.enabled", true),
    TITLE_PARTY_DISBANDED_HEADER("providers/visuals.yml", "titles.party-disbanded.header", "&c&l✖ Party Disbanded"),
    TITLE_PARTY_DISBANDED_FOOTER("providers/visuals.yml", "titles.party-disbanded.footer", "&7You've removed your party."),
    TITLE_PARTY_DISBANDED_FADE_IN("providers/visuals.yml", "titles.party-disbanded.fade-in", 5),
    TITLE_PARTY_DISBANDED_STAY("providers/visuals.yml", "titles.party-disbanded.stay", 15),
    TITLE_PARTY_DISBANDED_FADEOUT("providers/visuals.yml", "titles.party-disbanded.fade-out", 5),

    TITLE_PARTY_CREATED_ENABLED_BOOLEAN("providers/visuals.yml", "titles.party-created.enabled", true),
    TITLE_PARTY_CREATED_HEADER("providers/visuals.yml", "titles.party-created.header", "&a&l" + Symbol.CROSSED_SWORDS + " Party Created"),
    TITLE_PARTY_CREATED_FOOTER("providers/visuals.yml", "titles.party-created.footer", "&7Type /p for help."),
    TITLE_PARTY_CREATED_FADE_IN("providers/visuals.yml", "titles.party-created.fade-in", 5),
    TITLE_PARTY_CREATED_STAY("providers/visuals.yml", "titles.party-created.stay", 15),
    TITLE_PARTY_CREATED_FADEOUT("providers/visuals.yml", "titles.party-created.fade-out", 5),

    ;

    private final String configName;
    private final String configPath;
    private final Object defaultValue;

    /**
     * Constructor for the VisualLocaleImpl enum.
     *
     * @param configName   The name of the configuration file.
     * @param configPath   The path within the configuration file.
     * @param defaultValue The default value if not set in the configuration.
     */
    VisualsLocaleImpl(String configName, String configPath, Object defaultValue) {
        this.configName = configName;
        this.configPath = configPath;
        this.defaultValue = defaultValue;
    }
}