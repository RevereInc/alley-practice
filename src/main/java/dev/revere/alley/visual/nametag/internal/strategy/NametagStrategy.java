package dev.revere.alley.visual.nametag.internal.strategy;

import dev.revere.alley.visual.nametag.NametagView;
import dev.revere.alley.visual.nametag.model.NametagContext;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/06/2025
 */
public interface NametagStrategy {
    /**
     * Creates a NametagView based on the given context.
     *
     * @param context The context containing the viewer and target.
     * @return A NametagView if this strategy applies, otherwise null.
     */
    NametagView createNametagView(NametagContext context);
}