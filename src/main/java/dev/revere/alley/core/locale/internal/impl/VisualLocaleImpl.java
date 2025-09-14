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
public enum VisualLocaleImpl implements LocaleEntry {
    TITLE_MATCH_STARTING_HEADER("providers/visuals.yml", "titles.match-starting.header", "&a&l{stage}"),
    TITLE_MATCH_STARTING_FOOTER("providers/visuals.yml", "titles.match-starting.footer", ""),
    TITLE_MATCH_STARTING_FADE_IN("providers/visuals.yml", "titles.match-starting.fade-in", 0),
    TITLE_MATCH_STARTING_STAY("providers/visuals.yml", "titles.match-starting.stay", 23),
    TITLE_MATCH_STARTING_FADEOUT("providers/visuals.yml", "titles.match-starting.fade-out", 20),

    TITLE_MATCH_RESTARTING_ROUND_HEADER("providers/visuals.yml", "titles.match-restarting-round.header", "&a&l{stage}"),
    TITLE_MATCH_RESTARTING_ROUND_FOOTER("providers/visuals.yml", "titles.match-restarting-round.footer", "&fRound #{current-round}"),
    TITLE_MATCH_RESTARTING_ROUND_FADE_IN("providers/visuals.yml", "titles.match-restarting-round.fade-in", 0),
    TITLE_MATCH_RESTARTING_ROUND_STAY("providers/visuals.yml", "titles.match-restarting-round.stay", 23),
    TITLE_MATCH_RESTARTING_ROUND_FADEOUT("providers/visuals.yml", "titles.match-restarting-round.fade-out", 20),

    TITLE_MATCH_STARTED_HEADER("providers/visuals.yml", "titles.match-started.header", "&6&lMatch started"),
    TITLE_MATCH_STARTED_FOOTER("providers/visuals.yml", "titles.match-started.footer", "&fGood Luck!"),
    TITLE_MATCH_STARTED_FADE_IN("providers/visuals.yml", "titles.match-started.fade-in", 0),
    TITLE_MATCH_STARTED_STAY("providers/visuals.yml", "titles.match-started.stay", 40),
    TITLE_MATCH_STARTED_FADEOUT("providers/visuals.yml", "titles.match-started.fade-out", 20),

    TITLE_MATCH_RESPAWNING_HEADER("providers/visuals.yml", "titles.match-respawning.header", "&6&lRespawn"),
    TITLE_MATCH_RESPAWNING_FOOTER("providers/visuals.yml", "titles.match-respawning.footer", "&fRespawning in &6{seconds}s"),
    TITLE_MATCH_RESPAWNING_FADE_IN("providers/visuals.yml", "titles.match-respawning.fade-in", 0),
    TITLE_MATCH_RESPAWNING_STAY("providers/visuals.yml", "titles.match-respawning.stay", 23),
    TITLE_MATCH_RESPAWNING_FADEOUT("providers/visuals.yml", "titles.match-respawning.fade-out", 20),

    TITLE_MATCH_VICTORY_HEADER("providers/visuals.yml", "titles.match-victory.header", "&a&lVICTORY!"),
    TITLE_MATCH_VICTORY_FOOTER("providers/visuals.yml", "titles.match-victory.footer", "&a{winner} &fwon the Match!"),
    TITLE_MATCH_VICTORY_FADE_IN("providers/visuals.yml", "titles.match-victory.fade-in", 10),
    TITLE_MATCH_VICTORY_STAY("providers/visuals.yml", "titles.match-victory.stay", 20),
    TITLE_MATCH_VICTORY_FADEOUT("providers/visuals.yml", "titles.match-victory.fade-out", 20),

    TITLE_MATCH_DEFEAT_HEADER("providers/visuals.yml", "titles.match-defeat.header", "&c&lDEFEAT!"),
    TITLE_MATCH_DEFEAT_FOOTER("providers/visuals.yml", "titles.match-defeat.footer", "&c{winner} &fwon the Match!"),
    TITLE_MATCH_DEFEAT_FADE_IN("providers/visuals.yml", "titles.match-defeat.fade-in", 10),
    TITLE_MATCH_DEFEAT_STAY("providers/visuals.yml", "titles.match-defeat.stay", 20),
    TITLE_MATCH_DEFEAT_FADEOUT("providers/visuals.yml", "titles.match-defeat.fade-out", 20),

    TITLE_TEAM_SCORED_HEADER("providers/visuals.yml", "titles.team-scored.header", "&a&l{scorer} &fhas scored!"),
    TITLE_TEAM_SCORED_FOOTER("providers/visuals.yml", "titles.team-scored.footer", "&f{current-score} &7/&f {max-rounds}"),
    TITLE_TEAM_SCORED_FADE_IN("providers/visuals.yml", "titles.team-scored.fade-in", 10),
    TITLE_TEAM_SCORED_STAY("providers/visuals.yml", "titles.team-scored.stay", 20),
    TITLE_TEAM_SCORED_FADEOUT("providers/visuals.yml", "titles.team-scored.fade-out", 20),

    TITLE_CHECKPOINT_HEADER("providers/visuals.yml", "titles.checkpoint.header", "&aCHECKPOINT!"),
    TITLE_CHECKPOINT_FOOTER("providers/visuals.yml", "titles.checkpoint.footer", "&7({x}, {y}, {z})"),
    TITLE_CHECKPOINT_FADE_IN("providers/visuals.yml", "titles.checkpoint.fade-in", 10),
    TITLE_CHECKPOINT_STAY("providers/visuals.yml", "titles.checkpoint.stay", 20),
    TITLE_CHECKPOINT_FADEOUT("providers/visuals.yml", "titles.checkpoint.fade-out", 20),

    TITLE_MATCH_BED_DESTROYED_HEADER("providers/visuals.yml", "titles.match-bed-destroyed.header", "&c&lBED DESTROYED!"),
    TITLE_MATCH_BED_DESTROYED_FOOTER("providers/visuals.yml", "titles.match-bed-destroyed.footer", "&fYou will no longer respawn!"),
    TITLE_MATCH_BED_DESTROYED_FADE_IN("providers/visuals.yml", "titles.match-bed-destroyed.fade-in", 10),
    TITLE_MATCH_BED_DESTROYED_STAY("providers/visuals.yml", "titles.match-bed-destroyed.stay", 60),
    TITLE_MATCH_BED_DESTROYED_FADEOUT("providers/visuals.yml", "titles.match-bed-destroyed.fade-out", 10),

    TITLE_MATCH_SEEKERS_RELEASED_HEADER("providers/visuals.yml", "titles.match-seekers-released.header", "&c&lSEEKERS RELEASED!"),
    TITLE_MATCH_SEEKERS_RELEASED_FOOTER("providers/visuals.yml", "titles.match-seekers-released.footer", "&fTHe hunt has begun!"),
    TITLE_MATCH_SEEKERS_RELEASED_FADE_IN("providers/visuals.yml", "titles.match-seekers-released.fade-in", 10),
    TITLE_MATCH_SEEKERS_RELEASED_STAY("providers/visuals.yml", "titles.match-seekers-released.stay", 40),
    TITLE_MATCH_SEEKERS_RELEASED_FADEOUT("providers/visuals.yml", "titles.match-seekers-released.fade-out", 20),

    TITLE_CAMP_PROTECTION_HEADER("providers/visuals.yml", "titles.camp-protection.header", "&cCAMP PROTECTION"),
    TITLE_CAMP_PROTECTION_FOOTER("providers/visuals.yml", "titles.camp-protection.footer", "&fYou will take damage in {seconds} seconds!"),
    TITLE_CAMP_PROTECTION_FADE_IN("providers/visuals.yml", "titles.camp-protection.fade-in", 0),
    TITLE_CAMP_PROTECTION_STAY("providers/visuals.yml", "titles.camp-protection.stay", 23),
    TITLE_CAMP_PROTECTION_FADEOUT("providers/visuals.yml", "titles.camp-protection.fade-out", 20),

    TITLE_CAMP_PROTECTION_TAKING_DAMAGE_HEADER("providers/visuals.yml", "titles.camp-protection-taking-damage.header", "&cTAKING DAMAGE!"),
    TITLE_CAMP_PROTECTION_TAKING_DAMAGE_FOOTER("providers/visuals.yml", "titles.camp-protection-taking-damage.footer", "&fMove down!"),
    TITLE_CAMP_PROTECTION_TAKING_DAMAGE_FADE_IN("providers/visuals.yml", "titles.camp-protection-taking-damage.fade-in", 0),
    TITLE_CAMP_PROTECTION_TAKING_DAMAGE_STAY("providers/visuals.yml", "titles.camp-protection-taking-damage.stay", 23),
    TITLE_CAMP_PROTECTION_TAKING_DAMAGE_FADEOUT("providers/visuals.yml", "titles.camp-protection-taking-damage.fade-out", 20),

    TITLE_PARTY_DISBANDED_HEADER("providers/visuals.yml", "titles.party-disbanded.header", "&c&l✖ Party Disbanded"),
    TITLE_PARTY_DISBANDED_FOOTER("providers/visuals.yml", "titles.party-disbanded.footer", "&7You've removed your party."),
    TITLE_PARTY_DISBANDED_FADE_IN("providers/visuals.yml", "titles.party-disbanded.fade-in", 10),
    TITLE_PARTY_DISBANDED_STAY("providers/visuals.yml", "titles.party-disbanded.stay", 15),
    TITLE_PARTY_DISBANDED_FADEOUT("providers/visuals.yml", "titles.party-disbanded.fade-out", 10),

    TITLE_PARTY_CREATED_HEADER("providers/visuals.yml", "titles.party-created.header", "&a&l" + Symbol.CROSSED_SWORDS + " Party Created"),
    TITLE_PARTY_CREATED_FOOTER("providers/visuals.yml", "titles.party-created.footer", "&7Type /p for help."),
    TITLE_PARTY_CREATED_FADE_IN("providers/visuals.yml", "titles.party-created.fade-in", 10),
    TITLE_PARTY_CREATED_STAY("providers/visuals.yml", "titles.party-created.stay", 15),
    TITLE_PARTY_CREATED_FADEOUT("providers/visuals.yml", "titles.party-created.fade-out", 10),

    ACTIONBAR_HEALTH_INDICATOR_MESSAGE_FORMAT("providers/visuals.yml", "action-bars.health-indicator.message-format", "{name-color}{target} {health-bar}"),
    ACTIONBAR_HEALTH_INDICATOR_SYMBOL_APPEARANCE("providers/visuals.yml", "action-bars.health-indicator.symbol.appearance", "❤"),
    ACTIONBAR_HEALTH_INDICATOR_SYMBOL_COLOR_FULL("providers/visuals.yml", "action-bars.health-indicator.symbol.color.full", "&4&l"),
    ACTIONBAR_HEALTH_INDICATOR_SYMBOL_COLOR_HALF("providers/visuals.yml", "action-bars.health-indicator.symbol.color.half", "&c&l"),
    ACTIONBAR_HEALTH_INDICATOR_SYMBOL_COLOR_EMPTY("providers/visuals.yml", "action-bars.health-indicator.symbol.color.empty", "&7&l"),

    TAB_BOOLEAN("providers/tablist.yml", "tablist.enabled", true),
    TAB_HEADER("providers/tablist.yml", "tablist.header", Arrays.asList(
            "",
            "&6&lAlley Network",
            ""
    )),
    TAB_FOOTER("providers/tablist.yml", "tablist.footer", Arrays.asList(
            "",
            "&fPlaying with &6&lAlley &fdeveloped by &6&lEmmy &f& &6&lRemi",
            ""
    ));

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
    VisualLocaleImpl(String configName, String configPath, Object defaultValue) {
        this.configName = configName;
        this.configPath = configPath;
        this.defaultValue = defaultValue;
    }
}