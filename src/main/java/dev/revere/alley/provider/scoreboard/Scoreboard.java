package dev.revere.alley.provider.scoreboard;

import dev.revere.alley.Alley;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.animation.AnimationRepository;
import dev.revere.alley.tool.animation.enums.AnimationType;
import dev.revere.alley.tool.animation.type.internal.impl.DotAnimationImpl;
import dev.revere.alley.tool.reflection.utility.ReflectionUtility;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 30/04/2025
 */
public interface Scoreboard {
    /**
     * Gets the scoreboard lines for the given profile.
     *
     * @param profile The profile to get the scoreboard lines for.
     * @return The scoreboard lines.
     */
    List<String> getLines(Profile profile);

    /**
     * Gets the scoreboard lines for the given profile.
     *
     * @param profile The profile to get the scoreboard lines for.
     * @param player  The player to get the scoreboard lines for.
     * @return The scoreboard lines.
     */
    List<String> getLines(Profile profile, Player player);

    /**
     * Gets the dot animation for the scoreboard.
     *
     * @return The dot animation.
     */
    default DotAnimationImpl getDotAnimation() {
        return Alley.getInstance().getService(AnimationRepository.class).getAnimation(DotAnimationImpl.class, AnimationType.INTERNAL);
    }

    /**
     * Gets the ping of the player by using reflection.
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