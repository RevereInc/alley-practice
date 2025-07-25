package dev.revere.alley.provider.scoreboard.impl.match;

import dev.revere.alley.Alley;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.game.match.Match;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @since 26/06/2025
 */
public abstract class BaseMatchScoreboard implements MatchScoreboard {

    /**
     * Gets the path to the solo version of the scoreboard in the config.
     *
     * @return The configuration path.
     */
    protected abstract String getSoloConfigPath();

    /**
     * Gets the path to the team version of the scoreboard in the config.
     *
     * @return The configuration path.
     */
    protected abstract String getTeamConfigPath();

    @Override
    public List<String> getLines(Profile profile, Player player, GameParticipant<MatchGamePlayerImpl> you, GameParticipant<MatchGamePlayerImpl> opponent) {
        List<String> scoreboardLines = new ArrayList<>();
        Match match = profile.getMatch();
        String configPath = match.isTeamMatch() ? getTeamConfigPath() : getSoloConfigPath();

        for (String line : Alley.getInstance().getService(ConfigService.class).getScoreboardConfig().getStringList(configPath)) {
            scoreboardLines.add(replacePlaceholders(line, profile, player, you, opponent));
        }

        return scoreboardLines;
    }

    /**
     * Replaces all placeholders in a given line of the scoreboard.
     * Child classes should override this to add their own specific placeholders.
     *
     * @param line     The line with placeholders.
     * @param profile  The player's profile.
     * @param player   The player.
     * @param you      The player's game participant.
     * @param opponent The opponent's game participant.
     * @return The line with all placeholders replaced.
     */
    protected String replacePlaceholders(String line, Profile profile, Player player, GameParticipant<MatchGamePlayerImpl> you, GameParticipant<MatchGamePlayerImpl> opponent) {
        Match match = profile.getMatch();

        ProfileService profileService = Alley.getInstance().getService(ProfileService.class);

        String opponentName = match.isTeamMatch() ? getColoredName(profileService.getProfile(opponent.getLeader().getUuid())) + "' Team" : getColoredName(profileService.getProfile(opponent.getLeader().getUuid()));

        return CC.translate(line)
                .replace("{opponent}", opponentName)
                .replace("{player-ping}", String.valueOf(getPing(player)))
                .replace("{opponent-ping}", String.valueOf(getPing(opponent.getLeader().getTeamPlayer())))
                .replace("{duration}", match.getDuration())
                .replace("{arena}", match.getArena().getDisplayName() == null ? "&c&lNULL" : match.getArena().getDisplayName())
                .replace("{kit}", match.getKit().getDisplayName())
                .replace("{your-players}", String.valueOf(you.getPlayerSize()))
                .replace("{opponent-players}", String.valueOf(opponent.getPlayerSize()))
                .replace("{your-alive}", String.valueOf(you.getAlivePlayerSize()))
                .replace("{opponent-alive}", String.valueOf(opponent.getAlivePlayerSize()));
    }
}