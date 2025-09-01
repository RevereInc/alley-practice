package dev.revere.alley.feature.match.reward.annotation;

import dev.revere.alley.feature.match.reward.RewardType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Emmy
 * @project alley-practice
 * @since 01/09/2025
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RewardTypeProvider {
    /**
     * The type of reward this provider handles.
     *
     * @return The RewardType enum value.
     */
    RewardType type();
}