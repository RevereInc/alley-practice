package dev.revere.alley.visual.nametag.internal.strategy.impl;

import dev.revere.alley.common.text.CC;
import dev.revere.alley.visual.nametag.NametagView;
import dev.revere.alley.visual.nametag.internal.strategy.NametagStrategy;
import dev.revere.alley.visual.nametag.model.NametagContext;
import org.bukkit.ChatColor;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/06/2025
 */
public class DefaultStrategyImpl implements NametagStrategy {
    @Override
    public NametagView createNametagView(NametagContext context) {
        String prefix = context.getTargetProfile().getNameColor().toString();

        if (prefix == null || prefix.isEmpty()) {
            prefix = ChatColor.GRAY.toString();
        }

        return new NametagView(CC.translate(prefix), "");
    }
}