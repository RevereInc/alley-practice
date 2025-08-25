package dev.revere.alley.visual.scoreboard;

import dev.revere.alley.AlleyPlugin;
import dev.revere.alley.common.animation.AnimationService;
import dev.revere.alley.common.animation.AnimationType;
import dev.revere.alley.common.animation.internal.types.DotAnimation;
import dev.revere.alley.common.reflect.utility.ReflectionUtility;
import dev.revere.alley.core.profile.Profile;
import dev.revere.alley.core.profile.ProfileService;
import dev.revere.alley.core.profile.enums.ProfileState;
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
    default DotAnimation getDotAnimation() {
        return AlleyPlugin.getInstance().getService(AnimationService.class).getAnimation(DotAnimation.class, AnimationType.INTERNAL);
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

    /**
     * Safely counts the number of profiles in a given state.
     *
     * @param service The ProfileService to use for counting.
     * @param state   The ProfileState to count.
     * @return The count of profiles in the specified state, or 0 if an error occurs.
     */
    default int safeCountState(ProfileService service, ProfileState state) {
        try {
            return (int) service.getProfiles().values().parallelStream()
                    .filter(p -> p.getState() == state)
                    .count();
        } catch (Exception exception) {
            return 0;
        }
    }
}