package dev.revere.alley.adapter.knockback.impl;

import dev.revere.alley.adapter.knockback.Knockback;
import dev.revere.alley.adapter.knockback.enums.KnockbackType;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project alley-practice
 * @since 29/06/2025
 */
public class DefaultKnockbackImpl implements Knockback {
    @Override
    public KnockbackType getType() {
        return KnockbackType.DEFAULT;
    }

    @Override
    public void applyKnockback(Player player, String profile) {

    }
}