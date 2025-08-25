package dev.revere.alley.adapter.knockback;

import dev.revere.alley.bootstrap.lifecycle.Service;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface KnockbackAdapter extends Service {
    /**
     * Gets the name of the currently active knockback implementation.
     *
     * @return The IKnockback implementation for the current server type.
     */
    Knockback getKnockbackImplementation();
}