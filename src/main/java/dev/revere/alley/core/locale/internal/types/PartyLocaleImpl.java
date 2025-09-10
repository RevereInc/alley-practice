package dev.revere.alley.core.locale.internal.types;

import dev.revere.alley.core.locale.LocaleEntry;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 09/09/2025
 */
@Getter
public enum PartyLocaleImpl implements LocaleEntry {
    PARTY_CREATED(
            "messages.yml",
            "party.created",
            "&aYou have created a new party!"
    ),
    PARTY_DISBANDED(
            "messages.yml",
            "party.disbanded",
            "&cYour party has been disbanded!"
    ),
    NOT_IN_PARTY(
            "messages.yml",
            "party.not-in-party",
            "&cYou are not in a party."
    ),
    NOT_LEADER(
            "messages.yml",
            "party.not-leader",
            "&cYou are not the leader of the party."
    ),
    ALREADY_IN_PARTY(
            "messages.yml",
            "party.already-in-party",
            "&cYou are already in a party."
    ),
    PARTY_LEFT(
            "messages.yml",
            "party.left",
            "&cYou've left the party!"
    ),
    PLAYER_DISABLED_PARTY_INVITES(
            "messages.yml",
            "party.target-disabled-invites",
            "&c{player} has party invites disabled."
    ),
    DISABLED_PARTY_CHAT(
            "messages.yml",
            "party.disabled-chat",
            "&cYou have party messages disabled. &7(To enable: /togglepartymessages)"
    ),
    NO_PARTY_INVITE(
            "messages.yml",
            "party.no-invite",
            "&cYou do not have a party invitation from &6{player}&c."
    ),
    JOINED_PARTY(
            "messages.yml",
            "party.joined",
            "&aYou have joined &6{player}&a's party."
    ),
    PARTY_LOOKUP(
            "messages.yml",
            "party.lookup",
            Arrays.asList(
                    "",
                    " &6&l{leader}'s Party",
                    "  &f&l● &fLeader: &6{leader}",
                    "  &f&l● &fMembers: &6{members}",
                    "  &f&l● &fStatus: &6{status}",
                    "  &f&l● &fPrivacy: &6{privacy}",
                    ""
            )
    ),

    ;

    private final String configName;
    private final String configPath;
    private final Object defaultValue;

    /**
     * Constructor for the PartyLocale enum.
     *
     * @param configName   The name of the configuration file.
     * @param configPath   The path to the specific string within the configuration file.
     * @param defaultValue The default value for the locale entry.
     */
    PartyLocaleImpl(String configName, String configPath, Object defaultValue) {
        this.configName = configName;
        this.configPath = configPath;
        this.defaultValue = defaultValue;
    }
}