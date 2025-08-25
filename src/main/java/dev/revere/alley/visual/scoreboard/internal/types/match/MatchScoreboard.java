package dev.revere.alley.visual.scoreboard.internal.types.match;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.animation.AnimationService;
import dev.revere.alley.common.animation.AnimationType;
import dev.revere.alley.common.animation.internal.types.DotAnimation;
import dev.revere.alley.common.reflect.utility.ReflectionUtility;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.feature.match.model.GameParticipant;
import dev.revere.alley.feature.match.model.internal.MatchGamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
public interface MatchScoreboard {
    /**
     * Gets the scoreboard lines for the given profile in a regular solo match.
     *
     * @param profile  The profile to get the scoreboard lines for.
     * @param player   The player whose scoreboard is being displayed.
     * @param you      The player whose scoreboard is being displayed.
     * @param opponent The opponent player.
     * @return The scoreboard lines.
     */
    List<String> getLines(Profile profile, Player player, GameParticipant<MatchGamePlayer> you, GameParticipant<MatchGamePlayer> opponent);

    /**
     * Gets the dot animation for the scoreboard.
     *
     * @return The dot animation.
     */
    default DotAnimation getDotAnimation() {
        return AlleyPlugin.getInstance().getService(AnimationService.class).getAnimation(DotAnimation.class, AnimationType.INTERNAL);
    }

    /**
     * Gets the corresponding color of the player including the player's name.
     *
     * @param profile The profile of the player.
     * @return The formatted player name with color.
     */
    default String getColoredName(Profile profile) {
        ChatColor nameColor = profile.getNameColor();
        String name = profile.getName();

        if (nameColor != null) {
            return nameColor + name;
        } else {
            return ChatColor.WHITE + name;
        }
    }

    /**
     * Gets the ping of the player by using reflect.
     *
     * @param player The player to get the ping for.
     * @return The ping of the player.
     */
    default int getPing(Player player) {
        if (player == null) {
            return 0;
        }

        return ReflectionUtility.getPing(player);
    }
}