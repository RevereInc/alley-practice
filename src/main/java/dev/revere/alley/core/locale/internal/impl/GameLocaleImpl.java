package dev.revere.alley.core.locale.internal.impl;

import dev.revere.alley.core.locale.LocaleEntry;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author Emmy
 * @project alley-practice
 * @since 12/09/2025
 */
@Getter
public enum GameLocaleImpl implements LocaleEntry {
    MATCH_STARTED_DISCLAIMER_ENABLED_BOOLEAN("messages.yml", "match.started.kit-disclaimer.enabled", true),
    MATCH_STARTED_DISCLAIMER_FORMAT("messages.yml", "match.started.kit-disclaimer.format", Arrays.asList(
            "",
            "&6&l{kit-name}",
            " &6&l│ &f{kit-disclaimer}",
            ""
    )),

    MATCH_STARTED_MESSAGE_ENABLED_BOOLEAN("messages.yml", "match.started.message.regular.enabled", true),
    MATCH_STARTED_MESSAGE_FORMAT("messages.yml", "match.started.message.regular.format", Collections.singletonList("&fMatch has started.")),

    MATCH_ROUND_STARTED_MESSAGE_ENABLED_BOOLEAN("messages.yml", "match.started.message.round.enabled", true),
    MATCH_ROUND_STARTED_MESSAGE_FORMAT("messages.yml", "match.started.message.round.format", Collections.singletonList("&fRound {current-round} has started.")),

    MATCH_STARTING_MESSAGE_ENABLED_BOOLEAN("messages.yml", "match.starting.message.enabled", true),
    MATCH_STARTING_MESSAGE_FORMAT("messages.yml", "match.starting.message.format", Collections.singletonList("&a{stage}...")),

    MATCH_RESPAWNING_MESSAGE_ENABLED_BOOLEAN("messages.yml", "match.respawning.message.enabled", true),
    MATCH_RESPAWNING_MESSAGE_FORMAT("messages.yml", "match.respawning.message.format", Collections.singletonList("&a{seconds}...")),

    MATCH_TIME_LIMIT_EXCEEDED_FORMAT("messages.yml", "match.ending.time-limit-exceeded.format", Collections.singletonList("&cMatch ended due to reaching the time limit of &6{time-limit} minutes&c.")),

    MATCH_SCORED_MESSAGE_ENABLED_BOOLEAN("messages.yml", "match.scored.enabled", true),
    MATCH_SCORED_MESSAGE_SOLO_FORMAT("messages.yml", "match.scored.format.solo", Arrays.asList(
            ""
            , "{winner-color}&l{scorer} &6&lscored!"
            , "{winner-color}&l{winner-goals} &7- {loser-color}&l{loser-goals}"
            , ""
    )),
    MATCH_SCORED_MESSAGE_TEAM_FORMAT("messages.yml", "match.scored.format.team", Arrays.asList(
            ""
            , "{winner-color}&l{winner}'s Team &6&lscored!"
            , "{winner-color}&l{winner-goals} &7- {loser-color}&l{loser-goals}"
            , ""
    )),

    MATCH_BED_DESTRUCTION_MESSAGE_ENABLED_BOOLEAN("messages.yml", "match.bed-destruction.enabled", true),
    MATCH_BED_DESTRUCTION_MESSAGE_FORMAT("messages.yml", "match.bed-destruction.format", Arrays.asList(
            "",
            "&6&lBED DESTRUCTION! &6{bed-color}{bed} &fwas destroyed by &6{breaker-color}{breaker}&f!",
            ""
    )),

    MATCH_ENDED_MATCH_RESULT_REGULAR_FORMAT("messages.yml", "match.ended.match-result.regular.format", Arrays.asList(
            "",
            "&6&lMatch Results: &7(click to view)",
            "&aWinner: &f{winner} &7| &cLoser: &f{loser}",
            ""
    )),
    MATCH_ENDED_MATCH_RESULT_REGULAR_WINNER_COMMAND("messages.yml", "match.ended.match-result.regular.winner.command", "/inventory {winner}"),
    MATCH_ENDED_MATCH_RESULT_REGULAR_WINNER_HOVER("messages.yml", "match.ended.match-result.regular.winner.hover", "&aClick to view {winner}'s inventory"),
    MATCH_ENDED_MATCH_RESULT_REGULAR_LOSER_COMMAND("messages.yml", "match.ended.match-result.regular.loser.command", "/inventory {loser}"),
    MATCH_ENDED_MATCH_RESULT_REGULAR_LOSER_HOVER("messages.yml", "match.ended.match-result.regular.loser.hover", "&cClick to view {loser}'s inventory"),

    MATCH_ENDED_MATCH_RESULT_ELO_CHANGES_ENABLED_BOOLEAN("messages.yml", "match.ended.elo-changes.enabled", true),
    MATCH_ENDED_MATCH_RESULT_ELO_CHANGES_FORMAT("messages.yml", "match.ended.elo-changes.format", Arrays.asList(
            " &6&lElo Changes",
            "  &6&l● &f{winner} &a+{math-winner-elo} &7(&f{old-winner-elo} &7-> &f{new-winner-elo})",
            "  &6&l● &f{loser} &c-{math-loser-elo} &7(&f{old-loser-elo} &7-> &f{new-loser-elo})",
            ""
    )),

    ;

    private final String configName;
    private final String configPath;
    private final Object defaultValue;

    /**
     * Constructor for the GameLocaleImpl enum.
     *
     * @param configName   The name of the configuration file.
     * @param configPath   The path to the specific string within the configuration file.
     * @param defaultValue The default value for the locale entry.
     */
    GameLocaleImpl(String configName, String configPath, Object defaultValue) {
        this.configName = configName;
        this.configPath = configPath;
        this.defaultValue = defaultValue;
    }
}