package dev.revere.alley.provider.scoreboard.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.game.match.impl.DefaultMatch;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.provider.scoreboard.Scoreboard;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
public class SpectatorScoreboardImpl implements Scoreboard {
    @Override
    public List<String> getLines(Profile profile) {
        ConfigService configService = Alley.getInstance().getService(ConfigService.class);

        List<String> scoreboardLines = new ArrayList<>();

        GameParticipant<MatchGamePlayerImpl> participantA = getParticipantSafely(profile.getMatch().getParticipants(), 0);
        GameParticipant<MatchGamePlayerImpl> participantB = getParticipantSafely(profile.getMatch().getParticipants(), 1);

        String playerAName = getPlayerNameSafely(participantA);
        String playerBName = getPlayerNameSafely(participantB);
        String pingA = getPingSafely(participantA);
        String pingB = getPingSafely(participantB);

        if (profile.getMatch() instanceof DefaultMatch) {
            for (String line : configService.getScoreboardConfig().getStringList("scoreboard.lines.spectating.regular-match")) {
                scoreboardLines.add(CC.translate(line)
                        .replaceAll("\\{playerA}", playerAName)
                        .replaceAll("\\{playerB}", playerBName)
                        .replaceAll("\\{pingA}", pingA)
                        .replaceAll("\\{pingB}", pingB)
                        .replaceAll("\\{colorA}", String.valueOf(((DefaultMatch) profile.getMatch()).getTeamAColor()))
                        .replaceAll("\\{colorB}", String.valueOf(((DefaultMatch) profile.getMatch()).getTeamBColor()))
                        .replaceAll("\\{duration}", profile.getMatch().getDuration())
                        .replaceAll("\\{arena}", profile.getMatch().getArena().getDisplayName() == null ? "&c&lNULL" : profile.getMatch().getArena().getDisplayName())
                        .replaceAll("\\{kit}", profile.getMatch().getKit().getDisplayName()));
            }
        } else if (profile.getFfaMatch() != null) {
            for (String line : configService.getScoreboardConfig().getStringList("scoreboard.lines.spectating.ffa")) {
                scoreboardLines.add(CC.translate(line)
                        .replaceAll("\\{arena}", profile.getFfaMatch().getArena().getDisplayName() == null ? "&c&lNULL" : profile.getFfaMatch().getArena().getDisplayName())
                        .replaceAll("\\{kit}", profile.getFfaMatch().getKit().getDisplayName()));
            }
        }

        return scoreboardLines;
    }

    @Override
    public List<String> getLines(Profile profile, Player player) {
        return Collections.emptyList();
    }

    /**
     * Safely gets a participant from the list at the specified index.
     *
     * @param participants The list of participants
     * @param index        The index to retrieve
     * @return The participant at the index, or null if not available
     */
    private GameParticipant<MatchGamePlayerImpl> getParticipantSafely(List<GameParticipant<MatchGamePlayerImpl>> participants, int index) {
        if (participants == null || index >= participants.size() || index < 0) {
            return null;
        }
        return participants.get(index);
    }

    /**
     * Safely gets the player name from a participant.
     *
     * @param participant The participant to get the name from
     * @return The player name, or "Disconnected" if not available
     */
    private String getPlayerNameSafely(GameParticipant<MatchGamePlayerImpl> participant) {
        if (participant == null) {
            return "&c&lDisconnected";
        }

        if (!participant.getPlayers().isEmpty()) {
            return participant.getPlayers().get(0).getUsername();
        }

        if (!participant.getAllPlayers().isEmpty()) {
            return "&7" + participant.getAllPlayers().get(0).getUsername() + " &c(DC)";
        }

        return "&c&lDisconnected";
    }

    /**
     * Safely gets the ping from a participant.
     *
     * @param participant The participant to get the ping from
     * @return The ping as a string, or "0" if not available
     */
    private String getPingSafely(GameParticipant<MatchGamePlayerImpl> participant) {
        if (participant == null) {
            return "0";
        }

        if (!participant.getPlayers().isEmpty()) {
            Player player = participant.getPlayers().get(0).getTeamPlayer();
            if (player != null) {
                return String.valueOf(this.getPing(player));
            }
        }
        return "0";
    }
}